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
    @Autowired StoreRepository storeRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_RentalConfirm(@Payload Paid paid){

        if(!paid.validate()) return;
        Paid event = paid;
        System.out.println("\n\n##### listener RentalConfirm : " + paid.toJson() + "\n\n");

        
        

        // Sample Logic //
        Store.rentalConfirm(event);
        

        

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCancelled_RentalCancel(@Payload PayCancelled payCancelled){

        if(!payCancelled.validate()) return;
        System.out.println("\n\n##### listener RentalCancel : " + payCancelled.toJson() + "\n\n");

        PayCancelled event = payCancelled;


        

        // Sample Logic //
        Store.rentalCancel(event);
        

        

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverToyReturned_ToyReturn(@Payload ToyReturned toyReturned){

        if(!toyReturned.validate()) return;
        ToyReturned event = toyReturned;
        System.out.println("\n\n##### listener ToyReturn : " + toyReturned.toJson() + "\n\n");


        

        // Sample Logic //
        Store.toyReturn(event);
        

        

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverToyRepaired_UpdateToyStatus(@Payload ToyRepaired toyRepaired){

        if(!toyRepaired.validate()) return;
        ToyRepaired event = toyRepaired;
        System.out.println("\n\n##### listener UpdateToyStatus : " + toyRepaired.toJson() + "\n\n");


        

        // Sample Logic //
        Store.updateToyStatus(event);
        

        

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDiscarded_UpdateToyStatus(@Payload Discarded discarded){

        if(!discarded.validate()) return;
        Discarded event = discarded;
        System.out.println("\n\n##### listener UpdateToyStatus : " + discarded.toJson() + "\n\n");


        

        // Sample Logic //
        Store.updateToyStatus(event);
        

        

    }


}


