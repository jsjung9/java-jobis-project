<template>
    <div class="container" style="font-family: 'Pretendard-Bold'">
        <div class="welcome">
            <div class="pinkbox" :style="{ transform: pinkboxTransform }">
                <div class="signup" :class="{ nodisplay: !showSignup }">
                    <h1>회원가입</h1>
                    <form autocomplete="off" @submit.prevent="formSignUp">
                        <input type="text" placeholder="이메일" v-model="userEmail" />
                        <span
                            v-if="!emailValid"
                            class="error-message animate__animated animate__shakeX"
                            >@를 포함하여 올바른 형식으로 작성해주세요.</span
                        >
                        <input type="password" placeholder="비밀번호" v-model="password1" />
                        <span
                            v-if="!passwordValid"
                            class="error-message animate__animated animate__shakeX"
                            >8자 이상 72자 이내로 입력해 주세요.</span
                        >
                        <input type="password" placeholder="비밀번호 확인" v-model="password2" />
                        <span
                            v-if="!passwordSamevalid"
                            class="error-message animate__animated animate__shakeX"
                            >비밀번호가 일치하지 않습니다.</span
                        >
                        <button class="button submit">계정 만들기</button>
                    </form>
                </div>
                <div class="signin" :class="{ nodisplay: !showSignin }">
                    <h1>로그인</h1>
                    <form class="more-padding" autocomplete="off" @submit.prevent="formLogIn">
                        <input type="text" placeholder="이메일" v-model="loginEmail" />
                        <span
                            v-if="!loginEmailValid"
                            class="error-message animate__animated animate__shakeX"
                            >이메일을 입력하세요.</span
                        >
                        <input type="password" placeholder="비밀번호" v-model="password" />
                        <span
                            v-if="!loginPasswordValid"
                            class="error-message animate__animated animate__shakeX"
                            >비밀번호를 입력하세요.</span
                        >
                        <button class="button submit">로그인</button>
                    </form>
                    <div class="social-buttons-container">
                        <!-- 카카오 로그인 버튼 -->
                        <button @click="snsLogin('kakao')" class="sns-button">
                            <img src="@/img/kakao.png" alt="Kakao" class="sns-icon" />
                        </button>
                        <!-- 구글 로그인 버튼 -->
                        <button @click="snsLogin('google')" class="sns-button">
                            <img src="@/img/google.png" alt="Google" class="sns-icon" />
                        </button>
                        <!-- 네이버 로그인 버튼 -->
                        <button @click="snsLogin('naver')" class="sns-button">
                            <img src="@/img/naver.png" alt="Naver" class="sns-icon" />
                        </button>
                    </div>
                </div>
            </div>
            <div class="leftbox">
                <h2 class="title"><span>JOB</span>IS</h2>
                <p class="desc">pick your perfect <span>JOB</span></p>
                <!-- <LoginLogo class="logo" /> -->
                <p class="account">계정이 있으신가요?</p>
                <button class="button" @click="showSignIn">로그인</button>
            </div>
            <div class="rightbox">
                <h2 class="title"><span>JOB</span>IS</h2>
                <p class="desc">pick your perfect <span>JOB</span></p>
                <!-- <LoginLogo class="logo" /> -->
                <p class="account">계정이 없으신가요?</p>
                <button class="button" @click="showSignUp">회원가입</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useCounterStore } from '@/stores/counter';
import { signUp, logIn } from '@/apis/api/user';
import router from '@/router';

const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const store = useCounterStore();
const userEmail = ref('');
const password1 = ref('');
const password2 = ref('');
const emailValid = ref(true);
const passwordValid = ref(true);
const passwordSamevalid = ref(true);
const loginEmail = ref('');
const password = ref('');
const loginEmailValid = ref(true);
const loginPasswordValid = ref(true);
const showSignup = ref(false);
const showSignin = ref(true);

onMounted(() => window.scrollTo(0, 0));

const formSignUp = () => {
    emailValid.value = emailPattern.test(userEmail.value);
    passwordValid.value = password1.value.length >= 8 && password1.value.length <= 72;
    passwordSamevalid.value = password1.value === password2.value;
    if (!emailValid.value || !passwordValid.value || !passwordSamevalid.value) return;

    signUp(
        { email: userEmail.value, password: password1.value },
        () => {
            window.alert('회원가입에 성공하였습니다.');
            showSignIn();
        },
        (error) => {
            const message =
                error.response?.status === 409
                    ? '이미 가입된 이메일입니다.'
                    : '회원가입에 실패했습니다. 입력 내용을 확인해 주세요.';
            window.alert(message);
        }
    );
};

const formLogIn = () => {
    loginEmailValid.value = emailPattern.test(loginEmail.value);
    loginPasswordValid.value = password.value.length > 0;
    if (!loginEmailValid.value || !loginPasswordValid.value) return;

    logIn(
        { email: loginEmail.value, password: password.value },
        () => {
            store.setIsLogin(true);
            window.alert('로그인에 성공하였습니다.');
            router.push({ name: 'home' });
        },
        () => window.alert('이메일 또는 비밀번호가 일치하지 않습니다.')
    );
};

const pinkboxTransform = computed(() => (showSignup.value ? 'translateX(80%)' : 'translateX(0%)'));

const showSignIn = () => {
    showSignup.value = false;
    showSignin.value = true;
};

const showSignUp = () => {
    showSignup.value = true;
    showSignin.value = false;
};

const snsLogin = (type) => {
    const redirectUri = encodeURIComponent(window.location.origin);
    window.location.href = `${import.meta.env.VITE_APP_API_URL}/user/login/${type}?redirect_uri=${redirectUri}`;
};
</script>

<style lang="scss" scoped>
@import url('@/assets/login.scss');
</style>
