<template>
    <main>
        <section class="py-5 text-center container">
            <div class="row py-lg-5">
                <div class="col-lg-6 col-md-8 mx-auto">
                    <h1 class="fw-light">스크랩한 회사목록</h1>
                    <p class="lead text-body-secondary">좋은 회사들을 놓치지 마세요.</p>
                </div>
            </div>
        </section>
        <div class="container">
            <div class="row g-3">
                <div class="col-md-4" v-for="company in companyList" :key="company.companyId">
                    <div class="card shadow-sm cursor" @click="goCompanyDeatil(company.companyId)">
                        <div class="position-relative">
                            <img
                                class="bd-placeholder-img card-img-top"
                                width="100%"
                                height="100%"
                                src="@/assets/img/jobis.png"
                                alt="" />

                            <!-- 스크랩 버튼 -->
                            <button
                                type="button"
                                class="position-absolute top-0 end-0 border-0 container1"
                                @click.stop="cancelScrap(company.companyId)">
                                <!-- <i class="bi bi-bookmark-star"></i> -->
                                <i class="bi bi-bookmark-check-fill"></i>
                            </button>
                        </div>
                        <div class="card-body">
                            <p class="card-text">
                                {{ company.name }}
                            </p>
                            <p>{{ company.type }}</p>
                            <div class="d-flex justify-content-between align-items-center">
                                <small class="text-body-secondary">{{ company.address }}</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router';
import { deleteScrap } from '@/apis/api/company';
import { getScraps } from '@/apis/api/user';

onMounted(() => {
    getCompanyList();
});
const companyList = ref([]);
const getCompanyList = () => {
    getScraps(
        res => {
            companyList.value = res.data.data;
        },
        () => {
            companyList.value = [];
        },
    );
};

const cancelScrap = companyId => {
    deleteScrap(
        companyId,
        () => {
            getCompanyList();
        },
        () => window.alert('관심기업 해제에 실패했습니다.'),
    );
};

const goCompanyDeatil = companyId =>
    router.push({
        name: 'companydetail',
        params: { companyId },
    });
</script>

<style scoped>
/* 스크랩 버튼 */

.container1 {
    width: 45px;
    height: 45px;
    background-color: transparent;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    border-radius: 10px;
    color: rgba(255, 255, 0, 0.744);
}

.container1:hover {
    background-color: white;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.041);
}
.container1:active {
    transform: scale(0.9);
}
.cursor {
    cursor: pointer;
}
</style>
