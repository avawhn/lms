<template>
    <div class="app-container">
        <el-form :model="form" label-width="80" ref="form" :rules="rules">
            <el-form-item label="标题" prop="title" v-if="form.type && form.type !== '4'">
                <el-input v-model="form.title" style="width: 300px" />
            </el-form-item>

            <el-form-item label="题目类型" prop="type">
                <el-select
                    v-model="form.type"
                    placeholder="请选择题目类型"
                    @change="handleTypeChange"
                >
                    <el-option label="单选" value="1" />
                    <el-option label="多选" value="2" />
                    <el-option label="填空" value="3" />
                    <el-option label="算法" value="4" />
                </el-select>
            </el-form-item>

            <el-form-item
                label="题目内容"
                prop="description"
                v-if="form.type && form.type === '4'"
            >
                <el-upload
                    ref="upload"
                    class="upload-demo"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :auto-upload="false"
                >
                    <template #trigger>
                        <el-button type="primary">选择xml文件</el-button>
                    </template>
                </el-upload>
            </el-form-item>

            <el-form-item
                label="题目内容"
                prop="description"
                v-if="form.type && form.type !== '4'"
            >
                <WangEditor v-model="form.description" />
            </el-form-item>

            <div v-if="form.type && form.type !== '4'">
                <el-row v-for="(item, index) in form.answerList">
                    <el-col :span="12">
                        <el-form-item
                            :key="item.key"
                            :label="'选项' + (index + 1)"
                            :rules="{
                                required: true,
                                message: '选项不能为空',
                                trigger: 'blur',
                            }"
                        >
                            <el-input
                                v-model="item.value"
                                style="width: 400px"
                            />
                        </el-form-item>
                    </el-col>

                    <el-col :span="2">
                        <el-button
                            type="danger"
                            icon="Delete"
                            @click="removeOption(item)"
                        ></el-button>
                    </el-col>

                    <el-col
                        :span="10"
                        v-if="form.type === '1' || form.type === '2'"
                    >
                        <el-form-item>
                            <el-radio-group v-model="item.correct">
                                <el-radio :label="true">正确</el-radio>
                                <el-radio :label="false">错误</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item>
                    <el-button @click="addOption">添加选项</el-button>
                </el-form-item>
            </div>

            <el-form-item>
                <el-button type="primary" @click="submitAddProblemForm"
                    >保存</el-button
                >
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import WangEditor from "@/components/Editor";
import { addProblem } from "@/api/lms/problem";
import { ElMessage } from "element-plus";

export default {
    name: "ProblemAdd",
    components: { WangEditor },
    data() {
        return {
            form: {},
            rules: {
                title: [
                    {
                        required: true,
                        message: "标题不能为空",
                        trigger: "blur",
                    },
                ],
                description: [
                    {
                        required: true,
                        message: "题目内容不能为空",
                        trigger: "blur",
                    },
                ],
                type: [
                    {
                        required: true,
                        message: "题目类型不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        addOption() {
            this.form.answerList.push({
                value: null,
                correct: this.form.type === "3",
            });
        },
        removeOption(row) {
            this.form.answerList = this.form.answerList.filter(
                (item) => item !== row
            );
        },
        handleTypeChange() {
            const type = this.form.type ? this.form.type : "";
            const title = this.form.title ? this.form.title : "";
            const description = this.form.description
                ? this.form.description
                : "";

            this.resetForm();

            this.form.type = type;
            this.form.title = title;
            this.form.description = description;
        },
        resetForm() {
            this.form = {
                title: "",
                description: "",
                type: "",
                input: "",
                output: "",
                sample_input: "",
                sample_output: "",
                spj: "",
                hint: "",
                source: "",
                inDate: "",
                answerList: [],
            };
        },

        validForm() {
            if (!this.form.type || this.form.type === "0") {
                return true;
            }

            let correctCount = 0;
            this.form.answerList.forEach((item) => {
                correctCount += item.correct ? 1 : 0;
            });

            if (correctCount === 0) {
                ElMessage({
                    message: "不能没有正确选项.",
                    type: "warning",
                });
                return false;
            }

            if (this.form.type === "1" && correctCount !== 1) {
                // 单选
                ElMessage({
                    message: "单选题有且仅有一个正确选项.",
                    type: "warning",
                });
                return false;
            }

            return true;
        },
        submitAddProblemForm() {
            console.log(this.form);

            if (!this.validForm()) {
                return;
            }

            this.$refs["form"].validate((valid) => {
                if (valid) {
                }
            });

            addProblem(this.form).then((response) => {
                console.log(response);

                ElMessage({
                    message: "添加成功.",
                    type: "success",
                });

                this.resetForm();
            });
        },
    },
    created() {
        this.resetForm();
    },
};
</script>
