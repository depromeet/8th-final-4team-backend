# 하루 한달
> 디프만 8기 파이널 프로젝트 4조 백엔드

### 프로젝트 구조
* month-app : 어플리케이션 계층
* month-domain : 도메인 계층 (Domain, Repository, Domain Service)


### RUN Server (로컬 테스트용)
```
sudo chmod +x ./gradlew

# build
./gradlew clean build +x

# compose-up
docker-compose up -d --build
```

### 컨테이너 확인
```
$ docker-compose ps

            Name                           Command               State           Ports         
-----------------------------------------------------------------------------------------------
8th-final-4team-backend_dev_1   java -jar month-app/build/ ...   Up      0.0.0.0:8000->8000/tcp

```

### 컨테이너 삭제
```
$ docker-compose down
```

