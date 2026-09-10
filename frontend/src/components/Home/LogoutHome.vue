<template>
    <div>
        <Typing />
        <Top10CompanyList />
        <GoWorldcup
            data-aos="fade-up"
            data-aos-offset="10"
            data-aos-delay="50"
            data-aos-duration="1000"
            data-aos-easing="ease-in-out"
            data-aos-mirror="true"
            data-aos-once="false"
            data-aos-anchor-placement="top-bottom" />
        <Carousel2
            data-aos="fade-up"
            data-aos-offset="200"
            data-aos-delay="50"
            data-aos-duration="1000"
            data-aos-easing="ease-in-out"
            data-aos-mirror="true"
            data-aos-once="false"
            data-aos-anchor-placement="top-bottom" />
    </div>
</template>

<script setup>
import Carousel2 from '@/components/Home/Carousel2.vue';
import Top10CompanyList from '@/components/Home/Top10CompanyList.vue';
import Typing from '@/components/Home/Typing.vue';
import GoWorldcup from '@/components/Home/GoWorldcup.vue';
</script>

<style scoped></style>
