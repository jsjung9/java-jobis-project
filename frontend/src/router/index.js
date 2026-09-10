import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import LoginView from '@/views/Users/LoginView.vue';
import CompanyView from '@/views/Company/CompanyView.vue';
import CompanyDetail from '@/views/Company/CompanyDetail.vue';
import MypageView from '@/views/Users/MypageView.vue';
import SignupView from '@/views/Users/SignupView.vue';
import LoginSuccessView from '@/views/Users/LoginSuccessView.vue';
import LoginFailureView from '@/views/Users/LoginFailureView.vue';
import TokenExpireView from '@/views/Users/TokenExpireView.vue';
import Test from '@/views/Company/Test.vue';
import CompanySearchView from '@/views/Company/CompanySearchView.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView,
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
        },
        {
            path: '/company',
            name: 'company',
            component: CompanyView,
        },
        {
            path: '/companydetail/:companyId',
            name: 'companydetail',
            component: CompanyDetail,
            props: true,
        },
        {
            path: '/mypage',
            name: 'mypage',
            component: MypageView,
        },
        {
            path: '/signup',
            name: 'signup',
            component: SignupView,
        },
        {
            path: '/login-success',
            name: 'loginsuccess',
            component: LoginSuccessView,
        },
        {
            path: '/login-failure',
            name: 'login-failure',
            component: LoginFailureView,
        },
        {
            path: '/token-expire',
            name: 'tokenexpire',
            component: TokenExpireView,
        },
        {
            path: '/test',
            name: 'test',
            component: Test,
        },
        {
            path: '/search',
            name: 'search',
            component: CompanySearchView,
        },
    ],
});

export default router;
