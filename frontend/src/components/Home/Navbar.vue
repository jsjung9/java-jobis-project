<template>
    <nav class="top-bar">
        <!-- <Logo /> -->
        <!-- <div class="rectangle-4137"><Logo /></div> -->
        <div class="title">
            <RouterLink style="text-decoration: none" :to="{ name: 'home' }">JOBIS</RouterLink>
        </div>
        <div class="navigation">
            <div class="tab">
                <RouterLink style="text-decoration: none" :to="{ name: 'home' }">홈</RouterLink>
            </div>
            <div class="tab" v-if="store.isLogin">
                <RouterLink style="text-decoration: none" :to="{ name: 'company' }"
                    >기업추천</RouterLink
                >
            </div>
            <div class="tab">
                <RouterLink style="text-decoration: none" :to="{ name: 'search' }"
                    >기업검색</RouterLink
                >
            </div>
            <div class="tab" v-if="store.isLogin">
                <RouterLink style="text-decoration: none" :to="{ name: 'mypage' }"
                    >관심기업목록</RouterLink
                >
            </div>
            <!-- <button class="primary" @click="goSignUp">
                <div class="title2">회원가입</div>
            </button> -->
            <div class="tab" v-if="store.isLogin">
                <button class="primary" @click="formLogout">
                    <div class="title2">로그아웃</div>
                </button>
            </div>
            <div class="tab" v-else>
                <button class="primary" @click="goLogin">
                    <div class="title2">로그인</div>
                </button>
            </div>
        </div>
    </nav>
</template>

<script setup>
defineOptions({ name: 'HomeNavbar' });

import router from '@/router';
import { RouterLink } from 'vue-router';
import { useCounterStore } from '@/stores/counter';

const store = useCounterStore();

const goLogin = () => {
    router.push({
        name: 'login',
    });
};

const formLogout = () => {
    store.logOut();
};
</script>

<style scoped>
/* @import url('@/assets/header.css'); */
@import url('@/assets/blueTone/headerblue.css');
</style>
