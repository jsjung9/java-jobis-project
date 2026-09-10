<script setup>
import { onMounted } from 'vue';
import router from '@/router';

onMounted(() => {
    window.alert('소셜로그인에 실패하였습니다.');

    // Redirect to the root path
    router.push({ name: 'login' });
});
</script>

<style scoped></style>
