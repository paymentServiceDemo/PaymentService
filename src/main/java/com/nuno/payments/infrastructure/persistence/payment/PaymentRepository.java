package com.nuno.payments.infrastructure.persistence.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findOneByExternalId(String paymentId);

    void deleteByExternalId(String paymentId);
}
