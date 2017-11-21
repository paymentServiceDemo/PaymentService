package com.nuno.payments.interfaces.payment

import com.nuno.payments.api.generated.dto.PaymentDto
import com.nuno.payments.application.payment.PaymentService
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.OK

class PaymentControllerTest extends Specification {

    @Subject
    PaymentController subject

    def paymentService = Mock(PaymentService)

    void setup() {
        subject = new PaymentController(paymentService)
    }

    def "I can create payments"() {
        given: "A request"
        def id = '1'
        def payment = Mock PaymentDto

        when: "The create interface is called"
        def responseEntity = subject.createPayment payment

        then: "The response contains the ID and status 200 "
        1 * paymentService.createPayment(_) >> id
        responseEntity.statusCode == OK
        responseEntity.body.id == id
    }

    def "I can delete payments"() {
        given: "A request"
        def id = '1'

        when: "The delete interface is called"
        def responseEntity = subject.deletePayment id

        then: "The response is empty and status is 200"
        1 * paymentService.deletePayment(id) >> null
        responseEntity.statusCode == OK
    }

    def "I can list payments"() {
        when: "The list interface is called"
        def responseEntity = subject.getAllPayments()

        then: "The response contains a list of payments and status is 200"
        1 * paymentService.getAllPayments() >> []
        responseEntity.statusCode == OK
        responseEntity.body == []
    }

    def "I can update payments"() {
        given: "A request"
        def payment = Mock PaymentDto

        when: "The update interface is called"
        def responseEntity = subject.updatePayment payment

        then: "The response has status 200"
        1 * paymentService.updatePayment(_)
        responseEntity.statusCode == OK
    }

}
