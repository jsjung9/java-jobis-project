<template>
    <h1
        style="
            margin-bottom: 5rem;
            font-family: 'Pretendard-Bold';
            text-align: center;
            margin-left: 1em;
            margin-bottom: 2rem;
            margin-top: 4rem;
            color: #007bff;
        "
        data-aos="flip-left"
        data-aos-duration="1000">
        VIEWS TOP 10
    </h1>
    <!-- <div class="body">
        <h2 class="main" contenteditable="true">조회수 상위 기업</h2>
    </div> -->

    <section style="background-color: white; height: 50vh" class="d-flex">
        <div class="container" style="align-items: center; justify-content: center">
            <Swiper
                style="border: 3px solid #007bff; border-radius: 1.5rem"
                :loop="true"
                :speed="15000"
                :free-mode="true"
                :modules="[EffectCards, Autoplay]"
                :autoplay="{
                    delay: 1,
                    disableOnInteraction: false,
                }">
                <SwiperSlide>
                    <div class="swiper-slide">
                        <div
                            class="card-container"
                            v-for="company in companyList1"
                            :key="company.companyId">
                            <RouterLink
                                style="text-decoration: none"
                                :to="{
                                    name: 'companydetail',
                                    params: { companyId: company.companyId },
                                }">
                                <div class="card card-size">
                                    <div class="card-body">
                                        <img class="img-size" src="@/assets/img/jobis.png" alt="" />
                                        <h5 class="card-title font">
                                            {{ company.name }}
                                        </h5>
                                    </div>
                                </div>
                            </RouterLink>
                        </div>
                    </div>
                </SwiperSlide>
                <SwiperSlide>
                    <div class="swiper-slide">
                        <div
                            class="card-container"
                            v-for="company in companyList2"
                            :key="company.companyId">
                            <RouterLink
                                style="text-decoration: none"
                                :to="{
                                    name: 'companydetail',
                                    params: { companyId: company.companyId },
                                }">
                                <div class="card card-size">
                                    <div class="card-body">
                                        <img class="img-size" src="@/assets/img/jobis.png" alt="" />
                                        <h5 class="card-title font">
                                            {{ company.name }}
                                        </h5>
                                    </div>
                                </div>
                            </RouterLink>
                        </div>
                    </div>
                </SwiperSlide>
            </Swiper>
        </div>
    </section>
</template>

<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue';
import { EffectCards, Autoplay } from 'swiper/modules';
import 'swiper/css';
import { onMounted, ref } from 'vue';
import { getPopularCompany } from '@/apis/api/company';

const companyList = ref(null);
const companyList1 = ref([]);
const companyList2 = ref([]);

onMounted(() => {
    getTopViewList();
});
const getTopViewList = () => {
    getPopularCompany(
        (res) => {
            companyList.value = res.data.data;
            // companyList를 반으로 나누어 companyList1과 companyList2에 할당
            const halfIndex = Math.ceil(companyList.value.length / 2);
            companyList1.value = companyList.value.slice(0, halfIndex);
            companyList2.value = companyList.value.slice(halfIndex);
        },
        () => {}
    );
};
</script>

<style scoped>
@import url('@/assets/blueTone/maintext.css');
.swiper {
    width: 100%;
    height: 80%;
    margin-left: 1.5rem;
}
.swiper-slide {
    text-align: center;
    background: white;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: transform 0.3s;
}
.card {
    border: none;
}

.card-container {
    width: 20rem;
    height: 15rem;
}
.card-size {
    width: 12rem;
    height: 12rem;
}
.img-size {
    height: 12rem;
    width: 12rem;
    margin-bottom: 0.5rem;
    border-radius: 1rem;
}
.font {
    font-family: 'Pretendard-Bold';
    font-size: 1.1rem;
    width: 12rem;
}
</style>
