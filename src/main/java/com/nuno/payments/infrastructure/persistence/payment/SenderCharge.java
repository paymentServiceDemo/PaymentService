
package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributesChargesInformationSenderCharges;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SenderCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String amount;

    public String currency;

    public static SenderCharge toDto(PaymentDtoAttributesChargesInformationSenderCharges dto) {
        SenderCharge result = new SenderCharge();
        result.amount= dto.getAmount();
        result.currency= dto.getCurrency();
        return result;
    }
}
