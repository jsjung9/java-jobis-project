<template>
    <div class="container font">
        <h2 style="font-family: 'Pretendard-Bold'; margin-top: 3rem">강소기업 검색</h2>
        <!-- <SearchButton /> -->
        <div class="d-flex w-100 font justify-content-end">
            <div class="input-group mb-3 ms-3 w-25">
                <input
                    @keydown.enter="fetchData"
                    v-model="keyword"
                    type="text"
                    class="form-control"
                    style="font-size: 20px"
                    placeholder="회사명을 검색하세요"
                    aria-label="Recipient's username"
                    aria-describedby="button-addon2" />
                <button
                    @click="fetchData"
                    class="btn btn-outline-secondary"
                    type="button"
                    id="button-addon2">
                    <i class="bi bi-search"></i>
                </button>
            </div>
        </div>
        <div class="d-flex font">
            <button
                class="cssbuttons-io-button1"
                :class="{ active: regionIsclick }"
                @click="clickRiegon">
                지역
            </button>
            <button
                class="cssbuttons-io-button"
                :class="{ active: typeIsclick }"
                @click="clickType">
                산업군
            </button>
        </div>
        <!-- 지역 버튼 클릭 시 나오는 항목들 -->
        <div v-if="regionIsclick" class="d-flex col-10 font" v-bind="regionIsclick">
            <button
                @click="toggleRegionButton(region.id)"
                v-for="region in regions"
                :key="region.id"
                :class="{ active: isRegionSelected(region.id) }"
                class="cssbuttons-io-button1">
                {{ region.region.substring(0, 2) }}
            </button>
        </div>
        <!-- 산업군 버튼 클릭 시 나오는 항목들 -->
        <div v-if="typeIsclick" class="d-flex col-10 font" v-bind="typeIsclick">
            <button
                @click="toggleButton(industry.typeId)"
                v-for="industry in industries1"
                :key="industry.typeId"
                :class="{ active: isTypeSelected(industry.typeId) }"
                class="cssbuttons-io-button">
                {{
                    industry.typeId === 7
                        ? industry.industry.substring(0, 8)
                        : industry.industry.substring(0, 7)
                }}
            </button>
        </div>
        <div v-if="typeIsclick" class="d-flex col-10 font" v-bind="typeIsclick">
            <button
                @click="toggleButton(industry.typeId)"
                v-for="industry in industries2"
                :key="industry.typeId"
                :class="{ active: isTypeSelected(industry.typeId) }"
                class="cssbuttons-io-button">
                {{
                    industry.typeId === 14
                        ? industry.industry.substring(0, 6)
                        : industry.industry.substring(0, 7)
                }}
            </button>
        </div>
    </div>
    <!-- 테이블 임 -->
    <div class="container font" style="margin-top: 2em; text-align: center">
        <div class="row">
            <div class="col-xs-12">
                <table class="table table-bordered table-hover dt-responsive font">
                    <thead>
                        <tr>
                            <th>회사명</th>
                            <th>산업군</th>
                            <th>지역</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(company, index) in data" :key="index">
                            <td
                                @click="goCompanyDeatil(company.companyId)"
                                class="company-detail font">
                                {{ company.name }}
                            </td>
                            <td>{{ company.type }}</td>
                            <td>{{ company.address }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- 페이지 네이션 버튼 -->
        <!-- <div class="d-flex">
            <button class="container pagenation-btn" @click="previousPage" :disabled="page === 0">
                Previous Page
            </button>
            <button
                class="container pagenation-btn"
                @click="changePage(btn)"
                v-for="(btn, index) in pageButtons"
                :key="index"
                :class="{ active: btn === page }">
                {{ btn + 1 }}
            </button>
            <button class="container pagenation-btn" @click="nextPage">Next Page</button>
        </div> -->
        <!-- 페이지 네이션 테스트 용 -->
        <div class="btn-group me-2 font" role="group" aria-label="First group">
            <button
                @click="firstPage"
                :disabled="page === 0"
                type="button"
                class="btn btn-primary font">
                <i class="bi bi-arrow-bar-left"></i>
            </button>
            <button
                @click="previousPage"
                :disabled="page === 0"
                type="button"
                class="btn btn-primary font">
                <i class="bi bi-arrow-left-square"></i>
            </button>
            <button
                type="button"
                class="btn font"
                @click="changePage(btn)"
                v-for="(btn, index) in pageButtons"
                :key="index"
                :class="{ 'btn-primary': btn === page }">
                {{ btn + 1 }}
            </button>
            <button
                type="button"
                class="btn btn-primary font"
                @click="nextPage"
                :disabled="isLastPage">
                <i class="bi bi-arrow-right-square"></i>
            </button>
            <button
                @click="lastPage"
                :disabled="page === Math.ceil(totalData / size) - 1"
                type="button"
                class="btn btn-primary font">
                <i class="bi bi-arrow-bar-right"></i>
            </button>
        </div>
    </div>
</template>

<script setup>
defineOptions({ name: 'CompanySearch' });

import { ref, onMounted, watch, computed } from 'vue';
import { useCounterStore } from '@/stores/counter';
import { useRouter } from 'vue-router';
import { getCompanies } from '@/apis/api/company';

const store = useCounterStore();
const router = useRouter();

// 기업 리스트 받을 변수
const data = ref([]);
const page = ref(store.currentPage);
const size = ref(10); // 페이지당 아이템 수
const keyword = ref('');
const type = ref([]);
const location = ref([]);
const totalData = ref(0);
const pageButtons = ref([]);
const maxButtons = 10; // 최대 버튼 수

const fetchData = async () => {
    const payload = {
        page: page.value,
        size: size.value,
        keyword: keyword.value,
        type: type.value,
        location: location.value,
    };
    getCompanies(
        payload,
        (response) => {
            data.value = response.data.data.content;
            totalData.value = response.data.data.totalElements;
            generatePageButtons();
            // 페이지 스크롤을 최상단으로 이동
            window.scrollTo(0, 130);
        },
        () => {
            data.value = [];
            totalData.value = 0;
        }
    );
};

// /////////////////////////////////////////////페이지 네이션/////////////////////////////////////////////////

const previousPage = () => {
    if (page.value > 0) {
        page.value--;
        store.currentPage--;
    }
};

const nextPage = () => {
    if (page.value < Math.ceil(totalData.value / size.value) - 1) {
        page.value++;
        store.currentPage++;
    }
};

const changePage = (pageIndex) => {
    page.value = pageIndex;
    store.currentPage = pageIndex;
};

const generatePageButtons = () => {
    const totalPages = Math.ceil(totalData.value / size.value);
    const currentPageIndex = page.value;

    let startPage = Math.max(0, currentPageIndex - Math.floor(maxButtons / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxButtons - 1);

    if (endPage - startPage < maxButtons - 1) {
        startPage = Math.max(0, endPage - maxButtons + 1);
    }

    pageButtons.value = Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
};

const firstPage = () => {
    page.value = 0;
    store.currentPage = 0;
};

const lastPage = () => {
    page.value = Math.ceil(totalData.value / size.value) - 1;
    store.currentPage = Math.ceil(totalData.value / size.value) - 1;
};

onMounted(() => {
    fetchData();
});

// 마지막 페이지 여부 확인 함수
const isLastPage = computed(() => {
    const totalPages = Math.ceil(totalData.value / size.value);
    return page.value === totalPages - 1;
});

watch(
    () => page.value,
    () => {
        fetchData();
        generatePageButtons();
    }
);

// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 산업군 필터 검색 버튼 리스트
const industries1 = [
    { typeId: 1, industry: '건설업' },
    { typeId: 2, industry: '광업' },
    { typeId: 3, industry: '교육 서비스업' },
    { typeId: 4, industry: '금융 및 보험업' },
    { typeId: 5, industry: '농업, 임업 및 어업' },
    { typeId: 6, industry: '도매 및 소매업' },
    { typeId: 7, industry: '보건업 및 사회복지 서비스업' },
    { typeId: 8, industry: '부동산업' },
    { typeId: 9, industry: '사업시설 관리, 사업 지원 및 임대 서비스업' },
];

const industries2 = [
    { typeId: 10, industry: '수도, 하수 및 폐기물 처리, 원료 재생업' },
    { typeId: 11, industry: '숙박 및 음식점업' },
    { typeId: 12, industry: '예술, 스포츠 및 여가관련 서비스업' },
    { typeId: 13, industry: '운수 및 창고업' },
    { typeId: 14, industry: '전기, 가스, 증기 및 공기조절 공급업' },
    { typeId: 15, industry: '전문, 과학 및 기술 서비스업' },
    { typeId: 16, industry: '정보통신업' },
    { typeId: 17, industry: '제조업' },
    { typeId: 18, industry: '기타' },
];

// 지역 필터 검색 버튼 리스트
const regions = [
    { id: 1, region: '서울' },
    { id: 2, region: '부산' },
    { id: 3, region: '대구' },
    { id: 4, region: '인천' },
    { id: 5, region: '광주' },
    { id: 6, region: '대전' },
    { id: 7, region: '울산' },
    { id: 8, region: '세종' },
    { id: 9, region: '경기' },
    { id: 10, region: '강원' },
    { id: 11, region: '충북' },
    { id: 12, region: '충남' },
    { id: 13, region: '전남' },
    { id: 14, region: '전북' },
    { id: 15, region: '경남' },
    { id: 16, region: '경북' },
    { id: 17, region: '제주' },
];

// 산업군 별로 필터 로직
const typeIsclick = ref(false);
const clickType = () => {
    if (typeIsclick.value === false) {
        typeIsclick.value = true;
    } else {
        typeIsclick.value = false;
    }
};

// 지역 별로 필터 로직
const regionIsclick = ref(false);
const clickRiegon = () => {
    if (regionIsclick.value === false) {
        regionIsclick.value = true;
    } else {
        regionIsclick.value = false;
    }
};

// 선택된 산업군 필터들
const selectTypeFilter = ref([]);
const isCheckedType = ref(false);

const toggleButton = (buttonId) => {
    for (let i = 0; i < selectTypeFilter.value.length; i++) {
        // selectedTypeFilter 리스트에 i 번째 인덱스에 있는 값이 buttonId(=industries의 typeId 값)과 같으면
        // selectedTypeFilter 리스트에서 해당 값을 제거하여 필터 해제 기능
        if (selectTypeFilter.value[i] === buttonId) {
            selectTypeFilter.value.splice(i, 1);
            isCheckedType.value = true;
            break;
        }
    }
    // 반복문 돌아서 selectTypeFilter 리스트 안에 새로 들어온 buttonId 값이 없으면 리스트에 들어온 값(예: 7,5,3 등등) 추가
    if (isCheckedType.value === false) {
        selectTypeFilter.value.push(buttonId);
    }
    isCheckedType.value = false;
};

// 선택된 지역 필터들
const selectRegionFilter = ref([]);
const isCheckedRegion = ref(false);

const toggleRegionButton = (buttonId) => {
    for (let i = 0; i < selectRegionFilter.value.length; i++) {
        if (selectRegionFilter.value[i] === buttonId) {
            selectRegionFilter.value.splice(i, 1);

            isCheckedRegion.value = true;

            break;
        }
    }
    if (isCheckedRegion.value === false) {
        selectRegionFilter.value.push(buttonId);
    }
    isCheckedRegion.value = false;
};

// 산업군 토글 버튼이 선택되었다는 css 적용 용도로 만들어 놓음
const isTypeSelected = (buttonId) => {
    return selectTypeFilter.value.includes(buttonId);
};

// 지역 토글 버튼이 선택되었다는 css 적용 용도로 만들어 놓음
const isRegionSelected = (buttonId) => {
    return selectRegionFilter.value.includes(buttonId);
};

// Filter가 선택 될 때 마다 필터된 요청 보내기
watch(
    [selectRegionFilter, selectTypeFilter],
    ([newRegionFilters, newTypeFilters]) => {
        location.value = newRegionFilters.map((id) => regions[id - 1].region);
        type.value = newTypeFilters.map((id) =>
            id < 10 ? industries1[id - 1].industry : industries2[id - 10].industry
        );

        page.value = 0;

        fetchData();
    },
    { deep: true }
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 기업 상세 페이지 가기 로직
const goCompanyDeatil = (companyId) =>
    router.push({
        name: 'companydetail',
        params: { companyId },
    });
</script>
<style scoped>
.row {
    background: white;
}
.pagenation-btn {
    height: 4.2rem;
    width: 4.2rem;
}
.company-detail {
    cursor: pointer;
}
/* 버튼 스타일 테스트 용  */
.cssbuttons-io-button {
    align-items: center;
    justify-content: center; /* 텍스트를 수평으로 중앙 정렬합니다 */
    font-family: inherit;
    font-weight: 500;
    font-size: 14px;
    padding: 0.7em 0.8em; /* 필요에 따라 여백을 조정합니다 */
    color: white;
    background: #60b7ff;
    border: none;
    margin: 0.1em;
    letter-spacing: 0.05em;
    border-radius: 20em;
    width: 120px; /* 버튼의 고정된 너비를 설정합니다 */
    white-space: nowrap; /* 텍스트가 줄 바꿈되지 않도록 합니다 */
    overflow: hidden; /* 텍스트가 넘치는 경우 숨깁니다 */
    text-overflow: ellipsis; /* 넘치는 텍스트에 대해 점(...)을 표시합니다 */
}

.cssbuttons-io-button:hover {
    box-shadow: 0 0.5em 1.5em -0.5em #60b7ff;
}

.cssbuttons-io-button:active {
    box-shadow: 0 0.3em 1em -0.5em #60b7ff;
}
/* 수정된 스타일 */
.cssbuttons-io-button.active {
    background: #4a8dc4;
    box-shadow: none; /* 버튼이 활성화되었을 때 그림자를 없앱니다 */
}

.cssbuttons-io-button.active:hover {
    box-shadow: 0 0.5em 1.5em -0.5em #60b7ff;
}

.cssbuttons-io-button.active:active {
    box-shadow: 0 0.3em 1em -0.5em #60b7ff;
}

.cssbuttons-io-button1 {
    align-items: center;
    justify-content: center; /* 텍스트를 수평으로 중앙 정렬합니다 */
    font-family: inherit;
    font-weight: 500;
    font-size: 14px;
    padding: 0.7em 0.8em; /* 필요에 따라 여백을 조정합니다 */
    color: white;
    background: #099cff;
    border: none;
    margin: 0.1em;
    letter-spacing: 0.05em;
    border-radius: 20em;
    width: 120px; /* 버튼의 고정된 너비를 설정합니다 */
    white-space: nowrap; /* 텍스트가 줄 바꿈되지 않도록 합니다 */
    overflow: hidden; /* 텍스트가 넘치는 경우 숨깁니다 */
    text-overflow: ellipsis; /* 넘치는 텍스트에 대해 점(...)을 표시합니다 */
}

.cssbuttons-io-button1:hover {
    box-shadow: 0 0.5em 1.5em -0.5em #099cff;
}

.cssbuttons-io-button1:active {
    box-shadow: 0 0.3em 1em -0.5em #099cff;
}

/* 수정된 스타일 */
.cssbuttons-io-button1.active {
    background: #066eb3;
    box-shadow: none; /* 버튼이 활성화되었을 때 그림자를 없앱니다 */
}

.cssbuttons-io-button1.active:hover {
    box-shadow: 0 0.5em 1.5em -0.5em #099cff;
}

.cssbuttons-io-button1.active:active {
    box-shadow: 0 0.3em 1em -0.5em #099cff;
}

.font {
    font-family: 'Pretendard-Bold';
    font-size: 17px;
}
/* 테이블 스타일 */
/* 테이블 칼럼 너비 조정 */
table {
    border-radius: 10px;
    border-style: hidden;
    box-shadow: 0 0 0 1px white;
}
th {
    background: #007bff;
    color: white;
    /* border-radius: 10px;
    border-style: hidden;
    box-shadow: 0 0 0 1px white; */
}
th:nth-child(1),
td:nth-child(1) {
    width: 103px; /* 첫 번째 칼럼의 너비 */
}

th:nth-child(2),
td:nth-child(2) {
    width: 150px; /* 두 번째 칼럼의 너비 */
}

th:nth-child(3),
td:nth-child(3) {
    width: 300px; /* 세 번째 칼럼의 너비 */
}
th {
    border-radius: 10px;
    border-style: hidden;
    box-shadow: 0 0 0 1px white;
}

/* td {
    
    border-radius: 10px;
    border-style: hidden;
    box-shadow: 0 0 0 1px white;
} */
</style>
