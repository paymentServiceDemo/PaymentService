package com.nuno.payments.application.payment

import com.nuno.payments.api.generated.dto.*
import com.nuno.payments.domain.model.exception.PaymentNotFoundException
import com.nuno.payments.infrastructure.persistence.payment.*
import spock.lang.Specification
import spock.lang.Subject

class PaymentServiceTest extends Specification {

    @Subject
    PaymentService subject

    def paymentRepository = Mock(PaymentRepository)

    void setup() {
        subject = new PaymentService(paymentRepository)
    }

    def "createPayment() - success"() {
        given: "A create Request"
        def payment = defaultPaymentDto()

        when: "the create is called"
        def result = subject.createPayment payment

        then: "Payment is saved and the Id returned"
        1 * paymentRepository.save(_) >> new Payment(externalId: '1')
        result == '1'
    }

    def "deletePayment() - success"() {
        given: "A delete request"
        def paymentId = '1'
        when: "the delete is called"
        subject.deletePayment paymentId
        then: "Payment is found and deleted"
        1 * paymentRepository.findOneByExternalId(paymentId) >> new Payment(id: 1L)
        1 * paymentRepository.deleteByExternalId(paymentId)
    }

    def "deletePayment() - Payment not found"() {
        given: "A delete request"
        def paymentId = '1'
        when: "the delete is called"
        subject.deletePayment paymentId
        then: "Payment is found and deleted"
        1 * paymentRepository.findOneByExternalId(paymentId)
        thrown PaymentNotFoundException
    }

    def "getPayment() - Success"() {
        given: "A existing Payment"
        def paymentId = '1'
        when:
        subject.getPayment paymentId
        then:
        1 * paymentRepository.findOneByExternalId(paymentId) >> defaultPaymentEntity()
    }

    def "getPayment() - Payment not found"() {
        given: "A existing Payment"
        def paymentId = '1'
        when:
        subject.getPayment paymentId
        then:
        1 * paymentRepository.findOneByExternalId(paymentId)
        thrown PaymentNotFoundException
    }

    def "getAllPayments() - success"() {
        when:
        subject.getAllPayments()
        then:
        1 * paymentRepository.findAll() >> []
    }

    def "updatePayment() - success"() {
        given: "A update Request"
        def payment = defaultPaymentDto()

        when: "the update is called"
        subject.updatePayment payment

        then: "Payment is saved and the Id returned"
        1 * paymentRepository.findOneByExternalId(_) >> defaultPaymentEntity()
        1 * paymentRepository.save(_)
    }

    private PaymentDto defaultPaymentDto() {
        new PaymentDto(
                "type": "Payment",
                "id": "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43",
                "version": 0,
                "organisationId": "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb",
                "attributes": new PaymentDtoAttributes(
                        "amount": "100.21",
                        "beneficiaryParty": new PaymentDtoAttributesBeneficiaryParty(
                                "accountName": "W Owens",
                                "accountNumber": "31926819",
                                "accountNumberCode": "BBAN",
                                "accountType": 0,
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

    private Payment defaultPaymentEntity() {
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
}
