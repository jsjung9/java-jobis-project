import { localAxios } from '@/apis/utils/axios';

const local = localAxios();

const url = '/recommendation';

// 추천기업
function getRecommendation(worldcupId, success, fail) {
    local.get(`${url}/${worldcupId}`).then(success).catch(fail);
}

export { getRecommendation };
