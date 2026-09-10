import { localAxios } from "@/apis/utils/axios";

const local = localAxios();

const url = "/company";

// 기업조회
function getCompanies(payload, success, fail) {
    local.get(`${url}/search`, {
        params: {
            page: payload.page,
            size: payload.size,
            keyword: payload.keyword || undefined,
            location: payload.location.join(',') || undefined,
            type: payload.type.join(',') || undefined,
        },
    })
    .then(success)
    .catch(fail)
}

// 기업 상세조회
function getCompanyDetail(companyId, success, fail) {
    local.get(`${url}/info/${companyId}`)
    .then(success)
    .catch(fail)
}

// 기업 재무제표 조회
function getStatement(companyId, success, fail) {
    local.get(`${url}/statement/${companyId}`)
    .then(success)
    .catch(fail)
}

// 청년친화 강소기업 조회
function getYouthCompany(companyId, success, fail) {
    local.get(`${url}/youthcompany/${companyId}`)
    .then(success)
    .catch(fail)
}

// 기업 재무비율 조회
function getCompanyRate(companyId, success, fail) {
    local.get(`${url}/rate/${companyId}`)
    .then(success)
    .catch(fail)
}

// 기업 재무비율 조회
function getCompanyQuarter(companyId, success, fail) {
    local.get(`${url}/quarter/${companyId}`)
    .then(success)
    .catch(fail)
}

// 조회수 기업 top10
function getPopularCompany(success, fail) {
    local.get(`${url}/popular`)
    .then(success)
    .catch(fail)
}

// 기업 스크랩
function isScraped(companyId, success, fail) {
    local.get(`${url}/scrap/${companyId}`)
    .then(success)
    .catch(fail)
}

// 기업 스크랩
function addScrap(companyId, success, fail) {
    local.post(`${url}/scrap/${companyId}`)
    .then(success)
    .catch(fail)
}

// 기업 스크랩 취소
function deleteScrap(companyId, success, fail) {
    local.delete(`${url}/scrap/${companyId}`)
    .then(success)
    .catch(fail)
}

export {
    getCompanies,
    getCompanyDetail,
    getStatement,
    getYouthCompany,
    getCompanyRate,
    getCompanyQuarter,
    getPopularCompany,
    isScraped,
    addScrap,
    deleteScrap,
}
