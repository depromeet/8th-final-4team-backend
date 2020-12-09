# Jello
> 디프만 8기 파이널 프로젝트 4조 백엔드

### 전체 아키텍처 모델
<img src="/_images/model.png" width="100%" height="100%" title="아키텍처 모델" alt="아키텍처 모델"></img>

## 개발 스택
- JAVA8 + Spring Boot + Spring Web MVC
- Spring Data JPA + QueryDSL
- Gradle + Multi Module
- JUnit5 (단위 테스트 및 통합 테스트)

## 인프라
### Production
- AWS ECS + Fargate + AWS ECR
- AWS RDS (MySQL 5.7)
- Application Load Balancer (ALB)
- Github Action CI/CD
### Test
- AWS EC2
- H2 In-memory DB
- Docker Compose + Nginx Reverse Proxy ⇒ Blue/Green Deploy
- Github Action CI/CD + AWS ECR

## 프로젝트 구조
* month-app : 어플리케이션 계층
* month-external: 외부 API 연동 계층
* month-domainservice: 도메인서비스 계층
* month-domain : 도메인 계층 (Domain, Repository) 
* month-common: 공통 모듈 계층