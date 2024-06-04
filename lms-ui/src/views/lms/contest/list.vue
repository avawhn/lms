<template>
    <h1>测试列表</h1>
    <div class="app-container">
        <el-table v-loading="loading" :data="contestList">
            <el-table-column prop="contestId" label="测试编号" width="100" />
            <el-table-column prop="title" label="题目" width="180" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="startTime" label="开始时间" width="180" />
            <el-table-column prop="endTime" label="结束时间" width="180" />
            <el-table-column prop="totalScore" label="满分" width="180" />
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button
                        type="success"
                        icon="Document"
                        size="small"
                        @click="exportScore(scope.row)"
                        >导出分数</el-button
                    >
                    <el-button
                        type="danger"
                        icon="Delete"
                        size="small"
                        @click="remove(scope.row)"
                        >删除</el-button
                    >
                </template>
            </el-table-column>
        </el-table>
        <pagination
            v-show="total > 0"
            :total="total"
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            @pagination="getList"
        />
    </div>
</template>

<script>
import { listContest,delContest } from "@/api/lms/contest";
import { ElMessage, ElMessageBox } from 'element-plus';

export default {
    name: "ContestList",
    data() {
        return {
            loading: false,
            total: 0,
            contestList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
            },
        };
    },
    methods: {
        exportScore(row) {
            // console.log(row);

            this.download(
                "hustoj/contest/export/score/" + row.contestId,
                {
                    ...this.queryParams,
                },
                `score_${new Date().getTime()}.xlsx`
            );
        },
        remove(row) {
            console.log(row);
            this.$modal
                .confirm('是否确认删除 "' + row.title + '"？')
                .then(function () {
                    return delContest(row.contestId);
                })
                .then(() => {
                    this.getList();
                    this.$modal.msgSuccess("删除成功");
                })
                .catch(() => {});
            // delContest
        },
        getList() {
            this.loading = true;
            listContest(this.queryParams).then((response) => {
                this.total = response.total;
                this.contestList = response.rows;
                this.loading = false;
                console.log(response);
            });
        },
    },
    created() {
        this.getList();
    },
};
</script>
