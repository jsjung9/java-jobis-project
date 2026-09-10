<template>
    <div class="section">
        <div class="container">
            <div class="title">로그인</div>
            <form @submit.prevent="submitLogin">
                <div class="input">
                    <div class="title2">email</div>
                    <input
                        class="textfield"
                        type="email"
                        placeholder="ID를 입력하세요"
                        v-model="email" />
                </div>
                <div class="input">
                    <div class="title2">Password</div>
                    <input
                        class="textfield"
                        type="password"
                        placeholder="Password를 입력하세요"
                        v-model="password" />
                </div>
                <div class="button">
                    <button class="primary" type="submit">
                        <div class="title3">로그인</div>
                    </button>
                </div>
            </form>
            <div class="input2">
                <button class="div" @click="loginKakao">
                    <div class="container2">
                        <svg
                            class="div2"
                            width="18"
                            height="19"
                            viewBox="0 0 18 19"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg">
                            <g transform="translate(0, -10)" clip-path="url(#clip0_338_323)">
                                <path
                                    fill-rule="evenodd"
                                    clip-rule="evenodd"
                                    d="M9.00002 1.1001C4.02917 1.1001 0 4.21306 0 8.05238C0 10.4401 1.5584 12.5451 3.93152 13.7971L2.93303 17.4446C2.84481 17.7669 3.21341 18.0238 3.49646 17.837L7.87334 14.9483C8.2427 14.9839 8.61808 15.0047 9.00002 15.0047C13.9705 15.0047 17.9999 11.8919 17.9999 8.05238C17.9999 4.21306 13.9705 1.1001 9.00002 1.1001Z"
                                    fill="black" />
                            </g>
                            <defs>
                                <clipPath id="clip0_338_323">
                                    <rect
                                        width="17.9999"
                                        height="18"
                                        fill="white"
                                        transform="translate(0 0.5)" />
                                </clipPath>
                            </defs>
                        </svg>
                        <div class="label">카카오 로그인</div>
                    </div>
                </button>
            </div>
            <div class="input2">
                <button class="div3" @click="loginNaver">
                    <svg
                        class="div4"
                        width="16"
                        height="17"
                        viewBox="0 0 16 17"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg">
                        <g clip-path="url(#clip0_338_332)">
                            <path
                                d="M10.8491 9.06267L4.91687 0.5H0V16.5H5.15088V7.936L11.0831 16.5H16V0.5H10.8491V9.06267Z"
                                fill="white" />
                        </g>
                        <defs>
                            <clipPath id="clip0_338_332">
                                <rect
                                    width="16"
                                    height="16"
                                    fill="white"
                                    transform="translate(0 0.5)" />
                            </clipPath>
                        </defs>
                    </svg>
                    <div class="label2">네이버 로그인</div>
                </button>
            </div>
            <div class="input2">
                <button class="google-login" @click="loginGoogle">
                    <svg
                        class="icon-google"
                        width="19"
                        height="19"
                        viewBox="0 0 19 19"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg">
                        <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M18.14 9.70443C18.14 9.06625 18.0827 8.45262 17.9764 7.86353H9.5V11.3449H14.3436C14.135 12.4699 13.5009 13.4231 12.5477 14.0613V16.3194H15.4564C17.1582 14.7526 18.14 12.4453 18.14 9.70443Z"
                            fill="#4285F4" />
                        <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M9.5 18.5C11.93 18.5 13.9673 17.6941 15.4564 16.3195L12.5477 14.0613C11.7418 14.6013 10.7109 14.9204 9.5 14.9204C7.15591 14.9204 5.17182 13.3372 4.46409 11.21H1.45728V13.5418C2.93818 16.4831 5.98182 18.5 9.5 18.5Z"
                            fill="#34A853" />
                        <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M4.46409 11.2101C4.28409 10.6701 4.18182 10.0933 4.18182 9.50007C4.18182 8.90689 4.28409 8.33007 4.46409 7.79007V5.45825H1.45727C0.847727 6.67325 0.5 8.0478 0.5 9.50007C0.5 10.9523 0.847727 12.3269 1.45727 13.5419L4.46409 11.2101Z"
                            fill="#FBBC05" />
                        <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M9.5 4.07955C10.8214 4.07955 12.0077 4.53364 12.9405 5.42545L15.5218 2.84409C13.9632 1.39182 11.9259 0.5 9.5 0.5C5.98182 0.5 2.93818 2.51682 1.45728 5.45818L4.46409 7.79C5.17182 5.66273 7.15591 4.07955 9.5 4.07955Z"
                            fill="#EA4335" />
                    </svg>
                    <div class="sign-in-with-google">Sign in with Google</div>
                </button>
            </div>
            <RouterLink :to="{ name: 'signup' }">
                <div class="tab">회원가입</div>
            </RouterLink>
        </div>
        <svg
            class="vector-200"
            width="1440"
            height="1"
            viewBox="0 0 1440 1"
            fill="none"
            xmlns="http://www.w3.org/2000/svg">
            <path d="M0 1H1440" stroke="black" stroke-opacity="0.1" />
        </svg>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useCounterStore } from '@/stores/counter';
import { RouterLink } from 'vue-router';
import { logIn } from '@/apis/api/user';
import router from '@/router';
const store = useCounterStore();
const email = ref('');
const password = ref('');

const submitLogin = () => {
    const payload = {
        email: email.value,
        password: password.value,
    };
    logIn(
        payload,
        () => {
            store.setIsLogin(true);
            router.push({ name: 'home' });
        },
        () => window.alert('이메일 또는 비밀번호를 확인해 주세요.'),
    );
};

const socialLogin = type => {
    const redirectUri = window.location.origin;
    window.location.href = `${import.meta.env.VITE_APP_API_URL}/user/login/${type}?redirect_uri=${encodeURIComponent(redirectUri)}`;
};

const loginKakao = () => socialLogin('kakao');
const loginNaver = () => socialLogin('naver');
const loginGoogle = () => socialLogin('google');
</script>

<style scoped>
@import url('@/assets/login.css');
</style>
