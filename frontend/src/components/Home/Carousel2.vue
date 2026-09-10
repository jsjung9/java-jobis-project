<template>
    <div id="carouselExampleInterval" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner" style="height: 300px">
            <div class="carousel-item active" data-bs-interval="3000">
                <img src="@/assets/img/banner1.png" class="d-block w-100 border" alt="..." />
            </div>
            <div class="carousel-item" data-bs-interval="3000">
                <img src="@/assets/img/banner2.png" class="d-block w-100 border" alt="..." />
            </div>
            <div class="carousel-item" data-bs-interval="3000">
                <img src="@/assets/img/banner3.png" class="d-block w-100 border" alt="..." />
            </div>
        </div>
        <button
            class="carousel-control-prev"
            type="button"
            data-bs-target="#carouselExampleInterval"
            data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button
            class="carousel-control-next"
            type="button"
            data-bs-target="#carouselExampleInterval"
            data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
</template>

<script setup>
defineOptions({ name: 'HomeCarousel' });
</script>

<style scoped>
.border {
    border-radius: 1em;
}
</style>
