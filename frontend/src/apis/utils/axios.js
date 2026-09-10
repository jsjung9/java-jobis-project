import axios from 'axios';
import router from '@/router';

const API_BASE_URL = import.meta.env.VITE_APP_API_URL;
let refreshPromise = null;

function accessToken() {
    return localStorage.getItem('accessToken');
}

function storeAccessToken(response) {
    const authorization = response.headers.authorization;
    if (!authorization) return;
    localStorage.setItem('accessToken', authorization.replace(/^Bearer\s+/i, ''));
}

async function refreshAccessToken() {
    if (!refreshPromise) {
        refreshPromise = axios
            .post(`${API_BASE_URL}/user/token`, {}, { withCredentials: true })
            .then(response => {
                storeAccessToken(response);
                return accessToken();
            })
            .finally(() => {
                refreshPromise = null;
            });
    }
    return refreshPromise;
}

function localAxios() {
    const instance = axios.create({
        baseURL: API_BASE_URL,
        withCredentials: true,
        headers: { 'Content-Type': 'application/json' },
    });

    instance.interceptors.request.use(config => {
        const token = accessToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });

    instance.interceptors.response.use(
        response => {
            storeAccessToken(response);
            return response;
        },
        async error => {
            const request = error.config;
            const isUnauthorized = error.response?.status === 401;
            const isAuthRequest = request?.url?.includes('/user/token')
                || request?.url?.includes('/user/login');

            if (isUnauthorized && request && !request._retried && !isAuthRequest) {
                request._retried = true;
                try {
                    const token = await refreshAccessToken();
                    request.headers.Authorization = `Bearer ${token}`;
                    return instance(request);
                } catch (refreshError) {
                    localStorage.removeItem('accessToken');
                    await router.replace({ name: 'tokenexpire' });
                    return Promise.reject(refreshError);
                }
            }
            return Promise.reject(error);
        },
    );

    return instance;
}

export { localAxios };
