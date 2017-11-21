package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributesFx;
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
public class Fx {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String contractReference;

    public String exchangeRate;

    public String originalAmount;

    public String originalCurrency;


    public static Fx fromDto(PaymentDtoAttributesFx dto) {
        Fx result = new Fx();
        result.contractReference = dto.getContractReference();
        result.exchangeRate = dto.getExchangeRate();
        result.originalAmount = dto.getOriginalAmount();
        result.originalCurrency = dto.getOriginalCurrency();
        return result;
    }
}
