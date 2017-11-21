package com.nuno.payments.application.payment;

import com.nuno.payments.api.generated.dto.PaymentDto;
import com.nuno.payments.domain.model.exception.PaymentFoundException;
import com.nuno.payments.domain.model.exception.PaymentNotFoundException;
import com.nuno.payments.infrastructure.persistence.payment.Attributes;
import com.nuno.payments.infrastructure.persistence.payment.Payment;
import com.nuno.payments.infrastructure.persistence.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public String createPayment(PaymentDto payment) {
        if (paymentRepository.findOneByExternalId(payment.getId()) != null)
            throw new PaymentFoundException(payment.getId());
        return paymentRepository.save(Payment.fromDto(payment)).getExternalId();
    }

    public void deletePayment(String paymentId) {
        getExistingPayment(paymentId);
        paymentRepository.deleteByExternalId(paymentId);
    }

    private Payment getExistingPayment(String paymentId) {
        return Optional.ofNullable(paymentRepository.findOneByExternalId(paymentId))
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPayment(String paymentId) {
        return getExistingPayment(paymentId);
    }

    public void updatePayment(PaymentDto dto) {
        Payment paymentEntity = getExistingPayment(dto.getId());
        paymentEntity.type = dto.getType();
        paymentEntity.version = dto.getVersion();
        paymentEntity.organisationId = dto.getOrganisationId();
        paymentEntity.attributes = Attributes.fromDto(dto.getAttributes());
        paymentRepository.save(paymentEntity);
    }
}
