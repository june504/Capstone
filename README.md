![image](https://user-images.githubusercontent.com/38757114/170401995-ae4bc2d2-9444-4504-a733-9ae7528f64c6.png)

# 장남감 대여(ToyRental)

본 예제는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하도록 구성한 예제입니다.
이는 클라우드 네이티브 애플리케이션의 개발에 요구되는 체크포인트들을 통과하기 위한 예시 답안을 포함합니다.
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW

# 서비스 시나리오

기능적 요구사항
1. 고객이 장난감 대여 주문을 한다.
1. 고객은 주문이후에 결제를 진행된다.
1. 주문 결제가 완료되면 상점에 전달된다.
1. 상점주인은 대여 주문을 승인한다.
1. 고객이 주문을 취소한다.
1. 주문 취소와 동시에 결제가 취소된다.
1. 주문 취소가 완료되면 상점에 전달된다.
1. 고객은 장난감을 반납한다.
1. 장난감을 반납하면 상점에 전달된다.
1. 상점 주인은 장난감의 재고 상태를 상시 조회한다.
1. 상점 주인은 재고를 등록할 수 있다.
1. 상점 주인은 손상된 장난감을 수리 요청한다.
1. 상점 주인이 반납 확인을 하면 rental 대여 상태가 변경된다.
1. 수리기사는 장난감을 수리한다.
1. 수리된 장난감 정보를 상점에 전달한다.
1. 수리기사는 장난감을 폐기한다.
1. 폐기된 장난감 정보를 상점에 전달한다.

비기능적 요구사항
1. 트랜잭션
    1. 결제가 되지 않은 주문건은 아예 거래가 성립되지 않아야 한다 (Sync 호출)
1. 장애격리
    1. 상점관리 기능이 수행되지 않더라도 주문은 365일 24시간 받을 수 있어야 한다  Async (event-driven), Eventual Consistency
    1. 결제시스템이 과중되면 사용자를 잠시동안 받지 않고 결제를 잠시후에 하도록 유도한다  Circuit breaker, fallback
1. 성능
    1. 상점주인은 장난감 상태를 Dashboard를 통해 확인할 수 있어야 한다  CQRS


# 체크포인트

- 분석 설계


  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
    
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계


## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/38757114/170372267-17e61bf9-ece2-4488-a319-70fc2750a324.png)

## TO-BE 조직 (Vertically-Aligned)
  ![image](https://user-images.githubusercontent.com/38757114/170242318-89cc85d2-260c-426b-8152-57f464ac97a5.png)


## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과:  https://labs.msaez.io/#/storming/3jGlydfghQMxDDNLKx6tC2Jw4jn2/c275e9a22f064522da75bc037464d589


### 이벤트 도출
![image](https://user-images.githubusercontent.com/38757114/170240973-4b66e331-e3cf-46c2-be36-1eed891336d6.png)

### 부적격 이벤트 탈락
![image](https://user-images.githubusercontent.com/38757114/170241327-8a14f857-7d71-4f4e-933a-e5551a9b153d.png)

    - 과정중 도출된 잘못된 도메인 이벤트들을 걸러내는 작업을 수행함
        - Off-Line 매장에서 대여와 반납하는 서비스로 장난감 배달 이벤트는 제외

### 액터, 커맨드 부착하여 읽기 좋게
![image](https://user-images.githubusercontent.com/38757114/170247769-aaf5b1a4-a2ce-4749-822d-59316545be8a.png)

### 어그리게잇으로 묶기
![image](https://user-images.githubusercontent.com/38757114/170247909-0306932c-759c-4c4c-a908-6ffca90f9b78.png)

    - rental의 주문, store의 주문처리, repair의 처리 결과, payment 결제이력은 그와 연결된 command 와 event 들에 의하여 트랜잭션이 유지되어야 하는 단위로 그들 끼리 묶어줌

### 바운디드 컨텍스트로 묶기

![image](https://user-images.githubusercontent.com/38757114/170248007-2c86dd0d-fb2d-4b7c-bd39-d4180167a6cd.png)

    - 도메인 서열 분리 
        - Core Domain (rental, store) : 없어서는 안될 핵심 서비스이며, 연견 Up-time SLA 수준을 99.999% 목표, 배포주기는 app 의 경우 1주일 1회 미만, store 의 경우 1개월 1회 미만
        - Supporting Domain (repair) : 경쟁력을 내기위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 각 팀의 자율이나 표준 스프린트 주기가 1주일 이므로 1주일 1회 이상을 기준으로 함.
        - General Domain (payment) : 결제서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음

### 폴리시 부착 (괄호는 수행주체, 폴리시 부착을 둘째단계에서 해놔도 상관 없음. 전체 연계가 초기에 드러남)

![image](https://user-images.githubusercontent.com/38757114/170248122-5094f44f-fe8a-4fde-8800-8e29d88ee5b3.png)

### 폴리시의 이동과 컨텍스트 매핑 (점선은 Pub/Sub, 실선은 Req/Resp)

![image](https://user-images.githubusercontent.com/38757114/170248193-d5e764ee-0ef4-48ba-b694-848c44110f7b.png)

### 완성된 1차 모형

![image](https://user-images.githubusercontent.com/38757114/170248397-8b9b95f5-5554-4a2c-8412-2ef513fcc595.png)

    - View Model 추가

### 1차 완성본에 대한 기능적/비기능적 요구사항을 커버하는지 검증

![image](https://user-images.githubusercontent.com/38757114/170251183-72269cdd-41d6-4cdf-a3cc-b2f7b3f979dc.png)

    - 고객이 장난감 대여 주문을 한다. (ok)
    - 고객은 주문이후에 결제를 진행된다. (ok)
    - 주문 결제가 완료되면 상점에 전달된다. (ok)
    - 상점주인은 대여 주문을 승인한다. (ok)
    - 고객이 주문을 취소한다. (ok)
    - 주문 취소와 동시에 결제가 취소된다. (ok)
    - 주문 취소가 완료되면 상점에 전달된다. (ok) 
    - 고객은 장난감을 반납한다. (ok)
    - 장난감을 반납하면 상점에 전달된다. (ok)
    - 상점 주인은 장난감의 재고 상태를 상시 조회한다.

![image](https://user-images.githubusercontent.com/38757114/170254408-d329f749-29a9-4fd0-92cd-246f07e961f8.png)

    - 상점 주인은 재고를 등록할 수 있다. (ok)
    - 상점 주인은 손상된 장난감을 수리 요청한다. (ok)
    - 상점 주인이 반납 확인을 하면 rental 대여 상태가 변경된다.(?)

![image](https://user-images.githubusercontent.com/38757114/170254646-895c53e0-98b8-4496-b629-fa4a626ff896.png)

    - 수리기사는 장난감을 수리한다. (ok)
    - 수리된 장난감 정보를 상점에 전달한다. (ok)
    - 수리기사는 장난감을 폐기한다. (ok)
    - 폐기된 장난감 정보를 상점에 전달한다. (ok)


### 모델 수정

![image](https://user-images.githubusercontent.com/38757114/170369996-a8c0068d-2afa-4da7-b1e0-8d4436abb8be.png)
    
    - 수정된 모델은 모든 요구사항을 커버함...

### 비기능 요구사항에 대한 검증

    - 트랜잭션
        - 결제가 되지 않은 주문건은 아예 거래가 성립되지 않아야 한다 (Sync 호출) (ok)
    - 장애격리
        - 상점관리 기능이 수행되지 않더라도 주문은 365일 24시간 받을 수 있어야 한다  Async (event-driven), Eventual Consistency (ok)
        - 결제시스템이 과중되면 사용자를 잠시동안 받지 않고 결제를 잠시후에 하도록 유도한다  Circuit breaker, fallback (ok)
    - 성능
        - 상점주인은 장난감 상태를 Dashboard를 통해 확인할 수 있어야 한다  CQRS (ok)


## 헥사고날 아키텍처 다이어그램 도출
    
![image](https://user-images.githubusercontent.com/38757114/170381349-d213db00-5a31-4c98-974d-8b76ba725027.png)


    - Chris Richardson, MSA Patterns 참고하여 Inbound adaptor와 Outbound adaptor를 구분함
    - 호출관계에서 PubSub 과 Req/Resp 를 구분함
    - 서브 도메인과 바운디드 컨텍스트의 분리:  각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트와 파이선으로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
    cd store
    mvn spring-boot:run

    cd repair
    mvn spring-boot:run  

    cd rental
    mvn spring-boot:run  
    
    cd payment
    mvn spring-boot:run  
    
    cd view
    mvn spring-boot:run 

```


## SAGA

5개의 마이크로 서비스간 통신에서 kafka를 활용해 이벤트 메세지를 Pub/Sub하는 방법으로 구현하였다.

* ex) 장난감 상점에서 수리가 필요한 장난감이 있을 경우, RepairRequested 이벤트를 발생

* Store.java
```java
    public void repairRequest(){
        setToyStatus("UNDER_REPAIR");
        RepairRequested repairRequested = new RepairRequested();
        BeanUtils.copyProperties(this, repairRequested);
        repairRequested.publishAfterCommit();
    }

```


Kafka에는 아래와 같이 이벤트가 publish된 것을 확인할 수 있다.
```
{"eventType":"RepairRequested","timestamp":1653526587264,"toyId":3,"name":"타요버스","toyStatus":"UNDER_REPAIR"}
```



## CQRS

장난감의 실시간 상태와 렌탈,수리 아이디를 상점 관리자가 통합 조회할 수 있도록 CQRS로 구현하였다.

store, repair, rental 3개의 마이크로 서비스를 통합 조회해 성능 이슈를 사전에 예방할 수 있다.

![image1](https://user-images.githubusercontent.com/103399148/170413278-df17b29e-50f4-4be9-8aa9-5cb57d2ca400.png)


1. store 마이크로서비스 예시) 장난감이 등록되면, Registred 이벤트를 수신하여 등록
```java
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_CREATE_1 (@Payload Registered registered) {
        try {

            if (!registered.validate()) return;
           
            ToyList toyList = new ToyList();

            toyList.setToyId(registered.getToyId());
            toyList.setName(registered.getName());
            toyList.setToyRentalPrice(registered.getToyRentalPrice());
            toyList.setToyStatus("AVAILABLE");
 
            toyListRepository.save(toyList);

        }catch (Exception e){
           e.printStackTrace();
        }
    }
```

2. repair 마이크로서비스 예시) 수리불가로 폐기되는 경우, Discarded 이벤트를 수신하여 변경
```java
   @StreamListener(KafkaProcessor.INPUT)
    public void whenDiscarded_then_UPDATE_2(@Payload Discarded toyRepaired) {
        try {
            if (!toyRepaired.validate()) return;
            
            Optional<ToyList> toyListOptional = toyListRepository.findById(toyRepaired.getToyId());

            if( toyListOptional.isPresent()) {
                 ToyList toyList = toyListOptional.get();
         
                 toyList.setToyStatus("DISCARDED");
                
                 toyListRepository.save(toyList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

3. rental 마이크로서비스 예시) 장남감 대여 시, ToyRented 이벤트를 수신하여 변경
```java
    @StreamListener(KafkaProcessor.INPUT)
    public void whenToyRented_then_DELETE_1(@Payload ToyRented event) {
        try {
            if (!event.validate()) return;

            Optional<ToyList> toyListOptional = toyListRepository.findById(event.getToyId());

            if( toyListOptional.isPresent()) {

								ToyList toyList = toyListOptional.get();
    
                toyList.setToyStatus("RENT REQUEST");
                toyList.setRentalId(event.getRentalId());
            
                toyListRepository.save(toyList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```   


- CQRS 테스트 내용
```
장난감 생성
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="앵그리버드 인형" toyRentalPrice=4000 toyStatus="AVAILABLE"
```
![image2](https://user-images.githubusercontent.com/103399148/170416676-a307971c-be3d-4da6-b53b-b008f724841f.png)

```
CQRS에서 조회
http http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/toyLists
```
![image3](https://user-images.githubusercontent.com/103399148/170416650-d0197ade-9300-4f89-9bf2-33b7cdedc51f.png)

```
장난감 렌트
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals rentalStatus="rent" customerId=1 toyId=11
```
![image4](https://user-images.githubusercontent.com/103399148/170416652-7c624f9b-a2c4-4586-906b-dc674f2546e0.png)

```
CQRS에서 조회
http http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/toyLists
```
![image5](https://user-images.githubusercontent.com/103399148/170416653-5a3a9369-345a-4f04-af2f-390c18ca11f9.png)

```
장난감 폐기
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/repairs/8/discard
```
![image6](https://user-images.githubusercontent.com/103399148/170416656-82bcdf59-14e6-4ee2-9257-d5614150fda8.png)

```
CQRS에서 조회
http http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/toyLists
```
![image7](https://user-images.githubusercontent.com/103399148/170416659-8bef6445-5874-452a-911e-292366d13327.png)



## Correlation  
 Kafka에 publish되어있는 이벤트를 각기 다른 microservice에서 받아 처리할 수 있도록 하였다.

 ex) 고객이 장난감 대여취소를 했을 때 결제취소가 되고, 결제취소이벤트를 받아 대여취소가 진행된다.  

- 대여취소 이벤트 발생
```json
{"eventType":"RentalCancelled","timestamp":1653531080684,"rentalId":2,"customerId":1,"toyId":2,"rentalStatus":"cancel"}
```

 * PolicyHandler.java (Payment)

```java
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentalCancelled_PayCancel(@Payload RentalCancelled rentalCancelled){
        if(!rentalCancelled.validate()) return;
        RentalCancelled event = rentalCancelled;
        System.out.println("\n\n##### listener PayCancel : " + rentalCancelled.toJson() + "\n\n");
        Payment.payCancel(event);
    }
```  
* Payment.java (Payment)
```java
    public static void payCancel(RentalCancelled rentalCancelled){
        try{
            Integer rentalId = rentalCancelled.getRentalId();
            Optional<Payment> optionalRental = repository().findByRentalId(rentalId);
            optionalRental.orElseThrow(()-> new Exception("No Entity Found"));
            Payment payment = optionalRental.get();
            // 상태를 저장한다.
            payment.setPayStatus("cencel");
            repository().save(payment);
            
            // 이벤트를 발생시킨다.
            PayCancelled payCancelled = new PayCancelled();
            payCancelled.setPayId(payment.getPayId());
            payCancelled.setToyRentalPrice(payment.getToyRentalPrice());
            payCancelled.setRentalId(rentalCancelled.getRentalId());
            payCancelled.setPayStatus("cencel");

            payCancelled.publish();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
```
- 결제취소 이벤트 발생
```json
{"eventType":"PayCancelled","timestamp":1653531080759,"payId":1,"rentalId":2,"payStatus":"cencel","toyRentalPrice":2000}
```

* PolicyHandler.java (Store)
```java
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCancelled_RentalCancel(@Payload PayCancelled payCancelled){
        if(!payCancelled.validate()) return;
        System.out.println("\n\n##### listener RentalCancel : " + payCancelled.toJson() + "\n\n");
        PayCancelled event = payCancelled;
        Store.rentalCancel(event);
    }
```

* Store.java (Store)
```java
    public static void rentalCancel(PayCancelled payCancelled){
        try{
            Integer rentalId = payCancelled.getRentalId();
            Optional<Store> optionalStore = repository().findByRentalId(rentalId);
            optionalStore.orElseThrow(()-> new Exception("No Entity Found"));

            Store store = optionalStore.get();
            store.setToyStatus("AVAILABLE");
            repository().save(store);
        }  catch (Exception e){
            throw new RuntimeException(e);
        }
    }
```

```json
{
      "rentalId" : null,
      "name" : "팽이",
      "toyRentalPrice" : 4000,
      "toyStatus" : "AVAILABLE",
      "_links" : {
        "self" : {
          "href" : "http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/8"
        },
        "store" : {
          "href" : "http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/8"
        },
        "repairrequest" : {
          "href" : "http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/8/repairrequest"
        },
        "accept" : {
          "href" : "http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/8/accept"
        },
        "returnconfirm" : {
          "href" : "http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/8/returnconfirm"
        }
      }
    }
```


- Correlation 테스트 내용 추가
```
1. 장난감을 등록한다.
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="피카츄 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="라이츄 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="타요버스 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"

2. 장난감을 대여신청한다.
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals rentalStatus="rent" customerId=1 toyId=1 

3. 가게에서 대여확정한다.
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/1/accept

4. 장난감을 대여취소한다.
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals/1/rentalcancel

5. 대여취소가 반영됨을 확인한다.
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores

```

## Request / Response
 
 대여(toyRented) -> 결제(Pay) 간의 호출은 동기식 일관성을 유지하는 트랙잭션으로 처리하도록 구현했다.

- 결제서비스(payment)를 호출하기 위해 Proxy를 구현
```java
    @FeignClient(name="payment", url="${api.path.payment}")
    public interface PaymentService {
        @RequestMapping(method= RequestMethod.POST, path="/payments")
        public void pay(@RequestBody Payment payment);

    }
```

- 장난감 대여 직후 결제를 요청하도록 처리 
```java
    public void toyRental(){
        
        Payment payment = new Payment();
	payment.setRentalId(this.rentalId);
	payment.setToyId(this.toyId);
	payment.setToyRentalPrice(this.toyRentalPrice);

        PaymentService paymentService =  RentalApplication.applicationContext.
	getBean(toyrental.external.PaymentService.class);
	paymentService.pay(payment);   
    }
```

- 결제 후 결제승인 되었다는 이벤트를 Publish한다.
```java
    @PostPersist
    public void onPostPersist(){
        Paid paid = new Paid();
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();

    }
```    

- 결제 완료 후 상점에서 결제완료 이벤트(Paid)를 수신받아 장난감 대여를 확인한다.
```java
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_RentalConfirm(@Payload Paid paid){
        if(!paid.validate()) return;
        Paid event = paid;
        System.out.println("\n\n##### listener RentalConfirm : " + paid.toJson() + "\n\n");        // Sample Logic //
        Store.rentalConfirm(event);
    }
```

- Request / Response 테스트 내용 추가
```
장난감 대여
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals rentalStatus="rent" customerId=1 toyId=1 
```


# 운영

## Istio Ingress Gateway 를 통한 진입점 통일
- istio Circuit breaker 를 사용하기로 결정하여 gateway 도 istio ingress 를 사용
- default namespace 에 **istio-injection=enabled** 설정 후 다음과 같이 virtual service 구성

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: toyrental-gateway
spec:
  selector:
    istio: ingressgateway # use istio default controller
  servers:
  - port:
      number: 80
      name: http
      protocol: HTTP
    hosts:
    - "*"
---
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: toyrental-gateway
spec:
  hosts:
  - "*"
  gateways:
  - toyrental-gateway
  http:
  - match:
    - uri:
        prefix: /rentals
    route:
    - destination:
        host: rental
        port:
          number: 8080
  - match:
    - uri:
        prefix: /stores
    route:
    - destination:
        host: store
        port:
          number: 8080
  - match:
    - uri:
        prefix: /repairs
    route:
    - destination:
        host: repair
        port:
          number: 8080
  - match:
    - uri:
        prefix: /payments
    route:
    - destination:
        host: payment
        port:
          number: 8080
  - match:
    - uri:
        prefix: /toyLists
    route:
    - destination:
        host: view
        port:
          number: 8080
```

- istio-ingressgateway SVC ELB DNS 확인 후 서비스 정상 접근 확인 완료
    - [http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores](http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores)
    - [http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/repairs](http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/repairs)
    - [http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/payments](http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/payments)
    - [http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/toyLists](http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/toyLists)


## CI/CD 설정

- github
    - 소스 형상 관리 및 ArgoCD Deploy yaml 관리
    - repository 내에 서비스 별로 path 구성
    - https://github.com/archilee/Capstone/
![Untitled](https://user-images.githubusercontent.com/16043281/170403328-fb3517f3-8b45-4800-905f-bcbc326c177c.png)
    

- ECR
    - Container Image Registry - EKS 와 동일 region 에 각 서비스 별로 repository 구성
![Untitled 1](https://user-images.githubusercontent.com/16043281/170403228-1172dc84-f639-420f-a26f-b963a446f657.png)

- ArgoCD
    - 서비스 별로 app 구성
![Untitled 2](https://user-images.githubusercontent.com/16043281/170403252-4aa09516-a736-42cb-b1f2-b6891d9ba59f.png)

    - github path 별로 yaml 파일 sync
    - 개발 사항 변경에 대한 빠른 재배포를 위해 auto-sync - self heal 구성
    - docker build → docker push → deployment 삭제 → 자동 재배포
![Untitled 3](https://user-images.githubusercontent.com/16043281/170403290-2cdd9c7f-312e-46c0-8194-93519299078b.png)



## 동기식 호출 / 서킷 브레이킹 / 장애격리

* 서킷 브레이킹 프레임워크의 선택: Spring FeignClient + Istio(Circuit Breaker) 사용하여 구현함

시나리오는 store-->payment 연결을 RESTful Request/Response 로 연동하여 구현이 되어있고, 결제 요청이 과도할 경우 CB 를 통하여 장애격리.

- Istio(Circuit Breaker) 설정:  1번이라도 서버 오류가 발생 시 CB 회로가 닫히도록 (요청을 빠르게 실패처리, 차단) 설정
```yaml
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: dr-store   ## dr-rental, dr-repair, dr-payment, dr-view 동일
spec:
  host: store   ## store, repair, payment, view 동일
  trafficPolicy:
    loadBalancer:
      simple: ROUND_ROBIN
    outlierDetection:
      consecutive5xxErrors: 1  ## 1번이라도 서버 오류가 발생 시
      interval: 1s  ## 라우팅 대상 컨테이너 목록에서 1초단위로 분석
      baseEjectionTime: 3m  ## 3분동안 라우팅에서 제외
      maxEjectionPercent: 100  ## 모든 컨테이너가 제외될 수 있음
```

- siege 부하 테스트
```bash
kubectl exec -it siege -- siege -c50 -t60S -v --content-type "application/json" 'http://store:8080/stores POST {"toyStatus": "AVAILABLE", "toyRentalPrice":2000, "name": "콩순이 인형"}'

```

- 결과 확인 - kiali 에서 'Has Circuit Breaker' 배지 확인 완료
<img src = "https://user-images.githubusercontent.com/16043281/170430325-db5c7dcb-73ba-45da-b123-258f68404419.png" width="80%" height="80%">

## 오토스케일 아웃
앞서 CB 는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다. 


- rental 서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 80프로를 넘어서면 replica 를 3개까지 늘려준다.
- metrics-server 설치 (EKS 기본 미설치, CPU 사용량 알 수 없음)
```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

- deployment - CPU request 설정 (200m)
```yaml
## 모든 deployment 에 적용
## root@labs-195116231:/home/project/capstone-team/Capstone/store/kubernetes/deployment.yaml 

      containers:
        - name: store
          image: 979050235289.dkr.ecr.ap-southeast-2.amazonaws.com/store:latest
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: "200m"
```

- autoscale (HPA) 리소스 생성 (min 1 - max 3)
```yaml
apiVersion: autoscaling/v1
kind: HorizontalPodAutoscaler
metadata:
  name: store   ## rental, payment, repair, view
spec:
  maxReplicas: 3
  minReplicas: 1
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: store   ## rental, payment, repair, view
  targetCPUUtilizationPercentage: 80
```

- HPA 확인
```bash
root@labs-195116231:/home/project/capstone-team/Capstone# k get hpa

NAME      REFERENCE            TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
payment   Deployment/payment   2%/80%    1         3         1          20h
rental    Deployment/rental    3%/80%    1         3         1          18h
repair    Deployment/repair    2%/80%    1         3         1          20h
store     Deployment/store     3%/80%    1         3         1          20h
view      Deployment/view      3%/80%    1         3         1          20h

```

- Autoscale 테스트
    - Istio Ingress 로 직접 테스트하려했으나 AWS ELB 의 surge queue 등의 보호 처리로 인해 정상적인 테스트 불가능하여 클러스터 내부에서 테스트 수행하는 것으로 변경

```bash
# kubectl create deploy siege --image=apexacme/siege-nginx
# kubectl exec -it siege -- siege -c50 http://view:8080/toyLists
Defaulting container name to siege.
Use 'kubectl describe pod/siege -n default' to see all of the containers in this pod.
** SIEGE 4.0.4
** Preparing 50 concurrent users for battle.
The server is now under siege...^C
Lifting the server siege...
Transactions:                  19894 hits
Availability:                 100.00 %
Elapsed time:                  55.86 secs
Data transferred:              10.18 MB
Response time:                  0.14 secs
Transaction rate:             356.14 trans/sec
Throughput:                     0.18 MB/sec
Concurrency:                   49.58
Successful transactions:       19894
Failed transactions:               0
Longest transaction:            4.55
Shortest transaction:           0.00
```

    - 결과 - 정상적으로 POD 이 3개까지 늘어났다.
```bash
# k get hpa view -w
NAME   REFERENCE         TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
view   Deployment/view   82%/80%   1         3         1          24h
view   Deployment/view   656%/80%   1         3         1          24h
view   Deployment/view   464%/80%   1         3         3          24h

# k get po -l app=view
NAME                    READY   STATUS    RESTARTS   AGE
view-8487789c88-p9k5s   2/2     Running   0          127m
view-8487789c88-r6ts2   2/2     Running   0          31s
view-8487789c88-sxwq5   2/2     Running   0          31s
```

## 무정지 재배포

- readinessProbe 설정
```
      containers:
	  # ....
          # 생략
          # ....
          readinessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10

```

- 무정지 재배포와 셀프힐링 테스트를 위해 HPA 와 istio DR (destination rule),
- ArgoCD auto-sync 를 제거
```bash
# k delete hpa view
horizontalpodautoscaler.autoscaling "view" deleted

# k delete dr dr-view
destinationrule.networking.istio.io "dr-view" deleted
```

- 동일 이미지의 태그를 변경하여 push
```bash
root@labs-195116231:/home/project/capstone-team/Capstone/view# docker build -t 979050235289.dkr.ecr.ap-southeast-2.amazonaws.com/view:v1 .

root@labs-195116231:/home/project/capstone-team/Capstone/view# docker push 979050235289.dkr.ecr.ap-southeast-2.amazonaws.com/view:v1
```

- siege 로 상태 모니터링
```bash
kubectl exec -it siege -- siege -c100 -t60S -r10 -v http://view:8080/toyLists
```

- deployment/view EDIT
```bash
# k edit deploy/view

        containers:
        - name: view
          image: 979050235289.dkr.ecr.ap-southeast-2.amazonaws.com/view:latest --> v1
```

- 결과 - POD 변경되었으나 100% Availability 확인
```bash
## POD 변경 됨
# k get po -l app=view
view-5488bfc7cd-bgnn2      2/2     Running       0          26s
view-8487789c88-p9k5s      2/2     Terminating   0          152m

## siege 결과
HTTP/1.1 200     0.47 secs:     196 bytes ==> GET  /toyLists
HTTP/1.1 200     0.31 secs:     196 bytes ==> GET  /toyLists
HTTP/1.1 200     0.57 secs:     196 bytes ==> GET  /toyLists
HTTP/1.1 200     0.64 secs:     196 bytes ==> GET  /toyLists

Lifting the server siege...
Transactions:                  16034 hits
Availability:                 100.00 %
Elapsed time:                  59.17 secs
Data transferred:              12.13 MB
Response time:                  0.20 secs
Transaction rate:             270.98 trans/sec
Throughput:                     0.20 MB/sec
Concurrency:                   55.24
Successful transactions:       16044
Failed transactions:               0
Longest transaction:            3.02
Shortest transaction:           0.00
```


## Self healing (Liveness Probe)

- livenessProbe 설정
```
       containers:
					# ....
          # 생략
          # ....
          livenessProbe:
            httpGet:
              path: '/actuator/health'
              port: 8080
            initialDelaySeconds: 120
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 5

```

- 강제로 liveness probe 의 http check uri 를 수정하여 POD restart 확인
- deployment/view EDIT - test 를 위해 threshold 와 internal 을 짧게 변경

```yaml
# k edit deploy/view

        livenessProbe:
          failureThreshold: 5 --> 1
          httpGet:
            path: /actuator/health  --> /actuator/sick  ## health check fail 강제 발생
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 120 --> 60
          periodSeconds: 5 --> 2
          successThreshold: 1
          timeoutSeconds: 2
```

- 결과 확인 - describe 로 failure 확인되고 pod 조회시  재기동을 반복함
```bash
# k describe deploy view
... 생략 ....
Liveness:     http-get http://:8080/actuator/sick delay=60s timeout=2s period=2s #success=1 #failure=1

# k get po -l app=view -w
NAME                    READY   STATUS        RESTARTS   AGE
view-7f8857c48b-9twkj   2/2     Terminating   4          10m
view-dcd94d79b-rbmnr    2/2     Running       0          27s
view-7f8857c48b-9twkj   0/2     Terminating   4          10m
view-7f8857c48b-9twkj   0/2     Terminating   4          10m
view-7f8857c48b-9twkj   0/2     Terminating   4          10m
view-dcd94d79b-rbmnr    1/2     Running       1          63s
view-dcd94d79b-rbmnr    2/2     Running       1          80s
view-dcd94d79b-rbmnr    1/2     Running       2          2m3s
view-dcd94d79b-rbmnr    2/2     Running       2          2m20s
view-dcd94d79b-rbmnr    1/2     Running       3          3m5s
```

- 테스트 완료 후 복구
    - ArgoCD auto-sync 활성화

# 시연 시나리오
1. 각 MicroService 확인
```bash
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/repairs
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/payments
```

2. 장난감 등록
```bash
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="콩순이 인형" toyRentalPrice=2000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="크롱 인형" toyRentalPrice=4000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="루피 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="피카츄 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="라이츄 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores name="타요버스 인형" toyRentalPrice=5000 toyStatus="AVAILABLE"
```
	* CQRS 조회 (등록상태 확인가능)
	http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/toyLists

3. 장난감 대여
3-1. 장난감 대여 요청(pay서비스 req, res)
```bash
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals rentalStatus="rent" customerId=1 toyId=1 
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals rentalStatus="rent" customerId=1 toyId=2
```
	* CQRS 조회 (대여상태 확인가능)

3-2. 존재하지 않는 toyId 나 "AVAILABLE" 아닌 장난감으로 렌탈하려고 하면 
      404 오류 뱉음
```bash
http POST http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals rentalStatus="rent" customerId=1 toyId=1111111111
```

3-3. 가게의 대여확정
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/1/accept
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/2/accept
```
	* CQRS 조회 (대여확정 확인가능)

3-4. 고객의 대여내역 확인
```bash
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals/1
http GET http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals/2
```

4. 장난감 대여 취소
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals/1/rentalcancel
```
	* CQRS 조회 (대여취소 확인가능)
	
5. 장난감 반납
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/rentals/2/return
```
	* CQRS 조회 (반납 확인가능)

5.1 가게의 반납확인
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/2/returnconfirm
```

6. 장난감 수리요청
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/3/repairrequest
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/4/repairrequest
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/stores/5/repairrequest
```
	* CQRS 조회 (수리요청 확인가능)

6.1 장난감 수리
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/repairs/1/repair
```
	* CQRS 조회 (수리완료 확인가능)
	
6.2. 장난감 폐기
```bash
http PUT http://a51ce9ed0e17049e995a7719fed18a95-1021919797.ap-southeast-2.elb.amazonaws.com/repairs/2/discard
```
	* CQRS 조회 (폐기 확인가능)

