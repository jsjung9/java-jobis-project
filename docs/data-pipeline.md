# 데이터 파이프라인

## 기업 뉴스 분석

1. Spring API가 기업 ID로 Redis 캐시를 조회한다.
2. 캐시가 없을 때만 FastAPI에 정규화한 기업명을 전달한다.
3. Naver 검색 API에서 제한된 수의 후보를 가져와 제목과 URL을 중복 제거한다.
4. 허용한 Naver 뉴스 호스트만 본문을 요청한다. 연결·읽기 timeout과 전체 시간 예산을 적용한다.
5. 기사 배열을 OpenAI Responses API에 보내 JSON Schema에 맞는 키워드 결과를 요청한다.
6. Pydantic이 기사와 키워드의 필드, 길이, 중요도 범위를 다시 검증한다.
7. Spring API가 기사 링크를 포함한 결과를 Redis에 저장하고 클라이언트에 반환한다.

외부 연동이 실패하면 임의의 분석값을 만들지 않는다. 호출자는 502 응답을 받고 재시도 여부를 결정한다. 테스트는 HTTP와 OpenAI 클라이언트를 대체해 허용 호스트, 중복 제거, 오류 변환, 출력 범위를 검증한다.

## 특허 배치

```bash
cd hadoop
./gradlew clean test jar
java -jar build/libs/jobis-patent-batch-1.0.0-all.jar \
  <patent-input> <company-reference.csv> <output-dir>
```

기업 기준 CSV는 인용부호와 구분자 안의 문자를 보존하도록 Commons CSV로 읽는다. 원천 특허 행은 정규식 메타문자가 아닌 **리터럴 `|` 구분자**로 분리한다. 기존 구현의 `split("|")`는 빈 정규식 대안으로 해석돼 사실상 모든 문자 사이를 나누는 오류가 있었다.

Mapper는 기준정보의 사업자번호와 일치하는 행만 출력하고 다음 카운터를 남긴다.

- `MATCHED_RECORDS`: 정상 결합해 출력한 행
- `INVALID_RECORDS`: 필수 열이나 사업자번호가 유효하지 않은 행
- `UNMATCHED_RECORDS`: 형식은 맞지만 기준정보에서 회사를 찾지 못한 행

입력·기준정보·출력 경로를 코드 밖 인자로 받으므로 로컬 파일시스템과 HDFS에서 같은 작업 정의를 사용할 수 있다. 저장소에는 Hadoop 실행 배포본과 런타임 로그를 포함하지 않는다.
