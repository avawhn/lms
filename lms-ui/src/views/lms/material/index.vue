<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            size="small"
            :inline="true"
            v-show="showSearch"
            label-width="60px"
        >
            <el-form-item label="物料名称" prop="materialName">
                <el-input
                    v-model="queryParams.materialName"
                    placeholder="请输入物料名称"
                    clearable
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="所属分组" prop="groupId">
                <el-tree-select
                    v-model="queryParams.groupId"
                    :data="groupOptions"
                    :props="treeProps"
                    node-key="groupId"
                    check-strictly
                    :render-after-expand="false"
                >
                </el-tree-select>
            </el-form-item>
            <el-form-item label="负责人" prop="userIdList">
                <el-select
                    v-model="queryParams.userIds"
                    placeholder="请选择负责人"
                    style="width: 150px"
                >
                    <el-option
                        v-for="item in userList"
                        :key="item.userId"
                        :label="item.nickName"
                        :value="item.userId"
                    />
                </el-select>
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
                    v-hasPermi="['lms:material:add']"
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
                    v-hasPermi="['lms:material:edit']"
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
                    v-hasPermi="['lms:material:remove']"
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
                    v-hasPermi="['lms:material:export']"
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
            :data="materialList"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="40" align="center" />
            <el-table-column
                label="物料名称"
                align="center"
                prop="materialName"
            />
            <el-table-column
                label="所属组"
                align="center"
                prop="group.groupName"
            />
            <el-table-column label="图片" align="center" prop="imageUrl">
                <template #default="scope">
                    <img
                        :src="scope.row.imageUrl"
                        style="width: 40px; height: 40px"
                    />
                </template>
            </el-table-column>
            <el-table-column
                label="数量"
                align="center"
                prop="materialNumber"
            />
            <el-table-column label="负责人" align="center" prop="uesrNames" />
            <el-table-column
                label="联系电话"
                align="center"
                prop="telephones"
            />
            <el-table-column label="备注" align="center" prop="remark" />
            <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="primary"
                        icon="Plus"
                        @click="handleAddCount(scope.row)"
                        >入库</el-button
                    >
                    <el-button
                        size="small"
                        type="info"
                        icon="Minus"
                        @click="handleSubCount(scope.row)"
                        >消耗</el-button
                    >
                    <el-button
                        size="small"
                        type="warning"
                        icon="Edit"
                        @click="handleUpdate(scope.row)"
                        v-hasPermi="['lms:material:edit']"
                        >修改</el-button
                    >
                    <el-button
                        size="small"
                        type="danger"
                        icon="Delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:material:remove']"
                        >删除</el-button
                    >
                </template>
            </el-table-column>
        </el-table>

        <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
        />

        <!-- 添加或修改物料对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="物料名称" prop="materialName">
                    <el-input
                        v-model="form.materialName"
                        placeholder="请输入物料名称"
                    />
                </el-form-item>
                <el-form-item label="所属分组" prop="groupId">
                    <el-tree-select
                        v-model="form.groupId"
                        :data="groupOptions"
                        style="width: 240px"
                        :props="treeProps"
                        node-key="groupId"
                        check-strictly
                        :render-after-expand="false"
                    >
                    </el-tree-select>
                </el-form-item>
                <el-form-item label="图片" prop="imageUrl">
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
                </el-form-item>

                <el-form-item label="数量" prop="materialNumber">
                    <el-input-number v-model="form.materialNumber" :min="0" />
                </el-form-item>
                <el-form-item label="负责人" prop="userIdList">
                    <el-select
                        v-model="form.userIdList"
                        multiple
                        placeholder="请选择负责人"
                        style="width: 240px"
                    >
                        <el-option
                            v-for="item in userList"
                            :key="item.userId"
                            :label="item.nickName"
                            :value="item.userId"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input
                        v-model="form.remark"
                        type="textarea"
                        placeholder="请输入内容"
                    />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog
            :title="recordTitle"
            v-model="recordOpen"
            width="500px"
            append-to-body
        >
            <el-form
                ref="recordForm"
                :model="recordForm"
                :rules="recordRules"
                label-width="80px"
            >
                <el-form-item label="物料名称">
                    {{ recordForm.materialName }}
                </el-form-item>

                <el-form-item label="现有数量">
                    {{ recordForm.maxNumber }}
                </el-form-item>

                <el-form-item label="人员" prop="userId">
                    <el-select
                        v-model="recordForm.userId"
                        placeholder="请选择人员"
                        style="width: 150px"
                    >
                        <el-option
                            v-for="item in userList"
                            :key="item.userId"
                            :label="item.nickName"
                            :value="item.userId"
                        />
                    </el-select>
                </el-form-item>

                <el-form-item label="数量" prop="number">
                    <el-input-number v-model="recordForm.number" :min="1" />
                </el-form-item>

                <el-form-item label="用处" v-if="recordForm.type === '1'">
                    <el-input v-model="recordForm.work" style="width: 240px" placeholder="请输入用途" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitRecordForm"
                    >确 定</el-button
                >
                <el-button @click="cancelRecord">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listMaterial,
    getMaterial,
    delMaterial,
    addMaterial,
    updateMaterial,
} from "@/api/lms/material";

import { addMaterialRecord } from "@/api/lms/materialRecord";

import { listGroup } from "@/api/lms/group";
import { getUploadPreSignedUrl, uploadFile, deleteFile } from "@/api/lms/minio";
import { getUuid } from "@/utils/uuid";
import { genFileId, ElMessage } from "element-plus";
import { getAllUser } from "@/api/system/user";

export default {
    name: "Material",
    data() {
        return {
            userList: [],
            fileList: [],
            groupOptions: [],
            treeProps: markRaw({
                label: "groupName",
            }),
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
            // 物料表格数据
            materialList: [],
            // 弹出层标题
            title: "",
            recordTitle: "",
            // 是否显示弹出层
            open: false,
            recordOpen: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                userIdList: [],
                userIds: "",
                materialName: null,
                groupId: null,
                imageUrl: null,
                materialNumber: null,
            },
            // 表单参数
            form: {},
            recordForm: {},
            recordRules: {
                number: [
                    {
                        required: true,
                        message: "变更数量不能为空",
                        trigger: "blur",
                    },
                ],
                userId: [
                    {
                        required: true,
                        message: "变更数量不能为空",
                        trigger: "blur",
                    },
                ],
            },
            // 表单校验
            rules: {
                materialName: [
                    {
                        required: true,
                        message: "物料名称不能为空",
                        trigger: "blur",
                    },
                ],
                groupId: [
                    {
                        required: true,
                        message: "组id不能为空",
                        trigger: "blur",
                    },
                ],
                imageUrl: [
                    {
                        required: true,
                        message: "图片url不能为空",
                        trigger: "blur",
                    },
                ],
                materialNumber: [
                    {
                        required: true,
                        message: "数量不能为空",
                        trigger: "blur",
                    },
                ],
                userIdList: [
                    {
                        required: true,
                        message: "负责人不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    created() {
        this.getList();
        getAllUser().then((res) => {
            this.userList = res.data;
        });
        this.getGroupTree();
    },
    methods: {
        handleAddCount(row) {
            this.recordRest();
            this.recordForm.type = "0";
            this.recordForm.materialName = row.materialName;
            this.recordForm.materialId = row.materialId;
            this.recordForm.maxNumber = row.materialNumber;

            this.recordOpen = true;
            this.recordTitle = "物料入库";
        },
        handleSubCount(row) {
            this.recordRest();
            this.recordForm.type = "1";
            this.recordForm.materialName = row.materialName;
            this.recordForm.maxNumber = row.materialNumber;
            this.recordForm.materialId = row.materialId;

            this.recordOpen = true;
            this.recordTitle = "物料消耗";
        },
        handleRemove(file, uploadFiles) {
            if (!this.form.imageUrl) {
                return;
            }
            deleteFile("lms", this.form.imageUrl).then((res) => {
                // console.log(res);
            });
        },
        handleExceed(files) {
            this.fileList = [];
            const file = files[0];
            file.uid = genFileId();
            this.fileList.push(file);
            deleteFile("lms", this.form.imageUrl).then((res) => {
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
            const newFilename = this.generateUuidFileName("/material/", file);
            getUploadPreSignedUrl("lms", newFilename).then((response) => {
                const url = response.data;
                uploadFile(url, file).then((res) => {
                    if (res.status === 200) {
                        this.form.imageUrl = newFilename;
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
        getGroupTree() {
            listGroup().then((response) => {
                this.groupOptions = this.handleTree(response.data, "groupId");
            });
        },

        /** 查询物料列表 */
        getList() {
            this.loading = true;
            // console.log(this.queryParams)
            listMaterial(this.queryParams).then((response) => {
                response.rows.forEach((r) => {
                    const uesrNames = [];
                    const telephones = [];
                    r.userList.forEach((u) => {
                        uesrNames.push(u.nickName);
                        telephones.push(u.phonenumber);
                    });
                    r.uesrNames = uesrNames.join(",");
                    r.telephones = telephones.join(",");
                });

                this.materialList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },

        cancelRecord() {
            this.recordOpen = false;
            this.recordRest();
        },
        recordRest() {
            this.recordForm = {
                materialId: null,
                materialName: "",
                userId: null,
                number: null,
                work: null,
                maxNumber: null,
                type: null,
            };
            this.resetForm("recordForm");
        },
        // 表单重置
        reset() {
            this.fileList = [];
            this.form = {
                userIdList: [],
                materialId: null,
                materialName: null,
                groupId: null,
                imageUrl: null,
                materialNumber: 0,
                userIds: "",
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null,
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
            this.queryParams.userIds = "";
            this.queryParams.userIdList = [];
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map((item) => item.materialId);
            this.single = selection.length !== 1;
            this.multiple = !selection.length;
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加物料";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const materialId = row.materialId || this.ids;
            getMaterial(materialId).then((response) => {
                this.form = response.data;
                this.form.userIdList = this.form.userIds.split(",").map(Number);
                this.open = true;
                this.title = "修改物料";
            });
        },

        submitRecordForm() {
            if (
                this.recordForm.type === "1" &&
                this.recordForm.number > this.recordForm.maxNumber
            ) {
                ElMessage({
                    message: "消耗数量不能大于现有数量.",
                    type: "warning",
                });
                return;
            }
            addMaterialRecord(this.recordForm).then((res) => {
                ElMessage({
                    message:
                        (this.recordForm.type === "0" ? "入库" : "消耗") +
                        "成功.",
                    type: "success",
                });
                this.recordOpen = false;
                this.getList();
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.form.userIds = this.form.userIdList.join(",");
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.materialId != null) {
                        updateMaterial(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addMaterial(this.form).then((response) => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const materialIds = row.materialId || this.ids;
            this.$modal
                .confirm(
                    '是否确认删除物料编号为"' + materialIds + '"的数据项？'
                )
                .then(function () {
                    return delMaterial(materialIds);
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
                "lms/material/export",
                {
                    ...this.queryParams,
                },
                `material_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>
