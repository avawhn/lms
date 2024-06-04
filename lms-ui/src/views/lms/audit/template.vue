<template>
    <div class="app-container">
        <div class="myForm">
            <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                    <el-button
                        type="primary"
                        plain
                        icon="Plus"
                        size="default"
                        @click="handleAddField"
                        >添加字段</el-button
                    >
                </el-col>
            </el-row>
            <el-form
                ref="formRef"
                :model="form"
                :rules="formRules"
                label-width="80px"
            >
                <el-form-item label="模板名称" prop="name">
                    <el-input
                        v-model="form.name"
                        type="text"
                        placeholder="请输入模板名称"
                        style="width: 200px"
                    />
                </el-form-item>

                <el-form-item label="模板描述" prop="description">
                    <el-input
                        v-model="form.description"
                        type="textarea"
                        placeholder="请输入模板详细信息"
                        style="width: 400px"
                    />
                </el-form-item>

                <el-form-item label="菜单图标" prop="icon">
                    <el-popover
                        placement="bottom-start"
                        :width="540"
                        v-model:visible="showChooseIcon"
                        trigger="click"
                        @show="showSelectIcon"
                    >
                        <template #reference>
                            <el-input
                                v-model="form.image"
                                placeholder="点击选择图标"
                                @blur="showSelectIcon"
                                v-click-outside="hideSelectIcon"
                                style="width: 200px"
                                readonly
                            >
                                <template #prefix>
                                    <svg-icon
                                        v-if="form.image"
                                        :icon-class="form.image"
                                        class="el-input__icon"
                                        style="height: 32px; width: 16px"
                                    />
                                    <el-icon
                                        v-else
                                        style="height: 32px; width: 16px"
                                        ><search
                                    /></el-icon>
                                </template>
                            </el-input>
                        </template>
                        <icon-select ref="iconSelectRef" @selected="selected" />
                    </el-popover>
                </el-form-item>

                <!-- <el-form-item label="图片" prop="image">
                    <el-upload
                        v-model:file-list="fileList"
                        class="upload-demo"
                        :on-remove="handleRemove"
                        :limit="1"
                        :on-exceed="handleExceed"
                        :http-request="handleUploadFile"
                    >
                        <el-button type="primary">上传图片</el-button>
                        <template #tip>
                            <div class="el-upload__tip">
                                jpg/png files with a size less than 500KB.
                            </div>
                        </template>
                    </el-upload>
                </el-form-item> -->

                <el-row v-for="item in form.fieldList">
                    <el-col :span="15">
                        <el-form-item :label="item.label">
                            <component
                                :is="item.componentName"
                                :placeholder="item.placeholder"
                            ></component>
                        </el-form-item>
                    </el-col>

                    <el-col :span="1"> </el-col>

                    <el-col :span="8">
                        <el-button
                            type="danger"
                            icon="Delete"
                            circle
                            @click="deleteField(item)"
                        />
                    </el-col>
                </el-row>
            </el-form>

            <el-button
                type="primary"
                plain
                icon="Plus"
                size="default"
                @click="submitForm"
                >创建模板</el-button
            >
        </div>

        <!-- 添加字段对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form
                ref="fieldForm"
                :model="fieldForm"
                :rules="rules"
                label-width="80px"
            >
                <el-form-item label="名称" prop="label">
                    <el-input
                        v-model="fieldForm.label"
                        type="text"
                        placeholder="请输入名称"
                    />
                </el-form-item>

                <el-row>
                    <el-col :span="16">
                        <el-form-item label="类型" prop="type">
                            <el-select
                                v-model="fieldForm.componentName"
                                placeholder="请选择类型"
                            >
                                <el-option
                                    v-for="item in fieldTypeOption"
                                    :key="item.componentName"
                                    :label="item.label"
                                    :value="item.componentName"
                                >
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="是否必填">
                            <el-switch v-model="fieldForm.required" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="提示" prop="placeholder">
                    <el-input
                        v-model="fieldForm.placeholder"
                        type="text"
                        placeholder="请输入提示"
                    />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="addField">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import UploadFileComp from "@/components/UploadFileComp";
import InputNumber from "@/components/InputNumber";
import { genFileId, ElMessage,ClickOutside as vClickOutside } from "element-plus";
import { addTemplateV2 } from "@/api/lms/template";
import { getUploadPreSignedUrl, uploadFile, deleteFile } from "@/api/lms/minio";
import { getUuid } from "@/utils/uuid";
import SvgIcon from "@/components/SvgIcon";
import IconSelect from "@/components/IconSelect";

export default {
    name: "Template",
    components: { UploadFileComp, InputNumber ,SvgIcon,IconSelect},
    data() {
        return {
            iconSelectRef: null,
            showChooseIcon: false,
            fileList: [],
            // 弹出层标题
            title: "添加字段",
            // 是否显示弹出层
            open: false,
            fieldTypeOption: [
                {
                    label: "输入框",
                    componentName: "el-input",
                },
                {
                    label: "日期",
                    componentName: "el-date-picker",
                },
                {
                    label: "文件",
                    componentName: "UploadFileComp",
                },
                {
                    label: "数字",
                    componentName: "InputNumber",
                },
            ],
            // 表单参数
            form: {
                name: "",
                description: "",
                image: "",
                fieldList: [],
            },
            fieldForm: {
                label: "",
                placeholder: "",
                required: false,
            },
            formRules: {
                name: [
                    {
                        required: true,
                        message: "模板名称不能为空",
                        trigger: "blur",
                    },
                ],
            },
            // 表单校验
            rules: {
                label: [
                    {
                        required: true,
                        message: "名称不能为空",
                        trigger: "blur",
                    },
                ],
                type: [
                    {
                        required: true,
                        message: "类型不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    mounted() {},
    created() {},
    methods: {
        // ****************************************************************************************
        handleRemove(file, uploadFiles) {
            if (!this.form.image) {
                return;
            }
            deleteFile("lms", this.form.image);
        },

        handleExceed(files) {
            this.fileList = [];
            const file = files[0];
            file.uid = genFileId();
            this.fileList.push(file);
            deleteFile("lms", this.form.image).then((res) => {
                this.minioUploadFile(file);
            });
        },
        generateUuidFileName(prefix, file) {
            const filename = file.name;
            const dotIndex = filename.lastIndexOf(".");
            const extension = filename.substr(dotIndex + 1).toLowerCase();
            const newFilename = prefix + getUuid() + "." + extension;
            return newFilename;
        },
        minioUploadFile(file) {
            const newFilename = this.generateUuidFileName("/template/", file);
            getUploadPreSignedUrl("lms", newFilename).then((response) => {
                const url = response.data;
                uploadFile(url, file).then((res) => {
                    if (res.status === 200) {
                        this.form.image = newFilename;
                    } else {
                        ElMessage.error("上传失败.");
                    }
                });
            });
        },
        handleUploadFile(option) {
            const file = option.file;
            this.minioUploadFile(file);
        },

        // ****************************************************************************************
        /** 选择图标 */
        selected(name) {
            this.form.image = name;
            this.showChooseIcon = false;
        },
        /** 图标外层点击隐藏下拉列表 */
        hideSelectIcon(event) {
            var elem =
                event.relatedTarget ||
                event.srcElement ||
                event.target ||
                event.currentTarget;
            var className = elem.className;
            if (className !== "el-input__inner") {
                this.showChooseIcon = false;
            }
        },
        /** 展示下拉图标 */
        showSelectIcon() {
            this.iconSelectRef = null;
            this.showChooseIcon = true;
        },
        deleteField(item) {
            const index = this.form.fieldList.indexOf(item);
            if (index !== -1) {
                this.form.fieldList.splice(index, 1);
            }
        },
        submitForm() {
            if (this.form.fieldList.length === 0) {
                ElMessage({
                    message: "请先添加字段再进行创建.",
                    type: "warning",
                });
                return;
            }
            addTemplateV2(this.form).then((response) => {
                if (response.code === 200) {
                    ElMessage({
                        message: "添加成功.",
                        type: "success",
                    });
                    this.resetForm();
                }
            });
        },
        cancel() {
            this.open = false;
        },
        resetFieldForm() {
            this.fieldForm = {
                label: "",
                placeholder: "",
                required: false,
                componentName: "",
            };
        },
        resetForm() {
            this.form = {
                name: "",
                description: "",
                image: "",
                fieldList: [],
            };
        },
        handleAddField() {
            this.resetFieldForm();
            this.open = true;
        },
        addField() {
            const field = {
                ...this.fieldForm,
            };
            this.form.fieldList.push(field);
            this.open = false;
        },
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

    padding: 10px 10px;
}
</style>
