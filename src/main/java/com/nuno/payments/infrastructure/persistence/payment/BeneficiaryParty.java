package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributesBeneficiaryParty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class BeneficiaryParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String accountName;
    public String accountNumber;
    public String accountNumberCode;
    public BigDecimal accountType;
    public String address;
    public String bankId;
    public String bankIdCode;
    public String name;

    public static BeneficiaryParty fromDto(PaymentDtoAttributesBeneficiaryParty dto) {
        BeneficiaryParty result = new BeneficiaryParty();
        result.accountName = dto.getAccountName();
        result.accountNumber = dto.getAccountNumber();
        result.accountNumberCode = dto.getAccountNumberCode();
        result.accountType = dto.getAccountType();
        result.address = dto.getAddress();
        result.bankId = dto.getBankId();
        result.bankIdCode = dto.getBankIdCode();
        result.name = dto.getName();
        return result;
    }
}
