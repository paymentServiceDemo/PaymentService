package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributesDebtorParty;
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
public class DebtorParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String accountName;

    public String accountNumber;

    public String accountNumberCode;

    public String address;

    public String bankId;

    public String bankIdCode;

    public String name;

    public static DebtorParty fromDto(PaymentDtoAttributesDebtorParty dto) {
        DebtorParty result = new DebtorParty();
        result.accountName = dto.getAccountName();
        result.accountNumber = dto.getAccountNumber();
        result.accountNumberCode = dto.getAccountNumberCode();
        result.address = dto.getAddress();
        result.bankId = dto.getBankId();
        result.bankIdCode = dto.getBankIdCode();
        result.name = dto.getName();
        return result;
    }
}
