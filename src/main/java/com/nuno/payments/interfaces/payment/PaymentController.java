package com.nuno.payments.interfaces.payment;

import com.nuno.payments.api.generated.controller.PaymentsApi;
import com.nuno.payments.api.generated.dto.*;
import com.nuno.payments.application.payment.PaymentService;
import com.nuno.payments.infrastructure.persistence.payment.*;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentsApi {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<IdResponse> createPayment(@ApiParam(value = "The Payment to be created", required = true) @Valid @RequestBody PaymentDto payment) {

        IdResponse response = new IdResponse();
        response.setId(paymentService.createPayment(payment));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deletePayment(@ApiParam(value = "The ID of the payment", required = true) @PathVariable("paymentId") String paymentId) {
        paymentService.deletePayment(paymentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments().stream()
                .map(this::paymentToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<PaymentDto> getPayment(@ApiParam(value = "The ID of the payment", required = true) @PathVariable("paymentId") String paymentId) {
        return new ResponseEntity<>(paymentToDto(paymentService.getPayment(paymentId)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updatePayment(@ApiParam(value = "The updated payment" ,required=true )  @Valid @RequestBody PaymentDto paymentRequest) {
        paymentService.updatePayment(paymentRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private PaymentDto paymentToDto(Payment entity) {
        PaymentDto result = new PaymentDto();
        result.setId(entity.getExternalId());
        result.setType(entity.getType());
        result.setVersion(entity.getVersion());
        result.setOrganisationId(entity.getOrganisationId());
        result.setAttributes(attributesToDto(entity.getAttributes()));
        return result;
    }

    private PaymentDtoAttributes attributesToDto(Attributes entity) {
        PaymentDtoAttributes result = new PaymentDtoAttributes();
        result.setAmount(entity.getAmount());
        result.setBeneficiaryParty(beneficiaryPartyToDto(entity.getBeneficiaryParty()));
        result.setChargesInformation(chargesInformationToDto(entity.getChargesInformation()));
        result.setCurrency(entity.getCurrency());
        result.setDebtorParty(debtorPartyToDto(entity.getDebtorParty()));
        result.setEndToEndReference(entity.getEndToEndReference());
        result.setFx(fxToDto(entity.getFx()));
        result.setNumericReference(entity.getNumericReference());
        result.setPaymentId(entity.getPaymentId());
        result.setPaymentPurpose(entity.getPaymentPurpose());
        result.setPaymentScheme(entity.getPaymentScheme());
        result.setPaymentType(entity.getPaymentType());
        result.setProcessingDate(entity.getProcessingDate());
        result.setReference(entity.getReference());
        result.setSchemePaymentSubType(entity.getSchemePaymentSubType());
        result.setSchemePaymentType(entity.getSchemePaymentType());
        result.sponsorParty(sponsorPartyToDto(entity.getSponsorParty()));
        return result;
    }

    private PaymentDtoAttributesBeneficiaryParty beneficiaryPartyToDto(BeneficiaryParty entity) {
        PaymentDtoAttributesBeneficiaryParty result = new PaymentDtoAttributesBeneficiaryParty();
        result.setAccountName(entity.getAccountName());
        result.setAccountNumber(entity.getAccountNumber());
        result.setAccountNumberCode(entity.getAccountNumberCode());
        result.setAccountType(entity.getAccountType());
        result.setAddress(entity.getAddress());
        result.setBankId(entity.getBankId());
        result.setBankIdCode(entity.getBankIdCode());
        result.setName(entity.getName());
        return result;
    }

    private PaymentDtoAttributesDebtorParty debtorPartyToDto(DebtorParty entity) {
        PaymentDtoAttributesDebtorParty result = new PaymentDtoAttributesDebtorParty();
        result.setAccountName(entity.getAccountName());
        result.setAccountNumber(entity.getAccountNumber());
        result.setAccountNumberCode(entity.getAccountNumberCode());
        result.setAddress(entity.getAddress());
        result.setBankId(entity.getBankId());
        result.setBankIdCode(entity.getBankIdCode());
        result.setName(entity.getName());
        return result;
    }

    private PaymentDtoAttributesFx fxToDto(Fx entity) {
        PaymentDtoAttributesFx result = new PaymentDtoAttributesFx();
        result.setContractReference(entity.getContractReference());
        result.setExchangeRate(entity.getExchangeRate());
        result.setOriginalAmount(entity.getOriginalAmount());
        result.setOriginalCurrency(entity.getOriginalCurrency());
        return result;
    }

    private PaymentDtoAttributesChargesInformation chargesInformationToDto(ChargesInformation entity) {
        PaymentDtoAttributesChargesInformation result = new PaymentDtoAttributesChargesInformation();
        result.setBearerCode(entity.getBearerCode());
        result.setSenderCharges(entity.getSenderCharges().stream()
                .map(this::senderChargesToDto)
                .collect(Collectors.toList()));
        result.setReceiverChargesAmount(entity.getReceiverChargesAmount());
        result.setReceiverChargesCurrency(entity.getReceiverChargesCurrency());
        return result;
    }

    private PaymentDtoAttributesChargesInformationSenderCharges senderChargesToDto(SenderCharge entity) {
        PaymentDtoAttributesChargesInformationSenderCharges result = new PaymentDtoAttributesChargesInformationSenderCharges();
        result.setAmount(entity.getAmount());
        result.setCurrency(entity.getCurrency());
        return result;
    }

    private PaymentDtoAttributesSponsorParty sponsorPartyToDto(SponsorParty entity) {
        PaymentDtoAttributesSponsorParty result = new PaymentDtoAttributesSponsorParty();
        result.setAccountNumber(entity.getAccountNumber());
        result.setBankId(entity.getBankId());
        result.setBankIdCode(entity.getBankIdCode());
        return result;
    }
}
