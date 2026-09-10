<template>
    <div>
        <Navbar />
        <RouterView />
        <Footer />
    </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
import Navbar from './components/Home/Navbar.vue';
import Footer from './components/Home/Footer.vue';
</script>

<style>
@font-face {
    font-family: 'Pretendard-Black';
    src: url('@/assets/fonts/Pretendard-Black.woff');
}
@font-face {
    font-family: 'Pretendard-Bold';
    src: url('@/assets/fonts/Pretendard-Bold.woff');
}
* {
    font-family: 'Pretendard-Bold';
}
</style>
