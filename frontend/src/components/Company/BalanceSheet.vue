<template>
    <div class="card1" v-if="hasFinancialData">
        <div class="company-info">
            <div class="financial-section">
                <div class="section1">
                    <div>
                        <h3 style="margin-top: 1em;">재무제표</h3>
                        <table style="margin-top: 1em;" class="indicators">
                            <thead>
                                <tr>
                                    <th>항목</th>
                                    <th>{{ quarter[0].firstQuarterDate }}</th>
                                    <th>{{ quarter[0].secondQuarterDate }}</th>
                                    <th>{{ quarter[0].thirdQuarterDate }}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>유동부채</td>
                                    <td>{{ formatNumber(financialStatements[0].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[0].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[0].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>비유동부채</td>
                                    <td>{{ formatNumber(financialStatements[1].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[1].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[1].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>부채총계</td>
                                    <td>{{ formatNumber(financialStatements[2].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[2].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[2].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>자본금</td>
                                    <td>{{ formatNumber(financialStatements[3].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[3].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[3].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>자본금잉여금</td>
                                    <td>{{ formatNumber(financialStatements[4].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[4].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[4].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>이익잉여금</td>
                                    <td>{{ formatNumber(financialStatements[5].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[5].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[5].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>자본조정</td>
                                    <td>{{ formatNumber(financialStatements[6].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[6].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[6].thirdQuarter) }}</td>
                                </tr>
                                <tr>
                                    <td>자본총계</td>
                                    <td>{{ formatNumber(financialStatements[7].firstQuarter) }}</td>
                                    <td>
                                        {{ formatNumber(financialStatements[7].secondQuarter) }}
                                    </td>
                                    <td>{{ formatNumber(financialStatements[7].thirdQuarter) }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <h3 style="margin-top: 1em; margin-left: 3em;">손익계산서</h3>
                    <table style="margin-top: 1em; margin-left: 3em;"class="indicators">
                        <thead>
                            <tr>
                                <th>항목</th>
                                <th>{{ quarter[0].firstQuarterDate }}</th>
                                <th>{{ quarter[0].secondQuarterDate }}</th>
                                <th>{{ quarter[0].thirdQuarterDate }}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>매출액</td>
                                <td>{{ formatNumber(financialStatements[8].firstQuarter) }}</td>
                                <td>
                                    {{ formatNumber(financialStatements[8].secondQuarter) }}
                                </td>
                                <td>{{ formatNumber(financialStatements[8].thirdQuarter) }}</td>
                            </tr>
                            <tr>
                                <td>매출총이익</td>
                                <td>{{ formatNumber(financialStatements[9].firstQuarter) }}</td>
                                <td>
                                    {{ formatNumber(financialStatements[9].secondQuarter) }}
                                </td>
                                <td>{{ formatNumber(financialStatements[9].thirdQuarter) }}</td>
                            </tr>
                            <tr>
                                <td>영업이익</td>
                                <td>
                                    {{ formatNumber(financialStatements[10].firstQuarter) }}
                                </td>
                                <td>
                                    {{ formatNumber(financialStatements[10].secondQuarter) }}
                                </td>
                                    <td>
                                        {{ formatNumber(financialStatements[10].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>영업외수익</td>
                                    <td>
                                        {{ formatNumber(financialStatements[11].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[11].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[11].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>영업외비용</td>
                                    <td>
                                        {{ formatNumber(financialStatements[12].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[12].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[12].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>당기순이익</td>
                                    <td>
                                        {{ formatNumber(financialStatements[13].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[13].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[13].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>현금흐름표</td>
                                    <td>
                                        {{ formatNumber(financialStatements[14].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[14].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[14].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>영업활동으로 인한 현금흐름</td>
                                    <td>
                                        {{ formatNumber(financialStatements[15].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[15].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[15].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>투자활동으로 인한 현금흐름</td>
                                    <td>
                                        {{ formatNumber(financialStatements[16].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[16].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[16].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>재무활동으로 인한 현금흐름</td>
                                    <td>
                                        {{ formatNumber(financialStatements[17].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[17].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[17].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>현금의 증가감소</td>
                                    <td>
                                        {{ formatNumber(financialStatements[18].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[18].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[18].thirdQuarter) }}
                                    </td>
                                </tr>
                                <tr>
                                    <td>기말현금</td>
                                    <td>
                                        {{ formatNumber(financialStatements[19].firstQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[19].secondQuarter) }}
                                    </td>
                                    <td>
                                        {{ formatNumber(financialStatements[19].thirdQuarter) }}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue';
import { getCompanyQuarter, getStatement } from '@/apis/api/company';
import { useRoute } from 'vue-router';

const route = useRoute();
const companyId = ref(route.params.companyId);
const financialStatements = ref(null);
const quarter = ref(null);
const hasFinancialData = computed(() =>
    financialStatements.value?.length >= 20 && quarter.value?.length > 0,
);

onMounted(() => {
    getCompanySheet();
    loadCompanyQuarter();
});

const getCompanySheet = () => {
    getStatement(route.params.companyId,
        (res) => {
            financialStatements.value = res.data.data;
        },
        (err) => {
            console.log(err)
        }
    )
};

const loadCompanyQuarter = () => {
    getCompanyQuarter(
        companyId.value,
        res => {
            quarter.value = res.data.data;
        },
        () => {
            quarter.value = [];
        },
    );
};

const formatNumber = (value) => {
    return value ? parseFloat(value).toLocaleString() : '-';
};
</script>
<style scoped>
@import url('@/assets/sheet.css');
.section1{
    display: flex;
}
</style>
