<template>
    <div class="app-container">
        <div class="left-contaienr">
            <div class="wrapper">
                <h4>标题</h4>
                <div class="problem-content" v-html="problem.description" />
            </div>

            <div class="wrapper">
                <h4>样例输入</h4>
                <div class="problem-content">
                    <pre style="margin-top: 0; margin-bottom: 0">
<code v-html="problem.sampleInput"/>
</pre>
                </div>
            </div>

            <div class="wrapper">
                <h4>样例输出</h4>
                <div class="problem-content">
                    <pre style="margin-top: 0; margin-bottom: 0">
<code v-html="problem.sampleInput"/>
</pre>
                </div>
            </div>

            <div class="wrapper">
                <h4>提示</h4>
                <div class="problem-content" v-html="problem.hint" />
            </div>
        </div>

        <div class="right-container">
            <div class="head">
                Problem {{ problem.problemId }} Language:
                <el-select
                    v-model="form.language"
                    placeholder="请选择语言"
                    size="small"
                    style="width: 100px"
                    :fit-input-width="true"
                    @change="languageChange"
                >
                    <el-option
                        v-for="item in languageOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>

                <el-button
                    type="primary"
                    plain
                    @click="submitProblem"
                    size="small"
                    >{{ btnTxt }}</el-button
                >
            </div>
            <div v-if="solution.solutionId">
                <span
                    >状态: {{ solution.msg }} Memory: {{ solution.memory }} KB
                    Time: {{ solution.time }} ms</span
                >
            </div>
            <v-ace-editor
                v-model:value="form.sourceCode.source"
                :lang="language"
                theme="chrome"
                :options="options"
                class="vue-ace-editor"
            />
        </div>
    </div>
</template>

<script>
import { getProblemById } from "@/api/lms/problem";
import { VAceEditor } from "vue3-ace-editor";
import "@/assets/aceConfig.js";
import {
    listSolution,
    getSolutionBySolutionId,
    getSolutionDetailBySolutionId,
    addSolution,
} from "@/api/lms/solution";
import { getSourceCodeBySolutionId } from "@/api/lms/sourceCode";
import { ElMessage } from "element-plus";

export default {
    name: "ProblemAlgorithm",
    components: { VAceEditor },
    data() {
        return {
            problemId: null,
            language: "c_cpp",
            btnTxt: "提交",
            problem: {},
            form: {
                language: 0,
                problemId: null,
                sourceCode: {},
            },
            solution: {},
            languageOptions: [
                {
                    label: "C",
                    value: 0,
                },
                {
                    label: "C++",
                    value: 1,
                },
                {
                    label: "Java",
                    value: 3,
                },
                {
                    label: "Python",
                    value: 6,
                },
            ],
            options: {
                useWorker: true, // 启用语法检查,必须为true
                enableBasicAutocompletion: true, // 自动补全
                enableLiveAutocompletion: true, // 智能补全
                enableSnippets: true, // 启用代码段
                showPrintMargin: false, // 去掉灰色的线，printMarginColumn
                highlightActiveLine: true, // 高亮行
                highlightSelectedWord: true, // 高亮选中的字符
                tabSize: 4, // tab锁进字符
                fontSize: 14, // 设置字号
                wrap: false, // 是否换行
                readonly: false, // 是否可编辑
                // minLines: 10, // 最小行数，minLines和maxLines同时设置之后，可以不用给editor再设置高度
                // maxLines: 50, // 最大行数
            },
        };
    },
    methods: {
        languageChange() {
            const language = this.form.language;
            this.setLanguage(language);
        },

        setLanguage(language) {
            if (language === 0 || language === 1) {
                this.language = "c_cpp";
            } else if (language === 3) {
                this.language = "java";
            } else if (language === 6) {
                this.language = "python";
            }
        },
        submitProblem() {
            this.btnTxt = "提交中";
            addSolution(this.form).then((response) => {
                this.btnTxt = "测试中";
                const solutionId = response.data;
                setTimeout(() => {
                    getSolutionBySolutionId(solutionId).then((res) => {
                        this.solution = res.data;
                        console.log(this.solution);
                        this.btnTxt = "提交";
                    });
                }, 2000);
            });
        },
    },
    created() {
        this.problemId = this.$route.query.id;
        this.form.problemId = this.problemId;

        getProblemById(this.problemId).then((response) => {
            this.problem = response.data;
            console.log(this.problem);
        });

        const solutionId = this.$route.query.solutionId;
        if (solutionId) {
            getSolutionDetailBySolutionId(solutionId).then((res) => {
                this.form.sourceCode.source = res.data.sourceCode.source;
                
            });
        }
    },
};
</script>

<style lang="scss" scoped>
.vue-ace-editor {
    /* ace-editor默认没有高度，所以必须设置高度，或者同时设置最小行和最大行使编辑器的高度自动增高 */
    height: 300px;
    width: 500px;
    font-size: 16px;
    border: 1px solid;

    margin-left: auto;
    margin-right: auto;
}

.app-container {
    display: flex;
    height: 100vh;

    .left-contaienr {
        width: 50%;
        height: 100%;
        overflow-y: auto;

        text-align: center;
        flex: 0 0 auto;
    }

    .wrapper {
        width: 570px;
        margin: 10px auto 10px auto;
        border: #d4d4d5 solid;
        border-radius: 3px;

        h4 {
            margin: 0px;
            padding: 10px;
            background: #f3f4f5;
            text-align: left;
        }

        .problem-content {
            width: 100%;
            text-align: left;
            :deep(img) {
                max-width: 100%;
                height: auto;
            }

            pre {
                tab-size: 4;
                overflow: auto;

                display: block;
                padding: 9.5px;
                margin: 0 0 10px;

                font-size: 13px;
                font-family: monospace, monospace;
                line-height: 1.42857143;

                color: #333;

                word-break: break-all;
                word-wrap: break-word;

                background-color: #f5f5f5;
                border: 1px solid #ccc;
                border-radius: 4px;
            }

            code {
                font-family: "Consolas", "Ubuntu Mono", "Fira Mono", "Monaco",
                    "Menlo", "source-code-pro", monospace !important;
            }

            pre code {
                padding: 0;
                font-size: inherit;
                color: inherit;
                white-space: pre-wrap;
                background-color: transparent;
                border-radius: 0;
                display: block;
            }
        }
    }

    .right-container {
        flex: 1 1 auto;
        height: 100%;
        overflow-y: auto;
        text-align: center;

        .head {
            height: 30px;
            text-align: center;
            vertical-align: middle;
        }
    }
}
</style>
