# HotDeal API

Spring WebFlux 기반의 핫딜 API 서버입니다.

## 실행 방법

### 개발 환경 (Dev Profile)

```bash
export $(grep -v '^#' .env | xargs) && ./gradlew bootRun --args='--spring.profiles.active=dev'
```

또는 환경변수 설정:

```bash
export SPRING_PROFILES_ACTIVE=dev
export $(grep -v '^#' .env | xargs) && ./gradlew bootRun
```

### 프로덕션 환경 (Prod Profile)

```bash
export $(grep -v '^#' .env | xargs) && ./gradlew bootRun --args='--spring.profiles.active=prod'
```

또는 환경변수 설정:

```bash
export SPRING_PROFILES_ACTIVE=prod
export $(grep -v '^#' .env | xargs) && ./gradlew bootRun
```

> 참고: 프로필을 지정하지 않으면 기본값으로 'dev' 프로필이 적용됩니다.
