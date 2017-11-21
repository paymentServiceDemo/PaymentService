package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import static javax.persistence.CascadeType.ALL;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Attributes {

    public String amount;

    @OneToOne(cascade = ALL)
    public BeneficiaryParty beneficiaryParty = new BeneficiaryParty();

    @OneToOne(cascade = ALL)
    public ChargesInformation chargesInformation = new ChargesInformation();

    public String currency;

    @OneToOne(cascade = ALL)
    public DebtorParty debtorParty = new DebtorParty();

    public String endToEndReference;

    @OneToOne(cascade = ALL)
    public Fx fx = new Fx();

    public String numericReference;

    public String paymentId;

    public String paymentPurpose;

    public String paymentScheme;

    public String paymentType;

    public String processingDate;

    public String reference;

    public String schemePaymentSubType;

    public String schemePaymentType;

    @OneToOne(cascade = ALL)
    public SponsorParty sponsorParty;

    public static Attributes fromDto(PaymentDtoAttributes dto) {
        Attributes result = new Attributes();
        result.amount = dto.getAmount();
        result.beneficiaryParty = BeneficiaryParty.fromDto(dto.getBeneficiaryParty());
        result.chargesInformation = ChargesInformation.fromDto(dto.getChargesInformation());
        result.currency = dto.getCurrency();
        result.debtorParty = DebtorParty.fromDto(dto.getDebtorParty());
        result.endToEndReference = dto.getEndToEndReference();
        result.fx = Fx.fromDto(dto.getFx());
        result.numericReference = dto.getNumericReference();
        result.paymentId = dto.getPaymentId();
        result.paymentPurpose = dto.getPaymentPurpose();
        result.paymentScheme = dto.getPaymentScheme();
        result.paymentType = dto.getPaymentType();
        result.processingDate = dto.getProcessingDate();
        result.reference = dto.getReference();
        result.schemePaymentSubType = dto.getSchemePaymentSubType();
        result.schemePaymentType = dto.getSchemePaymentType();
        result.sponsorParty = SponsorParty.fromDto(dto.getSponsorParty());

        return result;
    }
}
