<template>
    <div class="app-container">
        <el-form
            ref="formRef"
            style="max-width: 800px"
            :model="form"
            :rules="rules"
            label-width="auto"
            class="demo-ruleForm"
            status-icon
        >
            <el-form-item label="测试题目" prop="title">
                <el-input v-model="form.title" style="width: 300px" />
            </el-form-item>

            <el-form-item label="比赛描述" prop="description">
                <el-input
                    v-model="form.description"
                    style="width: 500px"
                    :autosize="{ minRows: 4, maxRows: 8 }"
                    type="textarea"
                    placeholder="请输入比赛描述"
                />
            </el-form-item>

            <el-form-item label="开始时间" prop="startTime">
                <el-date-picker
                    v-model="form.startTime"
                    type="datetime"
                    placeholder="请选择开始时间"
                    value-format="YYYY-MM-DD hh:mm:ss"
                />
            </el-form-item>

            <el-form-item label="结束时间" prop="endTime">
                <el-date-picker
                    v-model="form.endTime"
                    type="datetime"
                    placeholder="请选择结束时间"
                    value-format="YYYY-MM-DD hh:mm:ss"
                />
            </el-form-item>

            <el-form-item label="参赛人员" prop="userIdArr">
                <el-select
                    v-model="form.userIdArr"
                    placeholder="请选择参加人员"
                    filterable
                    clearable
                    multiple
                    collapse-tags
                    style="width: 240px"
                >
                    <el-option
                        v-for="item in userOption"
                        :key="item.userId"
                        :label="item.nickName"
                        :value="item.userId"
                    />
                </el-select>
            </el-form-item>
            <div class="selectedUser">
                {{ selectedUser }}
            </div>

            <el-form-item label="测试题目" prop="problemIds">
                <el-button type="primary" plain @click="handleAddProblem"
                    >添加题目</el-button
                >

                <el-table
                    :data="problemList"
                    @selection-change="handleSelectionChange"
                    row-key="problemId"
                >
                    <el-table-column
                        type="selection"
                        width="55"
                        align="center"
                        :reserve-selection="true"
                    />
                    <el-table-column
                        label="题目编号"
                        align="center"
                        prop="problemId"
                    />
                    <el-table-column label="标题" align="center" prop="title" />
                    <el-table-column
                        label="时间限制"
                        align="center"
                        prop="timeLimit"
                    />
                    <el-table-column
                        label="内存限制"
                        align="center"
                        prop="memoryLimit"
                    />
                    <el-table-column
                        label="来源"
                        align="center"
                        prop="source"
                    />
                    <el-table-column label="类型" align="center" prop="type" />
                    <!-- <el-table-column label="设置分数" width="180">
                        <template #default="scope">
                            <el-input-number
                                v-model="scope.row.score"
                                :min="1"
                                :max="100"
                                style="width: 120px"
                            />
                        </template>
                    </el-table-column> -->
                </el-table>
                <pagination
                    v-show="problemTotal > 0"
                    :total="problemTotal"
                    v-model:page="problemQueryParams.pageNum"
                    v-model:limit="problemQueryParams.pageSize"
                    @pagination="getProblemList"
                />
            </el-form-item>

            <div style="text-align: center">
                <el-button type="primary" plain @click="submitAddContest">发布测试</el-button>
            </div>
        </el-form>

        <!-- 添加或修改问题对话框 -->
        <el-dialog
            title="添加问题"
            v-model="addProblemOpen"
            width="500px"
            append-to-body
        >
            <el-form ref="addProblemForm" :model="form">
                <h3 style="margin: 0px 0px 10px 0px; padding: 0px">
                    总分数：{{ totalScore }}
                </h3>
                <el-form-item :label="item.title" v-for="item in problemArr">
                    <span style="margin-right: 5px">分数：</span>
                    <el-input-number v-model="item.score" :min="1" :max="100" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="addProblem">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { getAllUser } from "@/api/system/user";
import { listProblem } from "@/api/lms/problem";
import { ElMessage } from "element-plus";
import { addContest } from "@/api/lms/contest";

export default {
    name: "PublishTest",
    components: {},
    setup() {},
    data() {
        return {
            addProblemOpen: false,
            userOption: [],
            problemList: [],
            problemIds: [],
            problemArr: [],
            problemTotal: 0,
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 查询参数
            problemQueryParams: {
                pageNum: 1,
                pageSize: 10,
                type: "",
            },
            form: {},
            rules: {
                title: [
                    {
                        required: true,
                        message: "请输入测试题目",
                        trigger: "blur",
                    },
                ],
                startTime: [
                    {
                        required: true,
                        message: "请输入开始时间",
                        trigger: "blur",
                    },
                ],
                endTime: [
                    {
                        required: true,
                        message: "请输入开始时间",
                        trigger: "blur",
                    },
                ],
                description: [
                    {
                        required: true,
                        message: "比赛描述不能为空",
                        trigger: "blur",
                    },
                ],
                userIdArr: [
                    {
                        required: true,
                        message: "参加比赛人员不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        submitAddContest() {
            this.$refs["formRef"].validate((valid) => {
                if (valid) { 
                    if (!this.form.problemList) {
                        ElMessage({
                            message: '试卷题目不能为空.',
                            type: 'warning',
                        });
                        return;
                    }
                    this.form.userId = this.form.userIdArr.join(",");
                    // console.log(this.form);
                    addContest(this.form).then(response => {
                        ElMessage({
                            message: '添加成功.',
                            type: 'success',
                        });
                    });
                }
            });
        },
        addProblem() {
            this.form.problemList = this.problemArr.map((item) => {
                return { problemId: item.problemId, score: item.score };
            });
            this.addProblemOpen = false;
        },
        cancel() {
            this.addProblemOpen = false;
        },
        handleAddProblem() {
            if (this.problemArr.length <= 0) {
                ElMessage({
                    message: "请先选择题目.",
                    type: "warning",
                });
                return;
            }
            this.addProblemOpen = true;
        },
        getProblemType(type) {
            if (type === "0") {
                return "算法题";
            } else if (type === "1") {
                return "单选题";
            } else if (type === "2") {
                return "多选题";
            } else if (type === "3") {
                return "填空题";
            } else {
                return "";
            }
        },
        getProblemList() {
            listProblem(this.problemQueryParams).then((response) => {
                console.log(this.problemArr);
                response.rows.forEach((r) => {
                    r.type = this.getProblemType(r.type);
                    const problem = this.problemArr.find(
                        (item) => item.problemId === r.problemId
                    );
                    if (problem) {
                        r.score = problem.score;
                    }
                });

                this.problemList = response.rows;
                this.problemTotal = response.total;
            });
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            console.log(selection);
            this.problemArr = selection.map((item) => {
                if (item.score === undefined) {
                    item.score = 1;
                }
                return item;
                // const data = {
                //     problemId: item.problemId,
                //     score: item.score === undefined ? 1 : item.score,
                // };
                // return data;
                // return item.problemId;
            });
            this.single = selection.length !== 1;
            this.multiple = !selection.length;
        },
    },
    created() {
        getAllUser().then((response) => {
            this.userOption = response.data;
        });
        this.getProblemList();
    },
    computed: {
        totalScore() {
            let total = 0;
            this.problemArr.forEach((p) => (total += p.score));
            return total;
        },
        // 一个计算属性的 getter
        selectedUser() {
            const userList = this.userOption.filter((user) =>
                this.form.userIdArr.includes(user.userId)
            );
            return userList.map((item) => item.nickName).join(",");
        },
        selectedProblem() {
            return this.problemArr.map((item) => item.title).join(",");
        },
    },
};
</script>

<style scoped lang="scss">
.selectedUser {
    margin-left: 80px;
    margin-bottom: 10px;
}

.selectedProblem {
    margin-left: 10px;
    margin-bottom: 10px;
    display: block;
}
</style>
