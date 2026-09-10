import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('../views/HomeView.vue'),
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('@/views/Users/LoginView.vue'),
        },
        {
            path: '/company',
            name: 'company',
            component: () => import('@/views/Company/CompanyView.vue'),
            meta: { requiresAuth: true },
        },
        {
            path: '/companydetail/:companyId',
            name: 'companydetail',
            component: () => import('@/views/Company/CompanyDetail.vue'),
            props: true,
        },
        {
            path: '/mypage',
            name: 'mypage',
            component: () => import('@/views/Users/MypageView.vue'),
            meta: { requiresAuth: true },
        },
        {
            path: '/login-success',
            name: 'loginsuccess',
            component: () => import('@/views/Users/LoginSuccessView.vue'),
        },
        {
            path: '/login-failure',
            name: 'login-failure',
            component: () => import('@/views/Users/LoginFailureView.vue'),
        },
        {
            path: '/token-expire',
            name: 'tokenexpire',
            component: () => import('@/views/Users/TokenExpireView.vue'),
        },
        {
            path: '/search',
            name: 'search',
            component: () => import('@/views/Company/CompanySearchView.vue'),
        },
    ],
});

router.beforeEach((to) => {
    if (to.meta.requiresAuth && !localStorage.getItem('accessToken')) {
        return { name: 'login', query: { redirect: to.fullPath } };
    }
});

export default router;
