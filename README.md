# 하루 한달
> 디프만 8기 파이널 프로젝트 4조 백엔드

### 프로젝트 구조
* month-app : 어플리케이션 계층
* month-domain : 도메인 계층 (Domain, Repository, Domain Service)

## 로컬에서 테스트 하는 방법.

```
$ git clone https://github.com/depromeet/8th-final-4team-backend
```

```
$ cd 8th-final-4team-backend

$ sudo chmod +x ./gradlew

$ ./gradlew clean build +x
```

```
$ docker-compose up -d --build
```

## Docker-compose 서비스 확인
```
$ docker-compose ps

            Name                           Command               State           Ports         
-----------------------------------------------------------------------------------------------
8th-final-4team-backend_dev_1   java -jar month-app/build/ ...   Up      0.0.0.0:8000->8000/tcp

```

## Docker-compose 서비스 종료 및 삭제
```
$ docker-compose down
```

