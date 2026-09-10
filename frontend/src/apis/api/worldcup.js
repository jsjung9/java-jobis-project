import { localAxios } from '@/apis/utils/axios';

const local = localAxios();

const url = '/worldcup';

// 월드컵
function worldcup(success, fail) {
    local.get(`${url}`).then(success).catch(fail);
}

export { worldcup };
