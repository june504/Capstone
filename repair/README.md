# repair

## Running in local development environment

```
mvn spring-boot:run


```
## TESTING

0. 수리목록 확인
http localhost:8082/repairs

1. 장난감 수리
http PUT localhost:8082/repairs/1/repair

2. 장난감 폐기
http PUT localhost:8082/repairs/1/discard


### kafka 이벤트큐 확인
/usr/local/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic toyrental --from-beginning

ex)
{"eventType":"ToyRented","timestamp":1653456967744,"rentalId":20,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456967755,"rentalId":21,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456968764,"rentalId":22,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456970774,"rentalId":23,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456970788,"rentalId":24,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456971797,"rentalId":25,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456973806,"rentalId":26,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456973822,"rentalId":27,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ToyRented","timestamp":1653456974829,"rentalId":28,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"Paid","timestamp":1653456977046,"payId":null,"rentalId":null,"payStatus":null,"toyRentalPrice":null}
{"eventType":"ToyRented","timestamp":1653456976837,"rentalId":29,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"Paid","timestamp":1653457008897,"payId":null,"rentalId":null,"payStatus":null,"toyRentalPrice":null}
{"eventType":"ToyRented","timestamp":1653457008888,"rentalId":30,"customerId":6,"toyId":6,"rentalStatus":null}
{"eventType":"Paid","timestamp":1653457108429,"payId":1,"rentalId":31,"payStatus":null,"toyRentalPrice":null}
{"eventType":"ToyRented","timestamp":1653457108374,"rentalId":31,"customerId":7,"toyId":7,"rentalStatus":null}
{"eventType":"RentalCancelled","timestamp":1653457194700,"rentalId":31,"customerId":7,"toyId":7,"rentalStatus":"cancel"}
{"eventType":"Registered","timestamp":1653457269061,"toyId":1,"name":"아기 인형","toyRentalPrice":2000,"toyStatus":"AVAILABLE","rentalId":null}
{"eventType":"Registered","timestamp":1653457274033,"toyId":2,"name":"파란 비행기 장난감","toyRentalPrice":3500,"toyStatus":"AVAILABLE","rentalId":null}
{"eventType":"Accepted","timestamp":1653457288544,"toyId":1,"rentalId":null,"name":"아기 인형","toyRentalPrice":2000,"toyStatus":"ACCEPTED"}
{"eventType":"Paid","timestamp":1653457288572,"payId":2,"rentalId":32,"payStatus":null,"toyRentalPrice":null}
{"eventType":"ToyRented","timestamp":1653457288563,"rentalId":32,"customerId":null,"toyId":null,"rentalStatus":"rent complete"}
{"eventType":"ReturnConfirmed","timestamp":1653457301006,"toyId":2,"rentalId":null,"name":"파란 비행기 장난감","toyStatus":"RETURNED","toyRentalPrice":3500}
{"eventType":"Registered","timestamp":1653457460582,"toyId":3,"name":"아이스크림 카트","toyRentalPrice":5000,"toyStatus":"AVAILABLE","rentalId":null}
{"eventType":"RepairRequested","timestamp":1653457464304,"toyId":null,"name":null,"toyStatus":null}
{"eventType":"Paid","timestamp":1653457662721,"payId":null,"rentalId":null,"payStatus":null,"toyRentalPrice":null}
{"eventType":"ToyRented","timestamp":1653457662650,"rentalId":33,"customerId":100,"toyId":100,"rentalStatus":null}
{"eventType":"Paid","timestamp":1653457731581,"payId":1,"rentalId":34,"payStatus":null,"toyRentalPrice":null}
{"eventType":"ToyRented","timestamp":1653457731525,"rentalId":34,"customerId":101,"toyId":101,"rentalStatus":null}
{"eventType":"RentalCancelled","timestamp":1653457764203,"rentalId":34,"customerId":101,"toyId":101,"rentalStatus":"cancel"}
{"eventType":"PayCancelled","timestamp":1653457764271,"payId":1,"rentalId":34,"payStatus":"cencel","toyRentalPrice":null}










## Packaging and Running in docker environment

```
mvn package -B
docker build -t username/repair:v1 .
docker run username/repair:v1
```

## Push images and running in Kubernetes

```
docker login 
# in case of docker hub, enter your username and password

docker push username/repair:v1
```

Edit the deployment.yaml under the /kubernetes directory:
```
    spec:
      containers:
        - name: repair
          image: username/repair:latest   # change this image name
          ports:
            - containerPort: 8080

```

Apply the yaml to the Kubernetes:
```
kubectl apply -f kubernetes/deployment.yml
```

See the pod status:
```
kubectl get pods -l app=repair
```

If you have no problem, you can connect to the service by opening a proxy between your local and the kubernetes by using this command:
```
# new terminal
kubectl port-forward deploy/repair 8080:8080

# another terminal
http localhost:8080
```

If you have any problem on running the pod, you can find the reason by hitting this:
```
kubectl logs -l app=repair
```

Following problems may be occurred:

1. ImgPullBackOff:  Kubernetes failed to pull the image with the image name you've specified at the deployment.yaml. Please check your image name and ensure you have pushed the image properly.
1. CrashLoopBackOff: The spring application is not running properly. If you didn't provide the kafka installation on the kubernetes, the application may crash. Please install kafka firstly:

https://labs.msaez.io/#/courses/cna-full/full-course-cna/ops-utility

