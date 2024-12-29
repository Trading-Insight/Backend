# Tradin

### 실행 전 필요 사항
- Postgresql, Redis 설치
  - 환경변수 입력 (Intellij Edit Configuration > Environment Variables)
    - DB_HOST=localhost;DB_PASSWORD=1234;DB_PORT=5432;DB_SCHEMA=postgres;DB_USERNAME=postgres;REDIS_HOST=localhost;REDIS_PORT=6379;AES_SECRET_KEY=AES_SECRET_KEY;COGNITO_CLIENT_ID=COGNITO_CLIENT_ID;COGNITO_AUTH_REDIRECT_URI=COGNITO_AUTH_REDIRECT_URI;COGNITO_ISSUER=COGNITO_ISSUER;SWAGGER_USERNAME=SWAGGER_USERNAME;SWAGGER_PASSWORD=SWAGGER_PASSWORD  
    

- Profile 설정
  - Intellij Edit Configuration > VM Options에 -Dspring.profiles.active=dev 입력
