<template>
    <div class="container">
        <footer class="py-3 my-4">
            <ul class="nav justify-content-center border-bottom pb-3 mb-3">
                <li class="nav-item">
                    <RouterLink class="nav-link px-2 text-body-secondary" :to="{ name: 'home' }"
                        >Home</RouterLink
                    >
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link px-2 text-body-secondary">Features</a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link px-2 text-body-secondary">Pricing</a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link px-2 text-body-secondary">FAQs</a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link px-2 text-body-secondary">About</a>
                </li>
            </ul>
            <p class="text-center text-body-secondary">© 2024 JOBIS, Inc</p>
        </footer>
    </div>
</template>

<script setup>
defineOptions({ name: 'HomeFooter' });
</script>

<style scoped>
/* @import url('@/assets/footer.css'); */
</style>
