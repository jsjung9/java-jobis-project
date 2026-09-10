package com.jobis.batch;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class PatentMatcherTest {

    @Test
    void matchesOneApplicantFromPipeSeparatedApplicants() throws Exception {
        PatentMatcher matcher = PatentMatcher.fromCompanyCsv(new StringReader(
                "\"테스트, 주식회사\",\"서울 강남구\"\n다른기업,부산 해운대구"
        ));

        var result = matcher.match(
                "10,\"개인 발명자|테스트, 주식회사 서울 강남구\",\"데이터 처리 장치\""
        );

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().companyKey())
                .isEqualTo("테스트, 주식회사,서울 강남구");
        assertThat(result.orElseThrow().patentCsv())
                .isEqualTo("10,\"개인 발명자|테스트, 주식회사 서울 강남구\",데이터 처리 장치");
    }

    @Test
    void handlesQuotedCommasWithoutShiftingColumns() throws Exception {
        PatentMatcher matcher = PatentMatcher.fromCompanyCsv(new StringReader(
                "\"테스트, 주식회사\",\"서울 강남구\""
        ));

        var result = matcher.match(
                "\"특허, 10\",\"테스트, 주식회사 서울 강남구\",\"장치, 시스템\""
        );

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().patentCsv())
                .isEqualTo("\"특허, 10\",\"테스트, 주식회사 서울 강남구\",\"장치, 시스템\"");
    }

    @Test
    void ignoresMalformedOrUnmatchedRows() throws Exception {
        PatentMatcher matcher = PatentMatcher.fromCompanyCsv(
                new StringReader("테스트,서울 강남구\n기업명만")
        );

        assertThat(matcher.match("열이,두개")).isEmpty();
        assertThat(matcher.match("10,다른기업 부산,특허")).isEmpty();
    }
}
