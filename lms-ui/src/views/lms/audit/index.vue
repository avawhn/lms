<template>
    <div class="app-container">
        <div class="myForm">
            <el-form
                ref="formRef"
                :model="form"
                :rules="formRules"
                label-width="80px"
            >
                <el-row v-for="item in fieldList">
                    <el-form-item :label="item.label" :prop="item.label">
                        <component
                            :is="item.componentName"
                            :placeholder="item.placeholder"
                            v-model="form[item.label]"
                            prefix="/audit/"
                        ></component>
                    </el-form-item>
                </el-row>

                <el-row>
                    <el-form-item label="审核人员">
                        <div class="addAudit">
                            <div
                                v-for="item in auditors"
                                style="display: inline"
                            >
                                <div
                                    class="auditor"
                                    @mouseenter="showDeleteIcon(item)"
                                    @mouseleave="hideDeleteIcon(item)"
                                >
                                    <div class="overlay" v-if="item.foc"></div>
                                    <div
                                        class="removeAuditor"
                                        @click="removeAuditor(item)"
                                        v-if="item.foc"
                                    >
                                        <el-icon><Delete /></el-icon>
                                    </div>

                                    <span>{{ item.nickName }}</span>
                                </div>
                                <img
                                    src="@/assets/images/arrow.png"
                                    class="arrow"
                                />
                            </div>

                            <img
                                src="@/assets/images/arrow.png"
                                class="arrow"
                                v-show="auditors.length === 0"
                            />

                            <el-button
                                type="primary"
                                icon="Plus"
                                @click="handleAddAudit"
                            />
                        </div>
                    </el-form-item>
                </el-row>

                <el-row>
                    <el-col :span="4"> </el-col>

                    <el-col :span="8">
                        <el-button type="primary" plain @click="submitForm"
                            >提 交</el-button
                        >
                    </el-col>
                </el-row>
            </el-form>
        </div>

        <el-dialog v-model="addAuditOpen" title="添加审核人员" width="500">
            <el-form
                :model="addAuditForm"
                ref="addAuditForm"
                :rules="addAuditRules"
            >
                <el-form-item label="姓名" label-width="60px" prop="userId">
                    <el-select
                        v-model="addAuditForm.userId"
                        placeholder="请选择审核人员"
                        style="width: 240px"
                    >
                        <el-option
                            v-for="item in auditOption"
                            :key="item.userId"
                            :label="item.nickName"
                            :value="item.userId"
                            :disabled="auditMemberIsAvail(item)"
                        />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addAuditOpen = false">取消</el-button>
                    <el-button type="primary" @click="submitAddAuditMember">
                        添加
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import UploadFileComp from "@/components/UploadFileComp";
import InputNumber from "@/components/InputNumber";
import DatePicker from "@/components/DatePicker";
import TextArea from "@/components/TextArea";

import { getFieldByTemplateId } from "@/api/lms/field";
import { getAllUser } from "@/api/system/user";
import useUserStore from "@/store/modules/user";
import { ElMessage } from "element-plus";

import { addRecord } from "@/api/lms/audit-record";

export default {
    name: "AuditDetail",
    components: { UploadFileComp, InputNumber, DatePicker, TextArea },
    setup() {
        const userStore = useUserStore();
        return { userStore };
    },
    data() {
        return {
            userId: this.userStore.userId,
            auditors: [],
            auditOption: [],
            addAuditOpen: false,
            fieldList: null,
            templateId: null,
            form: {},
            addAuditForm: {},
            formRules: {},
            addAuditRules: {
                userId: [
                    {
                        required: true,
                        message: "审核人员不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        hideDeleteIcon(item) {
            item.foc = false;
        },
        showDeleteIcon(item) {
            item.foc = true;
        },
        removeAuditor(user) {
            this.auditors = this.auditors.filter((item) => {
                return item.userId !== user.userId;
            });
        },
        submitAddAuditMember() {
            this.addAuditOpen = false;
            this.$refs["addAuditForm"].validate((valid) => {
                if (valid) {
                    const user = this.auditOption.find(
                        (e) => e.userId === this.addAuditForm.userId
                    );
                    user.foc = false;
                    this.auditors.push(user);
                }
            });
        },
        handleAddAudit() {
            this.addAuditForm = {
                userId: null,
            };
            this.addAuditOpen = true;
        },
        auditMemberIsAvail(user) {
            if (this.userId === user.userId) {
                return true;
            }
            return this.auditors.find((item) => item.userId === user.userId)
                ? true
                : false;
        },
        resetForm() {
            for (const key in this.form) {
                this.form[key] = null;
            }
        },
        submitForm() {
            this.$refs["formRef"].validate((valid) => {
                if (valid) {
                    if (this.auditors.length === 0) {
                        ElMessage({
                            message: "审核人员不能为空.",
                            type: "warning",
                        });
                        return;
                    }

                    this.form.createUserId = this.userId;
                    this.form.templateId = this.templateId;

                    this.form.detail = [];
                    this.fieldList.forEach((item) => {
                        const val = this.form[item.label];
                        if (val) {
                            this.form.detail.push({
                                label: item.label,
                                value: val,
                            });
                        }
                    });

                    this.form.auditorIds = this.auditors
                        .map((item) => item.userId)
                        .join(",");

                    addRecord(this.form).then((res) => {
                        ElMessage({
                            message: "申请成功.",
                            type: "success",
                        });

                        this.resetForm();
                        this.auditors = [];
                    });
                }
            });
        },
    },
    created() {
        this.templateId = this.$route.query.id;
        getFieldByTemplateId(this.templateId).then((response) => {
            this.fieldList = response.data;
            this.fieldList.forEach((e) => {
                if (e.required) {
                    this.formRules[e.label] = [
                        {
                            required: true,
                            message: e.label + "不能为空",
                            trigger: "blur",
                        },
                    ];
                }
            });
        });

        // 获取人员列表
        getAllUser().then((response) => {
            this.auditOption = response.data;
        });
    },
};
</script>

<style scoped lang="scss">
.myForm {
    width: 800px;
    height: 100%;
    margin-left: auto;
    margin-right: auto;
    border: #66b1ff solid;

    padding: 10px 10px 10px 10px;
}

.addAudit {
    vertical-align: middle;

    .arrow {
        height: 20px;
        vertical-align: middle;
        margin-right: 10px;
    }
}

.auditor {
    display: inline-flex;
    margin-right: 10px;
    background-color: #409eff;
    border-radius: 5px;
    color: #ffffff;
    vertical-align: middle;
    text-align: center;
    justify-content: center;
    position: relative;

    span {
        padding: 0px 10px;
    }

    .overlay {
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
    }

    .removeAuditor {
        position: absolute;
        margin-left: auto;
        margin-right: auto;
    }
}
</style>
