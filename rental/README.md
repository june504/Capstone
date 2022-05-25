# rental

## Running in local development environment

```
mvn spring-boot:run
```
##
카프카 실행
/usr/local/kafka/bin/kafka-topics.sh

카프카 이벤트 보기
/usr/local/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic toyrental --from-beginning

## rental test
```
0. 렌탈 조회
http GET localhost:8081/rentals

1. 렌탈 하기
#pay서비스 req, res 
http POST localhost:8081/rentals rentalStatus="rent" customerId=1 toyId=1 

#pay서비스 호출 없이
#안 됨 http POST localhost:8081/rentals/rent rentalStatus=="rent" toyId=="1" customerId=="1"

2. 렌탈 조회
http GET localhost:8081/rentals/1

3. 반납
http PUT localhost:8081/rentals/1/return

4. 취소
http PUT localhost:8081/rentals/1/rentalcancel

'''

## Packaging and Running in docker environment

```
mvn package -B
docker build -t username/rental:v1 .
docker run username/rental:v1
```

## Push images and running in Kubernetes

```
docker login 
# in case of docker hub, enter your username and password

docker push username/rental:v1
```

Edit the deployment.yaml under the /kubernetes directory:
```
    spec:
      containers:
        - name: rental
          image: username/rental:latest   # change this image name
          ports:
            - containerPort: 8080

```

Apply the yaml to the Kubernetes:
```
kubectl apply -f kubernetes/deployment.yml
```

See the pod status:
```
kubectl get pods -l app=rental
```

If you have no problem, you can connect to the service by opening a proxy between your local and the kubernetes by using this command:
```
# new terminal
kubectl port-forward deploy/rental 8080:8080

# another terminal
http localhost:8080
```

If you have any problem on running the pod, you can find the reason by hitting this:
```
kubectl logs -l app=rental
```

Following problems may be occurred:

1. ImgPullBackOff:  Kubernetes failed to pull the image with the image name you've specified at the deployment.yaml. Please check your image name and ensure you have pushed the image properly.
1. CrashLoopBackOff: The spring application is not running properly. If you didn't provide the kafka installation on the kubernetes, the application may crash. Please install kafka firstly:

https://labs.msaez.io/#/courses/cna-full/full-course-cna/ops-utility

