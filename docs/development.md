# 개발과 검증 가이드

## 요구 환경

- Java 17 이상
- Node.js 22.13 이상
- Python 3.12 이상
- MySQL 8과 Redis 7, 또는 Docker Compose

## Backend

```bash
cd backend
export DB_PASSWORD='local-password'
export JWT_SECRET_KEY='at-least-32-random-bytes-for-local-dev'
./gradlew bootRun --args='--spring.profiles.active=dev'
```

`application-local.example.yml`은 속성 구조 예시다. 실제 비밀값 파일은 커밋하지 않는다.

```bash
./gradlew clean test
```

테스트 프로필은 H2와 내장 설정을 사용하며 외부 DB나 OAuth 키가 필요하지 않다.

## Frontend

```bash
cd frontend
npm ci
VITE_APP_API_URL=http://localhost:8081/api npm run dev
```

```bash
npm run lint
npm run build
npm audit --audit-level=high
```

## News API

```bash
cd fastapi
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt -r requirements-dev.txt
OPENAI_API_KEY='your-key' uvicorn main:app --reload
```

```bash
python -m ruff check .
python -m ruff format --check .
python -m pytest
```

## Patent Batch

```bash
cd hadoop
./gradlew clean test jar
```

## 전체 스택

루트 `.env.example`을 `.env`로 복사하고 필요한 비밀값을 채운 뒤 실행한다.

```bash
docker compose config
docker compose up --build
```

Docker가 없는 환경에서는 각 모듈 검증으로 코드 품질을 확인할 수 있지만, 이미지 빌드와 컨테이너 간 health check는 확인할 수 없다. CI는 컴파일·테스트·정적 분석을 수행하며 외부 OAuth·Naver·OpenAI의 실제 자격증명은 사용하지 않는다.
