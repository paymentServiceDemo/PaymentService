
package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributesSponsorParty;
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
public class SponsorParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String accountNumber;

    public String bankId;

    public String bankIdCode;

    public static SponsorParty fromDto(PaymentDtoAttributesSponsorParty dto) {
        SponsorParty result = new SponsorParty();
        result.accountNumber = dto.getAccountNumber();
        result.bankId = dto.getBankId();
        result.bankIdCode = dto.getBankIdCode();
        return result;
    }
}
