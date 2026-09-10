# Jobis Web

Vue 3 기반 Jobis 프런트엔드입니다. 화면 단위 동적 import로 라우트를 분리하고, 모든 서버 요청은 `src/apis/utils/axios.js`의 공통 클라이언트를 거칩니다.

```bash
npm ci
npm run dev
```

환경 변수 `VITE_APP_API_URL`의 기본값은 `http://localhost:8081/api`입니다. 전체 구성과 인증 흐름은 루트의 [개발 가이드](../docs/development.md)와 [아키텍처 문서](../docs/architecture.md)를 참고하세요.

```bash
npm run lint
npm run build
npm audit --audit-level=high
```
