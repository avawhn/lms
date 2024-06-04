<template>
    <div class="app-container">
        <el-button
            type="primary"
            plain
            @click="passRateStatistics"
            size="small"
            icon="PieChart"
            >通过率统计</el-button
        >

        <el-table v-loading="loading" :data="recordList">
            <el-table-column
                label="提交编号"
                align="center"
                class-name="small-padding fixed-width"
            >
                <template #default="scope">
                    <el-link type="primary" @click="goToProblem(scope.row)">{{
                        scope.row.solutionId
                    }}</el-link>
                </template>
            </el-table-column>

            <el-table-column label="题目" align="center" prop="problem.title" />
            <el-table-column label="题目编号" align="center" prop="problemId" />
            <el-table-column label="内存" align="center" prop="memory" />
            <el-table-column label="时间" align="center" prop="time" />
            <el-table-column
                label="代码长度"
                align="center"
                prop="codeLength"
            />
            <el-table-column label="结果" align="center" prop="msg" />
            <el-table-column label="通过率" align="center" prop="passRate" />
        </el-table>

        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />

        <Chart
            v-model:open="chartOpen"
            :option="option"
            v-model:loading="chartLoading"
        />
    </div>
</template>

<script>
import {
    listSolutionRecord,
    getPassRateStatisticsByUserId,
} from "@/api/lms/solution";
import useUserStore from "@/store/modules/user";
import Chart from "@/components/Chart";

export default {
    name: "ProblemRecord",
    components: { Chart },
    setup() {
        const userStore = useUserStore();
        return { userStore };
    },
    data() {
        return {
            chartOpen: false,
            chartLoading: false,
            option: {
                tooltip: {
                    trigger: "item",
                },
                legend: {
                    top: "5%",
                    left: "center",
                },
                series: [
                    {
                        name: "Access From",
                        type: "pie",
                        radius: ["40%", "70%"],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: "#fff",
                            borderWidth: 2,
                        },
                        label: {
                            show: false,
                            position: "center",
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: 40,
                                fontWeight: "bold",
                            },
                        },
                        labelLine: {
                            show: false,
                        },
                        data: [],
                    },
                ],
            },
            recordList: [],
            loading: true,
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                userId: this.userStore.name,
            },
        };
    },
    methods: {
        passRateStatistics() {
            this.option.series[0].data = [];
            getPassRateStatisticsByUserId(this.userStore.name).then(
                (response) => {
                    const data = [
                        {
                            value: 0,
                            name: "[0%,60%)",
                        },
                        {
                            value: 0,
                            name: "[60%,100%)",
                        },
                        {
                            value: 0,
                            name: "100%",
                        },
                    ];
                    response.data.forEach((d) => {
                        if (d.passRate < 0.6) {
                            data[0].value += d.count;
                        } else if (d.passRate < 1) {
                            data[1].value += d.count;
                        } else {
                            data[2].value += d.count;
                        }
                    });
                    this.option.series[0].data = data;
                    this.chartOpen = true;
                    this.chartLoading = false;
                }
            );
        },
        goToProblem(row) {
            this.$router.push({
                path: "/problem/algorithm",
                query: { id: row.problemId, solutionId: row.solutionId },
            });
        },
        getList() {
            this.loading = true;
            listSolutionRecord(this.queryParams).then((response) => {
                console.log(response);
                response.rows.forEach((r) => {
                    r.passRate = r.passRate * 100 + "%";
                    r.memory = r.memory + "KiB";
                    r.time = r.time + "ms";
                    r.codeLength = r.codeLength + "bytes";
                });
                this.recordList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
    },
    created() {
        this.getList();
    },
};
</script>
