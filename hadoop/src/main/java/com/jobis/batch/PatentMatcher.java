package com.jobis.batch;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class PatentMatcher {

    private static final CSVFormat CSV = CSVFormat.DEFAULT.builder()
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .get();

    private final List<CompanyReference> companies;

    PatentMatcher(List<CompanyReference> companies) {
        this.companies = List.copyOf(companies);
    }

    static PatentMatcher fromCompanyCsv(Reader reader) throws IOException {
        List<CompanyReference> companies = new ArrayList<>();
        try (CSVParser parser = CSV.parse(reader)) {
            for (CSVRecord record : parser) {
                if (record.size() < 2 || record.get(0).isBlank() || record.get(1).isBlank()) {
                    continue;
                }
                companies.add(new CompanyReference(record.get(0), record.get(1)));
            }
        }
        return new PatentMatcher(companies);
    }

    Optional<MatchedPatent> match(String patentCsvLine) throws IOException {
        Optional<CSVRecord> parsedRecord = parseSingleRecord(patentCsvLine);
        if (parsedRecord.isEmpty()) {
            return Optional.empty();
        }
        CSVRecord record = parsedRecord.get();
        if (record.size() < 3) {
            return Optional.empty();
        }

        String applicants = record.get(1);
        for (String applicant : applicants.split("\\|")) {
            String normalizedApplicant = applicant.trim();
            Optional<CompanyReference> company = companies.stream()
                    .filter(candidate ->
                            normalizedApplicant.contains(candidate.name())
                                    && normalizedApplicant.contains(candidate.address()))
                    .findFirst();
            if (company.isPresent()) {
                return Optional.of(new MatchedPatent(
                        company.get().outputKey(),
                        toCsv(record.get(0), applicants, record.get(2))
                ));
            }
        }
        return Optional.empty();
    }

    private Optional<CSVRecord> parseSingleRecord(String line) throws IOException {
        try (CSVParser parser = CSV.parse(new java.io.StringReader(line))) {
            var iterator = parser.iterator();
            return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
        }
    }

    private String toCsv(String... values) throws IOException {
        StringBuilder output = new StringBuilder();
        try (org.apache.commons.csv.CSVPrinter printer =
                     new org.apache.commons.csv.CSVPrinter(output, CSV)) {
            printer.printRecord((Object[]) values);
        }
        return output.toString().stripTrailing();
    }

    record MatchedPatent(String companyKey, String patentCsv) {
    }
}
