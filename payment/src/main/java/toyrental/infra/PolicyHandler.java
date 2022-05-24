package toyrental.infra;

import javax.naming.NameParser;

import javax.naming.NameParser;

import toyrental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import toyrental.domain.*;


@Service
public class PolicyHandler{
    @Autowired PaymentRepository paymentRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentalCancelled_PayCancel(@Payload RentalCancelled rentalCancelled){

        if(!rentalCancelled.validate()) return;
        RentalCancelled event = rentalCancelled;
        System.out.println("\n\n##### listener PayCancel : " + rentalCancelled.toJson() + "\n\n");


        

        // Sample Logic //
        Payment.payCancel(event);
        

        

    }


}


