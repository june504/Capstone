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
    @Autowired
    RepairRepository repairRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRepairRequested_RequestRepair(@Payload RepairRequested repairRequested){

        if(!repairRequested.validate()) return;
        System.out.println("\n\n##### listener RequestRepair : " + repairRequested.toJson() + "\n\n");
        
        RepairRequested event = repairRequested;
        Repair.requestRepair(event);
        

        

    }


}


