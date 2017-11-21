package com.nuno.payments.infrastructure.persistence.payment;

import com.nuno.payments.api.generated.dto.PaymentDtoAttributesChargesInformation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChargesInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String bearerCode;

    @OneToMany(cascade = ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("id ASC")
    public List<SenderCharge> senderCharges = new ArrayList<>();

    public String receiverChargesAmount;

    public String receiverChargesCurrency;

    public static ChargesInformation fromDto(PaymentDtoAttributesChargesInformation dto) {
        ChargesInformation result = new ChargesInformation();
        result.bearerCode = dto.getBearerCode();
        result.receiverChargesAmount = dto.getReceiverChargesAmount();
        result.receiverChargesCurrency = dto.getReceiverChargesCurrency();
        result.senderCharges = dto.getSenderCharges().stream()
                .map(SenderCharge::toDto)
                .collect(Collectors.toList());
        return result;
    }


}
