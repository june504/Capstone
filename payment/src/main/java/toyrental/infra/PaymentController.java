package toyrental.infra;
import toyrental.domain.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

 @RestController
 @RequestMapping(value="/payments")
 @Transactional
 public class PaymentController {
        @Autowired
        PaymentRepository paymentRepository;

        @RequestMapping(value = "/{id}/paycancel",
         method = RequestMethod.PUT,
          produces = "application/json;charset=UTF-8")
        public Payment payCancel(@PathVariable(value = "id") Integer id, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
                System.out.println("##### /payment/payCancel  called #####");
                Optional<Payment> optionalPayment = paymentRepository.findById(id);
                
                optionalPayment.orElseThrow(()-> new Exception("No Entity Found"));
                Payment payment = optionalPayment.get();
                payment.payCancel();
                
                paymentRepository.save(payment);
                return payment;
                
        }

 }
