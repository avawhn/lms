<template>
    <el-upload
        v-model:file-list="fileList"
        class="upload-demo"
        :on-remove="handleRemove"
        :limit="limit"
        :on-exceed="handleExceed"
        :http-request="handleUploadFile"
    >
        <el-button type="primary">上传文件</el-button>
    </el-upload>
</template>

<script>
import { getUploadPreSignedUrl, uploadFile, deleteFile } from "@/api/lms/minio";
import { getUuid } from "@/utils/uuid";
import { genFileId, ElMessage } from "element-plus";

export default {
    name: "UploadFileComp",
    props: {
        // 图片名称
        modelValue: {
            type: String,
            default: "",
            // required: true,
        },
        limit: {
            type: Number,
            default: 10,
        },
        prefix: {
            type: String,
            default: "other",
        },
    },
    data() {
        return {
            fileList: [],
            fileNameList: [],
        };
    },
    methods: {
        handleRemove(file, uploadFiles) {
            if (!this.modelValue) {
                return;
            }
            deleteFile("lms", this.modelValue).then((res) => {
                this.fileNameList = this.fileNameList.filter(item => item.uid !== file.uid);
            });
        },
        handleExceed(files) {
            this.fileList = [];
            const file = files[0];
            file.uid = genFileId();
            this.fileList.push(file);
            deleteFile("lms", this.modelValue).then((res) => {
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
            const newFilename = this.generateUuidFileName(this.prefix, file);
            getUploadPreSignedUrl("lms", newFilename).then((response) => {
                const url = response.data;
                uploadFile(url, file).then((res) => {
                    if (res.status === 200) {
                        this.fileNameList.push({
                            uid: file.uid,
                            name: newFilename,
                        });
                        const newVal = this.fileNameList.map(item => item.name).join(",");
                        this.$emit("update:modelValue", newVal);
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
    watch: {
        modelValue(newV) {
            if (!newV) {
                this.fileList = [];
                this.fileNameList = [];
            }
        }
    }
};
</script>
