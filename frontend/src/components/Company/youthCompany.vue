<template>
    <div v-if="youthCompanyData.length > 0" class="card1">
        <h2 style="margin-top: 1em; margin-left: 1em">복지 지원 여부</h2>
        <div style="margin-left: 0.8em">
            <img
                v-if="youthCompanyData[0].supportSelfDevelopment == false"
                src="@/assets/img/welfare_false/교육_자기개발비지원_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportSelfDevelopment == true"
                src="@/assets/img/welfare_false/교육_자기개발비지원_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportEducation == false"
                src="@/assets/img/welfare_false/교육제도운영_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportEducation == true"
                src="@/assets/img/welfare_false/교육제도운영_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].cafeteria == false"
                src="@/assets/img/welfare_false/구내식당_카페테리아_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].cafeteria == true"
                src="@/assets/img/welfare_false/구내식당_카페테리아_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].dormCommuteBus == false"
                src="@/assets/img/welfare_false/기숙사_통근차량_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].dormCommuteBus == true"
                src="@/assets/img/welfare_false/기숙사_통근차량_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].businessLounge == false"
                src="@/assets/img/welfare_false/기타휴게시설_복지공간_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].businessLounge == true"
                src="@/assets/img/welfare_false/기타휴게시설_복지공간_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].companyClub == false"
                src="@/assets/img/welfare_false/동호회_학습조직_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].companyClub == true"
                src="@/assets/img/welfare_false/동호회_학습조직_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportCultureLife == false"
                src="@/assets/img/welfare_false/문화생활비_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportCultureLife == true"
                src="@/assets/img/welfare_false/문화생활비_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportSomethingElse == false"
                src="@/assets/img/welfare_false/부가지원제도_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportSomethingElse == true"
                src="@/assets/img/welfare_false/부가지원제도_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].gym == false"
                src="@/assets/img/welfare_false/스포츠센터_운동시설_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].gym == true"
                src="@/assets/img/welfare_false/스포츠센터_운동시설_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].flexibleWorkArrangement == false"
                src="@/assets/img/welfare_false/유연근무제_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].flexibleWorkArrangement == true"
                src="@/assets/img/welfare_false/유연근무제_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].childcareFacility == false"
                src="@/assets/img/welfare_false/육아시설_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].childcareFacility == true"
                src="@/assets/img/welfare_false/육아시설_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportChild == false"
                src="@/assets/img/welfare_false/자녀양육지원_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].supportChild == true"
                src="@/assets/img/welfare_false/자녀양육지원_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].nineToSix == false"
                src="@/assets/img/welfare_false/정시퇴근제_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].nineToSix == true"
                src="@/assets/img/welfare_false/정시퇴근제_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].refreshHoliday == false"
                src="@/assets/img/welfare_false/특별휴가지원_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].refreshHoliday == true"
                src="@/assets/img/welfare_false/특별휴가지원_TRUE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].goToSchoolLater == false"
                src="@/assets/img/welfare_false/후진학참여지원제도_FALSE.png"
                alt="" />
            <img
                v-if="youthCompanyData[0].goToSchoolLater == true"
                src="@/assets/img/welfare_false/후진학참여제도_TRUE.png"
                alt="" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getYouthCompany } from '@/apis/api/company';
const route = useRoute();

const companyId = ref(route.params.companyId);
const youthCompanyData = ref([]);

onMounted(() => {
    getYouthCompanyInformation();
});

const getYouthCompanyInformation = () => {
    getYouthCompany(
        companyId.value,
        (res) => {
            youthCompanyData.value = res.data.data;
        },
        () => {
            youthCompanyData.value = [];
        }
    );
};
</script>

<style scoped>
@import url('@/assets/sheet.css');
img {
    width: 13rem;
    height: 10rem;
}
</style>
