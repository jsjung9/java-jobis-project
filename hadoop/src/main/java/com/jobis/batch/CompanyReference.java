package com.jobis.batch;

record CompanyReference(String name, String address) {

    CompanyReference {
        name = name.trim();
        address = address.trim();
        if (name.isEmpty() || address.isEmpty()) {
            throw new IllegalArgumentException("기업명과 주소는 비어 있을 수 없습니다.");
        }
    }

    String outputKey() {
        return name + "," + address;
    }
}
