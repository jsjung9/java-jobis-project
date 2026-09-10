import { localAxios } from '@/apis/utils/axios';

const local = localAxios();

const url = '/user';

// 회원가입
function signUp(payload, success, fail) {
    local.post(`${url}/register`, payload).then(success).catch(fail);
}

// 로그인
function logIn(payload, success, fail) {
    local.post(`${url}/login`, payload).then(success).catch(fail);
}

function logOut(success, fail) {
    local.post(`${url}/logout`).then(success).catch(fail);
}

function getScraps(success, fail) {
    local.get(`${url}/scrap`).then(success).catch(fail);
}

export { signUp, logIn, logOut, getScraps };
