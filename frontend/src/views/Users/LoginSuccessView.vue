<script setup>
import { onMounted } from 'vue';
import router from '@/router';
import { useCounterStore } from '@/stores/counter';

const store = useCounterStore();

onMounted(() => {
    const tokens = new URLSearchParams(window.location.hash.slice(1));
    const accessToken = tokens.get('access_token');

    window.history.replaceState({}, document.title, window.location.pathname);

    if (!accessToken) {
        router.replace({ name: 'login-failure' });
        return;
    }

    // 로컬스토리지에 토큰 저장
    localStorage.setItem('accessToken', accessToken);
    store.setIsLogin(true);

    alert('로그인에 성공하였습니다!');

    // Redirect to the root path
    router.push({ name: 'home' });
});
</script>

<style scoped></style>
