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

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_CREATE_1 (@Payload Registered registered) {
        try {

            if (!registered.validate()) return;

            // view 객체 생성
            ToyList toyList = new ToyList();
            // view 객체에 이벤트의 Value 를 set 함
            toyList.setId(registered.getId());
            toyList.setName(registered.getName());
            // view 레파지 토리에 save
            toyListRepository.save(toyList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

