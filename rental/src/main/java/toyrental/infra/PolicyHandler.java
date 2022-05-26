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
    @Autowired RentalRepository rentalRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverAccepted_UpdateRentalStatus(@Payload Accepted accepted){

        if(!accepted.validate()) return;
        Accepted event = accepted;
        System.out.println("\n\n##### listener UpdateRentalStatus : " + accepted.toJson() + "\n\n");


        

        // Sample Logic //
        Rental.updateRentalStatus1(event);      
     

    }

   
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReturnConfirmed_UpdateRentalStatus(@Payload ReturnConfirmed returnConfirmed){

        if(!returnConfirmed.validate()) return;
        ReturnConfirmed event = returnConfirmed;
        System.out.println("\n\n##### listener UpdateRentalStatus : " + returnConfirmed.toJson() + "\n\n");


        

        // Sample Logic //
        Rental.updateRentalStatus2(event);
               

    }

}


