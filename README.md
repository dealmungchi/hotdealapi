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

## Swagger API 문서화 시스템

API 문서는 SpringDoc OpenAPI(Swagger)를 사용하여 생성됩니다. 이 시스템은 컨트롤러 인터페이스와 커스텀 어노테이션을 기반으로 문서를 자동 생성하도록 설계되었습니다.

### 주요 구성 요소

1. **ApiResponseSpec 어노테이션**
   - 컨트롤러 메서드에 사용하여 응답 유형과 오류 코드를 지정
   - 예시: `@ApiResponseSpec(responseClass = HotDealDto.class, errorCodes = {ErrorCode.HOTDEAL_NOT_FOUND})`

2. **ApiResponseCustomizer**
   - `OperationCustomizer`를 구현하여 Swagger 문서를 커스터마이즈
   - 메서드 반환 유형을 기반으로 응답 스키마를 자동 생성
   - API 이해를 돕기 위한 예제 응답 추가
   - 성공 및 오류 응답을 모두 처리

3. **SwaggerExampleGenerator**
   - Swagger 문서화를 위한 예제 객체 제공
   - 예제 DTO를 생성하는 메서드 포함

4. **ApiResponseDocs**
   - API 응답에 대한 스키마 정보를 제공하는 문서 전용 클래스
   - 실제 코드에서는 사용되지 않고, Swagger 문서화 목적으로만 사용

### 작동 방식

- 컨트롤러 인터페이스(`HotDealControllerSpec`, `DealCommentControllerSpec` 등)가 API 엔드포인트 정의
- 메서드의 `@ApiResponseSpec` 어노테이션이 성공 응답 클래스와 가능한 오류 코드 지정
- `ApiResponseCustomizer`는 리플렉션을 사용하여 반환 타입을 분석하고 적절한 문서 생성
- 오류 응답은 `ErrorCode` enum을 기반으로 자동 추가

### 이점

- API 문서의 중앙 집중식 관리
- 일관된 오류 응답 문서화
- 개발자 이해를 돕는 예제 응답
- 문서화 코드의 중복 감소
- 리플렉션 기반 스키마 자동 생성

## 사용법

새 API 엔드포인트 문서화:

1. 적절한 컨트롤러 명세 인터페이스에 메서드 추가
2. `@ApiResponseSpec` 어노테이션으로 메서드 주석 처리
3. 컨트롤러 클래스에서 메서드 구현

예시:

```java
@ApiResponseSpec(
    responseClass = HotDealDto.class,
    errorCodes = {ErrorCode.HOTDEAL_NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND}
)
Mono<ApiResponse<HotDealDto>> getHotDealById(@PathVariable Long id);
```

시스템은 성공 및 오류 응답 모두에 대해 적절한 문서를 자동으로 생성합니다.