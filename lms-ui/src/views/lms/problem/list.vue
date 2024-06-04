<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            :inline="true"
            v-show="showSearch"
            label-width="80px"
        >
            <el-form-item label="题目类型">
                <el-select
                    v-model="queryParams.type"
                    placeholder="请选择题目类型"
                    style="width: 100px"
                    @change="getList"
                >
                    <el-option
                        v-for="item in typeOption"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>
            </el-form-item>
        </el-form>

        <el-table :data="problemList" v-if="queryParams.type === '4'">
            <el-table-column label="题目ID" align="center">
                <template #default="scope">
                    <el-link
                        type="primary"
                        @click="goToAlgorithmProblem(scope.row)"
                        >{{ scope.row.title }}</el-link
                    >
                </template>
            </el-table-column>

            <el-table-column label="标题" align="center" prop="title" />
            <el-table-column label="时间限制" align="center" prop="timeLimit" />
            <el-table-column
                label="内存限制"
                align="center"
                prop="memoryLimit"
            />
            <el-table-column label="来源" align="center" prop="source" />
        </el-table>

        <div v-else class="content">
            <div v-if="queryParams.type === '1'">
                <div v-for="item in problemList">
                    <span v-html="item.description"></span>

                    <el-radio
                        disabled
                        v-for="ans in item.answerList"
                        v-model="ans.correct"
                        :label="true"
                        >{{ ans.value }}</el-radio
                    >
                </div>
            </div>

            <div v-if="queryParams.type === '2'">
                <div v-for="item in problemList">
                    <span v-html="item.description"></span>
                    <el-checkbox
                        v-model="ans.correct"
                        disabled
                        v-for="ans in item.answerList"
                        >{{ ans.value }}</el-checkbox
                    >
                </div>
            </div>

            <div v-if="queryParams.type === '3'">
                <div v-for="item in problemList">
                    <span v-html="item.description"></span>

                    <span
                        class="blankAns"
                        v-for="(ans, index) in item.answerList"
                    >
                        （ {{ index + 1 }} ）
                        <u>{{ ans.value }}</u>
                    </span>
                </div>
            </div>
        </div>

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
import { listProblem } from "@/api/lms/problem";

export default {
    name: "ProblemList",
    data() {
        return {
            problemList: [],
            // 显示搜索条件
            showSearch: true,
            typeOption: [
                {
                    label: "单选题",
                    value: "1",
                },
                {
                    label: "多选题",
                    value: "2",
                },
                {
                    label: "填空题",
                    value: "3",
                },
                {
                    label: "算法题",
                    value: "4",
                },
            ],
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                type: "",
            },
        };
    },
    methods: {
        goToAlgorithmProblem(row) {
            this.$router.push({
                path: "/problem/algorithm",
                query: { id: row.problemId },
            });
        },
        getList() {
            console.log(this.queryParams);

            listProblem(this.queryParams).then((response) => {
                this.problemList = response.rows;
                this.total = response.total;
                // console.log(this.problemList);
            });
        },
    },
    created() {},
};
</script>

<style scoped lang="scss">
.content {
    position: relative;
    margin-left: 20px;
}
.blankAns {
    display: block;
}
</style>
