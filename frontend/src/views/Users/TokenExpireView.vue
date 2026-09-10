<script setup>
import { onMounted } from 'vue';
import router from '@/router';
import { useCounterStore } from '@/stores/counter';

const store = useCounterStore();

onMounted(() => {
    window.alert('로그인 세션이 만료되었습니다.');
    store.setIsLogin(false);
    localStorage.removeItem('accessToken');
    router.replace({ name: 'login' });
});
</script>

<style scoped>

</style>
