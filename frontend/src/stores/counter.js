import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import router from '@/router';
import { logOut as requestLogout } from '@/apis/api/user';

export const useCounterStore = defineStore(
    'counter',
    () => {
        const isLogin = ref(false);
        const API_URL = import.meta.env.VITE_APP_API_URL?.replace(/\/api\/?$/, '');
        const token = ref(localStorage.getItem('accessToken'));
        const currentPage = ref(0);

        const logOut = function () {
            const clearLogin = () => {
                    token.value = null;
                    isLogin.value = false;
                    localStorage.removeItem('accessToken');
                    router.push({ name: 'home' });
            };
            requestLogout(clearLogin, clearLogin);
        };
        const setIsLogin = function (status) {
            isLogin.value = status;
        };

        return { logOut, isLogin, token, API_URL, currentPage, setIsLogin };
    },
    { persist: true }
);
