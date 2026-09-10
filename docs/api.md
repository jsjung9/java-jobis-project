# API 계약

모든 Spring API 경로 앞에는 `/api`가 붙는다. 일반 성공 응답은 `code`, `message`, 선택적 `data`를 포함한다. 오류 응답은 HTTP 상태와 함께 `code`, `message`를 반환하며 검증 오류에는 필드별 상세가 추가된다.

## 공개 API

| Method | Path | 설명 | 주요 입력 |
| --- | --- | --- | --- |
| POST | `/user/register` | 이메일 회원 가입 | JSON `email`, `password` |
| POST | `/user/login` | 이메일 로그인 | JSON `email`, `password` |
| GET | `/user/login/{google\|naver\|kakao}` | 소셜 로그인 시작 | `redirect_uri` |
| POST | `/user/token` | 토큰 갱신·회전 | HttpOnly refresh cookie |
| GET | `/user/check?email=` | 이메일 사용 가능 여부 | email |
| GET | `/company/search` | 기업 검색 | `page`, `size≤100`, `keyword`, 반복 `location`, 반복 `type` |
| GET | `/company/info/{companyId}` | 기본 정보 조회와 조회수 증가 | 양의 companyId |
| GET | `/company/statement/{companyId}` | 재무제표 | companyId |
| GET | `/company/rate/{companyId}` | 재무비율 | companyId |
| GET | `/company/youthcompany/{companyId}` | 청년친화 제도 | companyId |
| GET | `/company/quarter/{companyId}` | 분기 고용 정보 | companyId |
| GET | `/company/popular` | 조회수 상위 10개 | 없음 |
| GET | `/worldcup` | 중복 없는 무작위 후보 16개 | 없음 |
| GET | `/recommendation/{worldcupId}` | 후보와 연결된 추천 기업 | 양의 worldcupId |
| GET | `/patent/{companyId}` | 기업 특허 | 양의 companyId |
| GET | `/news/{companyId}/{name}` | 뉴스와 분석 키워드 | 양의 companyId, 기업명 |

## 인증 API

`Authorization: Bearer <access-token>` 헤더가 필요하다.

| Method | Path | 설명 |
| --- | --- | --- |
| POST | `/user/logout` | 서버의 리프레시 토큰을 폐기하고 쿠키를 제거한다. |
| POST | `/user/tendency` | 다섯 기업 성향 점수를 저장한다. |
| GET | `/user/tendency` | 로그인 사용자의 성향 이력을 조회한다. |
| GET | `/user/scrap` | 로그인 사용자의 관심 기업을 조회한다. |
| GET | `/company/scrap/{companyId}` | 로그인 사용자의 저장 여부를 조회한다. |
| POST | `/company/scrap/{companyId}` | 관심 기업을 멱등적으로 저장한다. |
| DELETE | `/company/scrap/{companyId}` | 로그인 사용자의 관심 기업을 삭제한다. |

## 토큰 흐름

로그인 성공 응답은 액세스 토큰을 `Authorization` 헤더로 전달하고 리프레시 토큰은 HttpOnly 쿠키로 설정한다. 프런트엔드는 401을 받으면 `/user/token`을 한 번 호출하며, 동시에 실패한 요청은 하나의 갱신 Promise를 공유한다. 갱신 성공 시 새 액세스 토큰으로 원 요청을 한 번 재시도한다. 갱신 실패 시 로컬 토큰을 지우고 로그인 화면으로 이동한다.

## News API 내부 계약

Spring 서버만 `GET /news/{name}`을 호출한다. 응답은 다음 구조를 따른다.

```json
{
  "news": [
    {"title": "기사 제목", "content": "본문 요약", "date": "2026-09-10", "link": "https://n.news.naver.com/..."}
  ],
  "keywords": [
    {"keyword": "매출성장", "importance": 8, "sentiment": true}
  ]
}
```

키워드는 최대 10개, 문자열은 13자 이하, 중요도는 0~10이다. 예상하지 않은 필드는 Pydantic에서 거부한다. OpenAPI의 최신 DTO 상세는 실행 후 `/api/swagger-ui/index.html`과 News API `/docs`에서 확인할 수 있다.
