package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    public String externalId;

    public String type;

    public BigDecimal version;

    public String organisationId;

    @Embedded
    public Attributes attributes = new Attributes();

    public static Payment fromDto(PaymentDto payment) {
        Payment result = new Payment();
        result.externalId = payment.getId();
        result.type = payment.getType();
        result.version = payment.getVersion();
        result.organisationId = payment.getOrganisationId();
        result.attributes = Attributes.fromDto(payment.getAttributes());
        return result;
    }
}
