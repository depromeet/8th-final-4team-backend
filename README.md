# 하루 한달
> 디프만 8기 파이널 프로젝트 4조 백엔드

### 프로젝트 구조
* month-app : 어플리케이션 계층
* month-external: 외부 API 연동 계층
* month-domain : 도메인 계층 (Domain, Repository, Domain Service)

### firebase-admin.json
```
# month-external/src/main/resources/firebase-admin.json
{
  "type": "...",
  "project_id": "...",
  "private_key_id": "...",
  "private_key": "",
  "client_id": "",
  "auth_uri": "",
  "token_uri": "",
  "auth_provider_x509_cert_url": "",
  "client_x509_cert_url": ""
}
```