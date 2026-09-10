import { localAxios } from '@/apis/utils/axios';

const local = localAxios();

const url = '/patent';

// 특허
function getPatent(companyId, success, fail) {
    local.get(`${url}/${companyId}`).then(success).catch(fail);
}

export { getPatent };
