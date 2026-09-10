<template>
    <div class="card">
        <div v-if="companyData" class="company-info">
            <div class="info-card">
                <h1>기업 상세 정보</h1>
                <div class="card-content">
                    <div class="details">
                        <div class="d-flex">
                            <h2>{{ companyData.name }}</h2>
                            <div class="scrap-action">
                                <button
                                    v-if="!isLogin"
                                    class="Btn"
                                    type="button"
                                    aria-label="로그인 후 관심 기업으로 저장"
                                    @click="goLogin">
                                    <i class="bi bi-star fs-5 star"></i>
                                </button>
                                <button
                                    v-else
                                    class="Btn"
                                    type="button"
                                    :aria-label="
                                        scraped ? '관심 기업 저장 취소' : '관심 기업으로 저장'
                                    "
                                    @click="scraped ? cancelScrap() : scrap()">
                                    <i
                                        :class="[
                                            'bi',
                                            scraped ? 'bi-star-fill' : 'bi-star',
                                            'fs-5',
                                            'star',
                                        ]"></i>
                                </button>
                            </div>
                        </div>
                        <p>회사주소: {{ companyData.address }}</p>
                        <div><strong>설립일자 :</strong> {{ companyData.foundAt }}</div>
                        <div><strong>대표자명 :</strong> {{ companyData.owner }}</div>
                        <div>
                            <strong>자본금 :</strong> {{ formatNumber(companyData.capital) }}원
                        </div>
                        <div><strong>종업원수 :</strong> {{ companyData.employeeCnt }}명</div>
                        <div><strong>업종 :</strong> {{ companyData.type }}</div>
                        <div><strong>주요제품명 :</strong> {{ companyData.productName }}</div>
                    </div>
                </div>
                <div v-if="companyData.youthCompany && youthCompanyData?.length">
                    <div>고용률 : {{ youthCompanyData[0].employmentRate }}%</div>
                    <div class="youth-stat">
                        평균월급 : {{ formatNumber(youthCompanyData[0].salary) }}원
                    </div>
                    <div class="youth-stat">청년비율 : {{ youthCompanyData[0].youthRate }}%</div>
                    <div class="youth-stat">
                        청년고용률: {{ youthCompanyData[0].youthHireRate }}%
                    </div>
                    <div class="youth-stat">
                        청년고용증가인원 : {{ youthCompanyData[0].youthHireIncreasingCnt }}명
                    </div>
                    <div class="youth-stat">
                        청년고용증가율 : {{ youthCompanyData[0].youthHireIncreasingRate }}%
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useCounterStore } from '@/stores/counter';
import { useRoute } from 'vue-router';
import router from '@/router';
import {
    getCompanyDetail,
    getYouthCompany,
    isScraped,
    addScrap,
    deleteScrap,
} from '@/apis/api/company';

const companyData = ref(null);
const youthCompanyData = ref(null);
const store = useCounterStore();
const scraped = ref(null);

const route = useRoute();
const isLogin = ref(false);

const companyId = ref(route.params.companyId);
onMounted(() => {
    isLogin.value = store.isLogin;
    getCompanyInformation();
});

function goLogin() {
    // 로그인 페이지로 이동
    const con = confirm('로그인이 필요한 활동입니다. \n로그인 페이지로 이동하시겠습니까?');
    if (con) {
        router.push({ name: 'login' });
    }
}

const getCompanyInformation = () => {
    getCompanyDetail(
        companyId.value,
        (res) => {
            companyData.value = res.data.data;
            if (companyData.value.youthCompany) {
                getYouthCompanyInformation(); // 청년친화강소기업 여부가 true면 정보를 가져옴
            }
            if (isLogin.value) {
                getScrap();
            }
        },
        (err) => {
            console.log(err);
        }
    );
};

const getYouthCompanyInformation = () => {
    getYouthCompany(
        companyId.value,
        (res) => {
            // 청년친화강소기업 정보를 가져와서 처리
            youthCompanyData.value = res.data.data;
        },
        (err) => {
            console.log(err);
        }
    );
};

// 스크랩 로직
const scrap = () => {
    if (scraped.value == false) {
        addScrap(
            companyId.value,
            () => {
                getCompanyInformation();
            },
            (err) => {
                console.log('스크랩 실패 했습니다.', err);
            }
        );
    }
};
// scrap true/false 유무 확인하는 로직
const getScrap = () => {
    isScraped(
        companyId.value,
        (res) => {
            scraped.value = res.data.data;
        },
        (err) => {
            console.log('스크랩 true/false 가져오기 실패', err);
        }
    );
};

const cancelScrap = () => {
    deleteScrap(
        companyId.value,
        () => {
            getCompanyInformation();
        },
        (err) => {
            console.log('scrap 취소 실패', err);
        }
    );
};

const formatNumber = (value) => Number(value ?? 0).toLocaleString('ko-KR');
</script>

<style scoped>
@import url('@/assets/information.css');
@import url('@/assets/scrapbtn.css');
</style>

<style scoped>
.scrap-action {
    margin-left: 0.5rem;
}

.youth-stat {
    margin-top: 0.6em;
}
</style>
