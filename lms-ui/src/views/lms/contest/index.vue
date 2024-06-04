<template>
    <div class="app-container">
        <h1>我的测试</h1>

        <el-table v-loading="loading" :data="contestList">
            <el-table-column
                prop="contest.contestId"
                label="测试编号"
                width="100"
            />
            <el-table-column prop="contest.title" label="题目" width="180" />
            <el-table-column prop="contest.description" label="描述" />
            <el-table-column
                prop="contest.startTime"
                label="开始时间"
                width="160"
            />
            <el-table-column
                prop="contest.endTime"
                label="结束时间"
                width="160"
            />

            <el-table-column prop="score" label="我的分数" width="100" align="center"/>
            <el-table-column
                prop="contest.totalScore"
                label="总分数"
                width="180"
            />

            <el-table-column label="操作" width="100" align="center">
                <template #default="scope">
                    <el-button
                        type="primary"
                        size="small"
                        plain
                        @click="toToTest(scope.row)"
                        v-if="canStart(scope.row)"
                        >开始测试</el-button
                    >
                    <el-tag type="primary" v-if="beforeStart(scope.row)"
                        >未开始</el-tag
                    >
                    <el-tag type="info" v-if="afterEnd(scope.row)"
                        >已结束</el-tag
                    >
                    <el-tag type="success" v-if="hasSubmit(scope.row)"
                        >已提交</el-tag
                    >
                </template>
            </el-table-column>
        </el-table>
        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />
    </div>
</template>

<script>
import { listContest, listUserContest } from "@/api/lms/contest";
import useUserStore from "@/store/modules/user";

export default {
    name: "MyTest",
    components: {},
    setup() {
        const userStore = useUserStore();
        return { userStore };
    },
    data() {
        return {
            loading: false,
            contestList: [],
            total: 0,
            // 查询参数
            queryParams: {
                // userId: this.userStore.userId,
                userId: 1,
                pageNum: 1,
                pageSize: 10,
                type: "",
            },
        };
    },
    methods: {
        canStart(row) {
            if (row.status === "1") {
                return false;
            }
            // 将时间字符串转换为 Date 对象
            const start = new Date(row.contest.startTime);
            const end = new Date(row.contest.endTime);
            const now = new Date(); // 获取当前时间
            // 判断当前时间是否在 startTime 和 endTime 之间
            return now >= start && now <= end;
        },
        beforeStart(row) {
            const start = new Date(row.contest.startTime);
            const now = new Date(); // 获取当前时间
            return now < start;
        },
        afterEnd(row) {
            if (row.status === "1") {
                return false;
            }
            const end = new Date(row.contest.endTime);
            const now = new Date(); // 获取当前时间
            return now > end;
        },
        hasSubmit(row) {
            return row.status === "1";
        },
        toToTest(row) {
            this.$router.push({
                path: "/contest/test",
                query: { contestId: row.contest.contestId },
            });
        },
        getList() {
            listUserContest(this.queryParams).then((response) => {
                this.contestList = response.rows;
                this.total = response.total;
                console.log(response);
            });
        },
    },
    created() {
        this.getList();
    },
};
</script>
