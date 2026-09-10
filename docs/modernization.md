# 2026 현대화 기록

## 발견한 문제와 수정

| 영역 | 문제 | 수정 | 검증 |
| --- | --- | --- | --- |
| 빌드 | 현재 JDK에서 Gradle 플러그인이 동작하지 않음 | Wrapper와 Spring Boot 3.5 계열로 갱신, Jakarta 전환 | Backend clean test |
| 인증 | 보호 API 경계 누락, 리프레시 토큰 재사용 가능 | 경로별 권한, JSON 401/403, 회전형 HttpOnly refresh cookie | Security boundary·JWT tests |
| 사용자 | 이메일 대소문자 중복과 원문 저장 위험 | 정규화, BCrypt, DB 충돌 변환 | User service tests |
| 기업 | 조회수 read-modify-write 경쟁, 중복 관심 기업 | 원자적 증가 쿼리와 멱등 저장 | Repository integration tests |
| 추천 | 관계 매핑 방향과 무작위 후보 수 불안정 | 관계 모델 정정, 정확히 16개 보장 | Service tests |
| 뉴스 | 무제한 외부 호출, 잘못된 링크 필드, SSRF 가능성 | 캐시, 시간·건수 제한, 허용 호스트, 엄격한 응답 모델 | Python 9 tests |
| 특허 | `split("|")` 오류, 로컬 절대경로, Hadoop 배포본 포함 | 리터럴 파싱, CLI 인자, 독립 Gradle 모듈 | Batch 3 tests |
| Web | 중복 HTTP 처리, 죽은 화면과 자산, 19개 npm 취약점 | Axios 단일 경로, route lazy load, 의존성·자산 정리 | lint 0, audit 0, production build |
| 배포 | 개발 서버 기반 이미지, 비밀값 기본 포함, CI 없음 | 다단계 비루트 이미지, 필수 환경 변수, health check, 4-job CI | YAML parse와 각 모듈 검증 |

## 측정 방법

프런트엔드 수치는 초기 공개본과 현재 본문에서 각각 `npm run build`를 실행해 생성된 Vite 요약을 비교했다. 초기 주 진입 JavaScript는 약 550.97 KB, 현재 `index` 청크는 170.71 KB다. 라우터·차트·화면 코드를 별도 청크로 나눴으므로 첫 화면에서 받지 않는 기능은 지연 로딩된다.

대형 애니메이션 두 개의 합은 약 42.8 MB GIF에서 약 1.7 MB animated WebP로 줄었다. npm 취약점은 초기 설치 결과 19건에서 현재 `npm audit` 0건으로 바뀌었다. 이 수치는 네트워크 전송 압축, 브라우저 캐시, 실제 LCP 개선값을 뜻하지 않으며 실서비스 성능 수치로 확대 해석하지 않는다.

## 검증 범위

2026년 9월 10일 로컬에서 다음을 통과했다.

- Spring Backend: clean test 및 QueryDSL 생성 소스 재구성
- Vue Frontend: clean install, ESLint 경고 0, production build, npm audit 0
- News API: Ruff check·format, pytest 9개
- Patent Batch: clean test 3개, 실행 jar 생성
- Compose·GitHub Actions·Dependabot YAML 파싱

검증 환경에 Docker daemon이 없어 컨테이너 이미지는 직접 빌드하지 못했다. Naver·OAuth·OpenAI 자격증명을 사용하는 실제 외부 종단 간 시나리오도 자동 검증에 포함하지 않았다.
