<template>
    <div class="app-container">
        <h1>{{ contest.title }}</h1>
        <CountdownTimer :contestTime="contest.contestTime" v-if="contest.contestTime" @timeEnd="timeEnd"/>

        <div v-for="(item, index) in contest.problemList">
            <div v-if="item.problem && item.problem.type === '1'">
                <div v-html="item.problem.description" />
                <el-radio-group v-model="form.ansList[index].ans[0]">
                    <el-radio
                        :label="option.value"
                        v-for="option in item.problem.answerList"
                        >{{ option.value }}</el-radio
                    >
                </el-radio-group>
            </div>

            <div v-if="item.problem && item.problem.type === '2'">
                <div v-html="item.problem.description" />
                <el-checkbox-group v-model="form.ansList[index].ans">
                    <el-checkbox
                        :label="option.value"
                        :value="option.value"
                        v-for="(option, oidx) in item.problem.answerList"
                    />
                </el-checkbox-group>
            </div>

            <div v-if="item.problem && item.problem.type === '3'">
                <div v-html="item.problem.description" />
                <div v-for="(option, oidx) in item.problem.answerList">
                    答案 {{ oidx + 1 }}：
                    <el-input
                        v-model="form.ansList[index].ans[oidx]"
                        style="width: 240px"
                        placeholder="请输入答案"
                    />
                </div>
            </div>

            <div v-if="item.problem && item.problem.type === '4'">
                <div class="wrapper">
                    <h4>描述</h4>
                    <div
                        class="problem-content"
                        v-html="item.problem.description"
                    />
                </div>

                <div class="wrapper">
                    <h4>样例输入</h4>
                    <div class="problem-content">
                        <pre style="margin-top: 0; margin-bottom: 0">
<code v-html="item.problem.sampleInput"/>
</pre>
                    </div>
                </div>

                <div class="wrapper">
                    <h4>样例输出</h4>
                    <div class="problem-content">
                        <pre style="margin-top: 0; margin-bottom: 0">
<code v-html="item.problem.sampleInput"/>
</pre>
                    </div>
                </div>

                <div class="wrapper">
                    <h4>提示</h4>
                    <div class="problem-content" v-html="item.problem.hint" />
                </div>

                <div>
                    <div class="head">
                        Problem {{ item.problem.problemId }} Language:
                        <el-select
                            v-model="form.ansList[index].language"
                            placeholder="请选择语言"
                            size="small"
                            style="width: 100px"
                            :fit-input-width="true"
                            @change="languageChange(index)"
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
                            @click="submitProblem(index)"
                            size="small"
                            >{{ btnTxt }}</el-button
                        >
                    </div>
                    <div v-if="solutionList[index].solutionId">
                        <span
                            >状态: {{ solutionList[index].msg }} Memory:
                            {{ solutionList[index].memory }} KB Time:
                            {{ solutionList[index].time }} ms</span
                        >
                    </div>
                    <v-ace-editor
                        v-model:value="form.ansList[index].ans[0]"
                        :lang="form.ansList[index].lang"
                        theme="chrome"
                        :options="options"
                        class="vue-ace-editor"
                    />
                </div>
            </div>
        </div>

        <div style="text-align: center">
            <el-button type="primary" plain @click="submitForm"
                >提交试卷</el-button
            >
        </div>
    </div>
</template>

<script>
import { getContestDetailByContestId, submit } from "@/api/lms/contest";
import { VAceEditor } from "vue3-ace-editor";
import "@/assets/aceConfig.js";
import {
    getSolutionBySolutionId,
    addSolution,
    testCode,
} from "@/api/lms/solution";
import CountdownTimer from "@/components/CountdownTimer";

export default {
    name: "Test",
    setup() {},
    components: { VAceEditor, CountdownTimer },
    data() {
        return {
            form: {
                contestId: null,
                ansList: [],
            },
            solutionList: [],
            btnTxt: "提交",
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
            contestId: null,
            contest: {},
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
        timeEnd() {
            this.submitForm();
        },
        getLanguage(language) {
            if (language === 0 || language === 1) {
                return "c_cpp";
            } else if (language === 3) {
                return "java";
            } else if (language === 6) {
                return "python";
            } else {
                return "";
            }
        },
        submitProblem(index) {
            // console.log(this.form.ansList[index]);
            this.btnTxt = "有题目正在提交";
            testCode(this.form.ansList[index]).then((response) => {
                this.btnTxt = "有题目正在测试";
                const solutionId = response.data;
                setTimeout(() => {
                    getSolutionBySolutionId(solutionId).then((res) => {
                        if (res.data.result < 4) {
                            setTimeout(() => {
                                getSolutionBySolutionId(solutionId).then(
                                    (res) => {
                                        if (res.data.result < 4) {
                                            return;
                                        }
                                        this.solutionList[index] = res.data;
                                        console.log(this.solutionList[index]);
                                        this.btnTxt = "提交";
                                    }
                                );
                            }, 2000);
                            return;
                        }
                        this.solutionList[index] = res.data;
                        console.log(this.solutionList[index]);
                        this.btnTxt = "提交";
                    });
                }, 2000);
            });
        },
        submitForm() {
            // console.log(this.form);
            submit(this.form).then((response) => {
                this.$router.push({ path: "/contest/mytest" });
            });
        },
        languageChange(index) {
            const lang = this.getLanguage(this.form.ansList[index].language);
            this.form.ansList[index].lang = lang;
        },
    },
    created() {
        this.contestId = this.$route.query.contestId;
        getContestDetailByContestId(this.contestId).then((response) => {
            this.contest = response.data;
            this.form.contestId = this.contest.contestId;

            this.contest.problemList.forEach((p) => {
                this.solutionList.push({});

                const data = {
                    problemId: p.problemId,
                    type: p.problem.type,
                    language: 0,
                    lang: "c_cpp",
                    ans: [],
                };

                if (data.type === "3") {
                    for (let i = 0; i < p.problem.answerList.length; i++) {
                        data.ans.push("");
                    }
                }
                this.form.ansList.push(data);
            });
            console.log("contest: ", this.contest);
            // console.log("form: ", this.form);
        });
    },
};
</script>

<style scoped lang="scss">
.app-container {
    .vue-ace-editor {
        /* ace-editor默认没有高度，所以必须设置高度，或者同时设置最小行和最大行使编辑器的高度自动增高 */
        height: 300px;
        width: 500px;
        font-size: 16px;
        border: 1px solid;
    }
    h1 {
        text-align: center;
        margin: 0px;
        padding: 0px;
    }

    .wrapper {
        width: 570px;
        margin: 10px 0px 10px 0px;
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
}
</style>
