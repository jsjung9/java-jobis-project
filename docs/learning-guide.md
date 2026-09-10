# 코드 학습 가이드

이 문서는 운영 코드에 학습용 장문 주석을 넣지 않고도 설계를 따라갈 수 있게 만든 별도 안내서다.

## 1. 기업 검색에서 시작하기

`CompanyController`의 `/company/search`에서 `CompanyService#getCompanies`로 이동한다. 선택적 검색 조건은 QueryDSL의 `BooleanExpression`으로 변환되며 값이 없으면 `null`을 반환해 where 절에서 제외한다. 결과와 count 쿼리를 분리하고 동일한 조건을 사용한다. 이름과 ID를 함께 정렬해 페이지를 넘겨도 같은 값 사이의 순서가 흔들리지 않게 한다.

## 2. 트랜잭션 경계 보기

Service의 기본 조회는 `@Transactional(readOnly = true)`다. 조회수를 올리는 `findCompany`, 관심 기업을 바꾸는 메서드만 쓰기 트랜잭션으로 재정의한다. 조회수는 엔티티를 읽고 값을 다시 쓰지 않고 Repository update 쿼리 한 번으로 증가시킨다. 이 차이는 동시에 두 요청이 들어올 때 하나의 증가가 사라지는 문제를 막는다.

## 3. 인증 경계 따라가기

`SecurityConfig`에서 공개·인증 경로를 먼저 확인하고, `JwtAuthenticationFilter`에서 토큰 검증 후 SecurityContext에 사용자를 넣는 순서를 본다. `AuthUtil`은 이 컨텍스트의 ID를 도메인 서비스로 전달한다. `/user/token`은 쿠키 토큰, Redis 저장 토큰, 실제 사용자 존재를 차례로 검증한 뒤 두 토큰을 모두 새로 만든다.

## 4. 외부 서비스 실패 다루기

`NewsService`는 cache-aside 흐름을 구현한다. 캐시 미스일 때만 `NewsApiClient`가 FastAPI를 호출하고 5초 안에 응답이 없거나 4xx/5xx면 도메인 오류로 바꾼다. Python 쪽에서는 검색, 기사 추출, 분석을 더 작은 함수로 나눠 각 경계를 가짜 클라이언트로 테스트한다. 실패 시 근거 없는 키워드를 만들지 않는 것이 핵심이다.

## 5. 배치의 입력 계약 이해하기

`PatentBatchJob`은 세 경로를 인자로 받고 Mapper setup에서 기업 CSV를 읽는다. Mapper가 원천 행의 사업자번호를 찾아 출력 레코드를 만든다. 테스트는 헤더·인용부호·리터럴 파이프 파싱과 일치 여부를 확인한다. 배포 환경의 Hadoop 설정은 저장소에 복사하지 않고 실행 환경이 제공한다.

## 6. 프런트엔드 요청 흐름 보기

`frontend/src/apis/utils/axios.js`에서 공통 클라이언트를 확인한 뒤 각 `apis/api/*.js`와 화면 컴포넌트를 따라간다. 요청마다 토큰 처리 코드를 복사하지 않고 interceptor 한곳에서 처리한다. 동시에 여러 요청이 401을 받아도 토큰 갱신 요청은 하나만 실행된다.

## 다음 학습 과제

- Testcontainers로 MySQL·Redis 통합 테스트를 추가하고 H2와의 차이를 기록하기
- Flyway로 `jobis_dump.sql` 대신 버전 관리되는 스키마·기준 데이터 마이그레이션 만들기
- 뉴스 캐시 TTL과 동시 cache miss 합치기(single-flight) 구현하기
- OpenTelemetry로 뉴스 외부 호출 시간과 실패율 측정하기
- Playwright로 로그인, 검색, 스크랩의 브라우저 회귀 테스트 만들기

과제는 현재 구현됐다는 뜻이 아니다. 다음 개선 시 효과와 검증 방법을 함께 정하기 위한 학습 목록이다.
