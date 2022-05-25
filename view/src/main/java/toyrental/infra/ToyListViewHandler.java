package toyrental.infra;

import toyrental.domain.*;
import toyrental.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ToyListViewHandler {


    @Autowired
    private ToyListRepository toyListRepository;


    //repaire 이벤트 
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRepairRequested_then_UPDATE_1(@Payload RepairRequested repairRequested) {
        try {
            if (!repairRequested.validate()) return;
                // view 객체 조회
            Optional<ToyList> toyListOptional = toyListRepository.findById(repairRequested.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 toyList.setToyStatus("UNDER_REPAIR");
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenToyRepaired_then_UPDATE_2(@Payload ToyRepaired toyRepaired) {
        try {
            if (!toyRepaired.validate()) return;
                // view 객체 조회
            Optional<ToyList> toyListOptional = toyListRepository.findById(toyRepaired.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                 toyList.setToyStatus("AVAILABLE");
                 
                 //이 부분 이상함.....
                 toyList.setRepairId(toyRepaired.getRepairId());
                 toyList.setRepairmanId(toyRepaired.getRepairmanId());
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDiscarded_then_UPDATE_2(@Payload Discarded toyRepaired) {
        try {
            if (!toyRepaired.validate()) return;
                // view 객체 조회
            Optional<ToyList> toyListOptional = toyListRepository.findById(toyRepaired.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 toyList.setToyStatus("DISCARDED");
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Store쪽 이벤트 
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_CREATE_1 (@Payload Registered registered) {
        try {

            if (!registered.validate()) return;

            // view 객체 생성
            ToyList toyList = new ToyList();
            // view 객체에 이벤트의 Value 를 set 함
            toyList.setToyId(registered.getToyId());
            toyList.setName(registered.getName());
            toyList.setToyRentalPrice(registered.getToyRentalPrice());
            toyList.setToyStatus("AVAILABLE");
            // view 레파지 토리에 save
            toyListRepository.save(toyList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAccepted_then_UPDATE_2(@Payload Accepted accepted) {
        try {
            if (!accepted.validate()) return;
                // view 객체 조회
            Optional<ToyList> toyListOptional = toyListRepository.findById(accepted.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                toyList.setToyStatus("ACCEPTED");
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReturnConfirmed_then_UPDATE_2(@Payload ReturnConfirmed event) {
        try {
            if (!event.validate()) return;
                // view 객체 조회
            Optional<ToyList> toyListOptional = toyListRepository.findById(event.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                toyList.setToyStatus("AVAILABLE");
                toyList.setRentalId(null);
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }








    //rental 쪽 이벤트 처리
    @StreamListener(KafkaProcessor.INPUT)
    public void whenToyReturned_then_DELETE_1(@Payload ToyReturned toyReturned) {
        try {
            if (!toyReturned.validate()) return;

            Optional<ToyList> toyListOptional = toyListRepository.findById(toyReturned.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                toyList.setToyStatus("RETURN REQUEST");
                //toyList.setRentalId("");
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenToyRented_then_DELETE_1(@Payload ToyRented event) {
        try {
            if (!event.validate()) return;

            Optional<ToyList> toyListOptional = toyListRepository.findById(event.getToyId());

            if( toyListOptional.isPresent()) {


                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                toyList.setToyStatus("RENT REQUEST");
                toyList.setRentalId(event.getRentalId());
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenToyRentalCancelled_then_DELETE_1(@Payload RentalCancelled event) {
        try {
            if (!event.validate()) return;

            Optional<ToyList> toyListOptional = toyListRepository.findById(event.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                toyList.setToyStatus("AVAILABLE");
                toyList.setRentalId(null);
                // view 레파지 토리에 save
                 toyListRepository.save(toyList);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

