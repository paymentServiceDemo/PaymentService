package com.nuno.payments.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nuno.payments.PaymentsApplication
import com.nuno.payments.generated.client.ApiClient
import com.nuno.payments.generated.client.api.PaymentsApi
import com.nuno.payments.generated.client.model.*
import com.nuno.payments.infrastructure.persistence.payment.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import retrofit2.Response
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = [PaymentsApplication], webEnvironment = WebEnvironment.RANDOM_PORT)
class PaymentIT extends Specification {

    PaymentsApi paymentsApi

    @Autowired
    PaymentRepository paymentRepository

    @Autowired
    ObjectMapper objectMapper

    @Value('${local.server.port}')
    int port

    def setup() {
        paymentsApi = generateClient(PaymentsApi) as PaymentsApi
        cleanup()
    }

    def "Create Payment - Success"() {
        given: "A payment Request"
        def paymentDto = defaultPaymentDto()

        when: "I call the API"
        def result = calling({ paymentsApi.createPayment(paymentDto).execute() }, HttpStatus.OK) as IdResponse

        then: "Payment is created"
        result.id != null
        def entity = paymentRepository.findOneByExternalId paymentDto.getId()
        entity.type == "Payment"
        entity.externalId == "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
        entity.version == 0
        entity.organisationId == "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"
        entity.attributes.amount == "100.21"
        entity.attributes.beneficiaryParty.accountName == "W Owens"
        entity.attributes.beneficiaryParty.accountNumber == "31926819"
        entity.attributes.beneficiaryParty.accountNumberCode == "BBAN"
        entity.attributes.beneficiaryParty.accountType == 0
        entity.attributes.beneficiaryParty.address == "1 The Beneficiary Localtown SE2"
        entity.attributes.beneficiaryParty.bankId == "403000"
        entity.attributes.beneficiaryParty.bankIdCode == "GBDSC"
        entity.attributes.beneficiaryParty.name == "Wilfred Jeremiah Owens"
        entity.attributes.chargesInformation.bearerCode == "SHAR"
        entity.attributes.chargesInformation.senderCharges.size() == 2
        entity.attributes.chargesInformation.senderCharges.find { it.amount == "5.00" && it.currency == "GBP" }
        entity.attributes.chargesInformation.senderCharges.find { it.amount == "10.00" && it.currency == "USD" }
        entity.attributes.chargesInformation.receiverChargesAmount == "1.00"
        entity.attributes.chargesInformation.receiverChargesCurrency == "USD"
        entity.attributes.currency == "GBP"
        entity.attributes.debtorParty.accountName == "EJ Brown Black"
        entity.attributes.debtorParty.accountNumber == "GB29XABC10161234567801"
        entity.attributes.debtorParty.accountNumberCode == "IBAN"
        entity.attributes.debtorParty.address == "10 Debtor Crescent Sourcetown NE1"
        entity.attributes.debtorParty.bankId == "203301"
        entity.attributes.debtorParty.bankIdCode == "GBDSC"
        entity.attributes.debtorParty.name == "Emelia Jane Brown"
        entity.attributes.endToEndReference == "Wil piano Jan"
        entity.attributes.fx.contractReference == "FX123"
        entity.attributes.fx.exchangeRate == "2.00000"
        entity.attributes.fx.originalAmount == "200.42"
        entity.attributes.fx.originalCurrency == "USD"
        entity.attributes.numericReference == "1002001"
        entity.attributes.paymentId == "123456789012345678"
        entity.attributes.paymentPurpose == "Paying for goods/services"
        entity.attributes.paymentScheme == "FPS"
        entity.attributes.paymentType == "Credit"
        entity.attributes.processingDate == "2017-01-18"
        entity.attributes.reference == "Payment for Em's piano lessons"
        entity.attributes.schemePaymentSubType == "InternetBanking"
        entity.attributes.schemePaymentType == "ImmediatePayment"
        entity.attributes.sponsorParty.accountNumber == "56781234"
        entity.attributes.sponsorParty.bankId == "123123"
        entity.attributes.sponsorParty.bankIdCode == "GBDSC"
    }

    def "Create Payment - Failure payment exists"() {
        given: "A payment Request"
        def paymentDto = defaultPaymentDto()

        and: "payment already exists in the system"
        paymentRepository.save defaultPaymentEntity()

        when: "I call the API"
        def result = calling({
            paymentsApi.createPayment(paymentDto).execute()
        }, HttpStatus.BAD_REQUEST) as ErrorResponse

        then: "Payment is created"
        result.errors.first().fieldName == 'ID'
        result.errors.first().message == 'Payment with ID 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43 already exists in the system'
    }

    def "Delete Payment - Success"() {
        given: "A existing payment"
        def paymentId = paymentRepository.save(defaultPaymentEntity()).getExternalId()

        when: "I call the API"
        calling({ paymentsApi.deletePayment(paymentId).execute() }, HttpStatus.OK)

        then: "Payment is deleted"
        paymentRepository.findOneByExternalId(paymentId) == null
    }

    def "Delete Payment - Failure Payment not found"() {
        when: "I call the API"
        def response = calling({ paymentsApi.deletePayment('123').execute() }, HttpStatus.NOT_FOUND) as ErrorResponse

        then: "Payment is deleted"
        response.errors.first().fieldName == 'ID'
        response.errors.first().message == 'Payment with ID 123 not found'
    }

    def "Get payment - Success"() {
        given: "A existing payment"
        def paymentId = paymentRepository.save(defaultPaymentEntity()).getExternalId()

        when: "I call the API"
        def response = calling({ paymentsApi.getPayment(paymentId).execute() }, HttpStatus.OK) as PaymentDto

        then: "Payment is returned"
        response != null
        response == defaultPaymentDto()
    }


    def "Get all payment with data - Success"() {
        given: "Payment"
        def payment = paymentRepository.save(defaultPaymentEntity())
        def dtoExpectedId = payment.externalId

        and: "Another payment"
        def dto2ExpectedId = "Unique Id"
        def payment2 = defaultPaymentEntity()
        payment2.setExternalId dto2ExpectedId
        paymentRepository.save payment2

        when: "I call the API"
        def response = calling({ paymentsApi.getAllPayments().execute() }, HttpStatus.OK) as PaymentDto[]

        then: "Payment is returned"
        response != null
        response.size() == 2
        response.find { it.id == dtoExpectedId }
        response.find { it.id == dto2ExpectedId }

    }

    def "Get all payment with no data - Success"() {
        when: "I call the API without any data"
        def response = calling({ paymentsApi.getAllPayments().execute() }, HttpStatus.OK) as PaymentDto[]

        then: "Payment is returned"
        response != null
        response.size() == 0
    }

    def "Update Payment - Success"() {
        given: "A payment Request"
        def paymentDto = defaultPaymentDto()

        and: "A entity already exists"
        def entity = new Payment(
                "type": 'Test',
                "externalId": '4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43',
                "version": 99,
                "organisationId": 'X'
        )

        paymentRepository.save(entity)

        when: "I call the API"
        calling({ paymentsApi.updatePayment(paymentDto).execute() }, HttpStatus.OK)

        then: "Payment is updated"
        def updatedEntity = paymentRepository.findOneByExternalId(entity.externalId)
        updatedEntity.type == "Payment"
        updatedEntity.version == 0.00
        updatedEntity.organisationId == "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"
        updatedEntity.attributes != null
    }

    private static PaymentDto defaultPaymentDto() {
        new PaymentDto(
                "type": "Payment",
                "id": "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43",
                "version": 0.00,
                "organisationId": "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb",
                "attributes": new PaymentDtoAttributes(
                        "amount": "100.21",
                        "beneficiaryParty": new PaymentDtoAttributesBeneficiaryParty(
                                "accountName": "W Owens",
                                "accountNumber": "31926819",
                                "accountNumberCode": "BBAN",
                                "accountType": 0.00,
                                "address": "1 The Beneficiary Localtown SE2",
                                "bankId": "403000",
                                "bankIdCode": "GBDSC",
                                "name": "Wilfred Jeremiah Owens"
                        ),
                        "chargesInformation": new PaymentDtoAttributesChargesInformation(
                                "bearerCode": "SHAR",
                                "senderCharges": [
                                        new PaymentDtoAttributesChargesInformationSenderCharges(
                                                "amount": "5.00",
                                                "currency": "GBP"
                                        ),
                                        new PaymentDtoAttributesChargesInformationSenderCharges(
                                                "amount": "10.00",
                                                "currency": "USD"
                                        )
                                ],
                                "receiverChargesAmount": "1.00",
                                "receiverChargesCurrency": "USD"
                        ),
                        "currency": "GBP",
                        "debtorParty": new PaymentDtoAttributesDebtorParty(
                                "accountName": "EJ Brown Black",
                                "accountNumber": "GB29XABC10161234567801",
                                "accountNumberCode": "IBAN",
                                "address": "10 Debtor Crescent Sourcetown NE1",
                                "bankId": "203301",
                                "bankIdCode": "GBDSC",
                                "name": "Emelia Jane Brown"
                        ),
                        "endToEndReference": "Wil piano Jan",
                        "fx": new PaymentDtoAttributesFx(
                                "contractReference": "FX123",
                                "exchangeRate": "2.00000",
                                "originalAmount": "200.42",
                                "originalCurrency": "USD"
                        ),
                        "numericReference": "1002001",
                        "paymentId": "123456789012345678",
                        "paymentPurpose": "Paying for goods/services",
                        "paymentScheme": "FPS",
                        "paymentType": "Credit",
                        "processingDate": "2017-01-18",
                        "reference": "Payment for Em's piano lessons",
                        "schemePaymentSubType": "InternetBanking",
                        "schemePaymentType": "ImmediatePayment",
                        "sponsorParty": new PaymentDtoAttributesSponsorParty(
                                "accountNumber": "56781234",
                                "bankId": "123123",
                                "bankIdCode": "GBDSC"
                        )
                )
        )
    }

    private static Payment defaultPaymentEntity() {
        new Payment(
                "type": "Payment",
                "externalId": "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43",
                "version": 0,
                "organisationId": "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb",
                "attributes": new Attributes(
                        "amount": "100.21",
                        "beneficiaryParty": new BeneficiaryParty(
                                "accountName": "W Owens",
                                "accountNumber": "31926819",
                                "accountNumberCode": "BBAN",
                                "accountType": 0,
                                "address": "1 The Beneficiary Localtown SE2",
                                "bankId": "403000",
                                "bankIdCode": "GBDSC",
                                "name": "Wilfred Jeremiah Owens"
                        ),
                        "chargesInformation": new ChargesInformation(
                                "bearerCode": "SHAR",
                                "senderCharges": [
                                        new SenderCharge(
                                                "amount": "5.00",
                                                "currency": "GBP"
                                        ),
                                        new SenderCharge(
                                                "amount": "10.00",
                                                "currency": "USD"
                                        )
                                ],
                                "receiverChargesAmount": "1.00",
                                "receiverChargesCurrency": "USD"
                        ),
                        "currency": "GBP",
                        "debtorParty": new DebtorParty(
                                "accountName": "EJ Brown Black",
                                "accountNumber": "GB29XABC10161234567801",
                                "accountNumberCode": "IBAN",
                                "address": "10 Debtor Crescent Sourcetown NE1",
                                "bankId": "203301",
                                "bankIdCode": "GBDSC",
                                "name": "Emelia Jane Brown"
                        ),
                        "endToEndReference": "Wil piano Jan",
                        "fx": new Fx(
                                "contractReference": "FX123",
                                "exchangeRate": "2.00000",
                                "originalAmount": "200.42",
                                "originalCurrency": "USD"
                        ),
                        "numericReference": "1002001",
                        "paymentId": "123456789012345678",
                        "paymentPurpose": "Paying for goods/services",
                        "paymentScheme": "FPS",
                        "paymentType": "Credit",
                        "processingDate": "2017-01-18",
                        "reference": "Payment for Em's piano lessons",
                        "schemePaymentSubType": "InternetBanking",
                        "schemePaymentType": "ImmediatePayment",
                        "sponsorParty": new SponsorParty(
                                "accountNumber": "56781234",
                                "bankId": "123123",
                                "bankIdCode": "GBDSC"
                        )
                )
        )
    }

    def calling(Closure methodToCall, HttpStatus expectedStatus) {
        def response = methodToCall.call() as Response
        assert response.code() == expectedStatus.value()

        if (response.successful) {
            return response.body()
        } else {
            return objectMapper.readValue(response.errorBody().string(), ErrorResponse)
        }
    }

    def generateClient(Class c) {
        new ApiClient()
                .adapterBuilder
                .baseUrl("http://localhost:" + port + "/")
                .build()
                .create(c)
    }

    def cleanup() {
        paymentRepository.deleteAll()
    }

}