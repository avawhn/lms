<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            size="small"
            :inline="true"
            v-show="showSearch"
        >
            <!-- <el-form-item label="图片路径" prop="url">
                <el-input
                    v-model="queryParams.url"
                    placeholder="请输入图片路径"
                    clearable
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item> -->
            <el-form-item label="年级" prop="grade">
                <el-input
                    v-model="queryParams.grade"
                    placeholder="请输入年级"
                    clearable
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button
                    type="primary"
                    icon="Search"
                    size="small"
                    @click="handleQuery"
                    >搜索</el-button
                >
                <el-button icon="Refresh" size="small" @click="resetQuery"
                    >重置</el-button
                >
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    plain
                    icon="Plus"
                    size="small"
                    @click="handleAdd"
                    v-hasPermi="['lms:video:add']"
                    >新增</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="success"
                    plain
                    icon="Edit"
                    size="small"
                    :disabled="single"
                    @click="handleUpdate"
                    v-hasPermi="['lms:video:edit']"
                    >修改</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    plain
                    icon="Delete"
                    size="small"
                    :disabled="multiple"
                    @click="handleDelete"
                    v-hasPermi="['lms:video:remove']"
                    >删除</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="warning"
                    plain
                    icon="Download"
                    size="small"
                    @click="handleExport"
                    v-hasPermi="['lms:video:export']"
                    >导出</el-button
                >
            </el-col>
            <right-toolbar
                :showSearch.sync="showSearch"
                @queryTable="getList"
            ></right-toolbar>
        </el-row>

        <el-table
            v-loading="loading"
            :data="videoList"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="年级" align="center" prop="grade" />
            <el-table-column label="存储路径" align="center" prop="url" />
            <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="danger"
                        icon="Delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:video:remove']"
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

        <!-- 添加或修改比赛视频对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="年级" prop="grade">
                    <el-input v-model="form.grade" placeholder="请输入年级" />
                </el-form-item>
                <el-form-item label="比赛视频">
                    <el-upload
                        ref="uploadRef"
                        class="upload-demo"
                        :auto-upload="false"
                        :limit="1"
                        :http-request="handleUploadFile"
                        :on-exceed="handleExceed"
                    >
                        <template #trigger>
                            <el-button type="primary">选择视频</el-button>
                        </template>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listVideo,
    getVideo,
    delVideo,
    addVideo,
    updateVideo,
} from "@/api/lms/video";
import {
    getUploadPreSignedUrl,
    uploadFile,
    deleteFile,
    getPreviewUrl,
} from "@/api/lms/minio";
import { getUuid } from "@/utils/uuid";
import { genFileId, ElMessage } from "element-plus";

export default {
    name: "Video",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 比赛视频表格数据
            videoList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                url: null,
                grade: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                url: [
                    {
                        required: true,
                        message: "图片路径不能为空",
                        trigger: "blur",
                    },
                ],
                grade: [
                    {
                        required: true,
                        message: "年级不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    created() {
        this.getList();
    },
    methods: {
        // *********************************************************************************
        handleExceed(files) {
            this.$refs["uploadRef"].clearFiles();
            const file = files[0];
            file.uid = genFileId();
            this.$refs["uploadRef"].handleStart(file);
        },
        generateUuidFileName(prefix, file) {
            const filename = file.name;
            const dotIndex = filename.lastIndexOf(".");
            const extension = filename.substr(dotIndex + 1).toLowerCase();
            const newFilename = prefix + getUuid() + "." + extension;
            return newFilename;
        },
        minioUploadFile(file) {
            const newFilename = this.generateUuidFileName(
                "/competition/",
                file
            );
            this.form.url = newFilename;
            getUploadPreSignedUrl("lms", newFilename).then((response) => {
                const url = response.data;
                uploadFile(url, file).then((res) => {
                    if (res.status === 200) {
                        addVideo(this.form).then((response) => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
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
        // *********************************************************************************
        submitUpload() {
            this.$refs["uploadRef"].submit();
        },
        /** 查询比赛视频列表 */
        getList() {
            this.loading = true;
            listVideo(this.queryParams).then((response) => {
                this.videoList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                id: null,
                url: null,
                grade: null,
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map((item) => item.id);
            this.single = selection.length !== 1;
            this.multiple = !selection.length;
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加比赛视频";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids;
            getVideo(id).then((response) => {
                this.form = response.data;
                this.open = true;
                this.title = "修改比赛视频";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.id != null) {
                        updateVideo(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        this.submitUpload();

                        // addVideo(this.form).then((response) => {
                        //     this.$modal.msgSuccess("新增成功");
                        //     this.open = false;
                        //     this.getList();
                        // });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids;
            this.$modal
                .confirm('是否确认删除比赛视频编号为"' + ids + '"的数据项？')
                .then(function () {
                    return delVideo(ids);
                })
                .then(() => {
                    this.getList();
                    this.$modal.msgSuccess("删除成功");
                })
                .catch(() => {});
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download(
                "system/video/export",
                {
                    ...this.queryParams,
                },
                `video_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>
