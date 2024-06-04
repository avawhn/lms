<template>
    <div class="app-container">
        <el-form
            ref="formRef"
            style="max-width: 600px"
            :model="form"
            :rules="rules"
            class="demo-ruleForm"
            status-icon
        >
            <el-form-item label="实验室名称" prop="name">
                <el-input v-model="form.name" style="width: 300px" />
            </el-form-item>

            <el-form-item label="实验室介绍" prop="description">
                <el-input
                    v-model="form.description"
                    style="width: 700px"
                    :autosize="{ minRows: 4, maxRows: 8 }"
                    type="textarea"
                    placeholder="请输入实验室介绍"
                />
            </el-form-item>

            <el-form-item label="实验室图片">
                <el-upload
                    v-model:file-list="fileList"
                    list-type="picture-card"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleRemove"
                    :http-request="handleUploadFile"
                >
                    <el-icon><Plus /></el-icon>
                </el-upload>
            </el-form-item>
        </el-form>

        <div style="margin-left: 200px">
            <el-button type="primary" plain @click="submitForm">保存</el-button>
        </div>
        <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>
    </div>
</template>

<script>
import {
    getUploadPreSignedUrl,
    uploadFile,
    deleteFile,
    getPreviewUrl,
} from "@/api/lms/minio";
import { getUuid } from "@/utils/uuid";
import { genFileId, ElMessage } from "element-plus";
import {
    getLaboratoryInfo,
    addLaboratoryImage,
    delLaboratoryInfo,
    updateLaboratoryInfo,
} from "@/api/lms/laboratoryInfo";
export default {
    name: "LaboratoryManage",
    data() {
        return {
            dialogImageUrl: "",
            dialogVisible: false,
            fileList: [],
            rules: {
                name: [
                    {
                        required: true,
                        message: "请输入实验室名称",
                        trigger: "blur",
                    },
                ],
                description: [
                    {
                        required: true,
                        message: "请输入实验室介绍",
                        trigger: "blur",
                    },
                ],
            },
            form: {},
        };
    },
    methods: {
        submitForm() {
            updateLaboratoryInfo(this.form).then((res) => {
                ElMessage({
                    message: "保存成功.",
                    type: "success",
                });
            });
        },
        handlePictureCardPreview(uploadFile) {
            this.dialogImageUrl = uploadFile.url;
            this.dialogVisible = true;
        },

        handleRemove(file, uploadFiles) {
            console.log("remove file: ", file);
            let name = null;
            let id = null;
            if (file.raw) {
                name = file.raw.newName;
                id = file.raw.id;
            } else {
                name = file.name;
                id = file.id;
            }

            deleteFile("lms", name).then((res) => {});
            delLaboratoryInfo(id).then((res) => {});
        },
        generateUuidFileName(prefix, file) {
            const filename = file.name;
            const dotIndex = filename.lastIndexOf(".");
            const extension = filename.substr(dotIndex + 1).toLowerCase();
            const newFilename = prefix + getUuid() + "." + extension;
            return newFilename;
        },
        minioUploadFile(file) {
            const newFilename = this.generateUuidFileName("/laboratory/", file);
            file.newName = newFilename;
            getUploadPreSignedUrl("lms", newFilename).then((response) => {
                const url = response.data;
                uploadFile(url, file).then((res) => {
                    if (res.status === 200) {
                        const data = {
                            value: newFilename,
                        };
                        addLaboratoryImage(data).then((res) => {
                            file.id = res.data;
                        });
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
    },
    created() {
        getLaboratoryInfo().then((res) => {
            res.data.imageList.forEach((r) => {
                getPreviewUrl("lms", r.value).then(async (res) => {
                    const url = res.data;
                    this.fileList.push({
                        id: r.id,
                        name: r.value,
                        url: url,
                    });
                });
            });
            this.form = res.data;
        });
    },
};
</script>
