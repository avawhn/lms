<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            size="small"
            :inline="true"
            v-show="showSearch"
        >
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
                    v-hasPermi="['lms:award:add']"
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
                    v-hasPermi="['lms:award:edit']"
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
                    v-hasPermi="['lms:award:remove']"
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
                    v-hasPermi="['lms:award:export']"
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
            :data="awardList"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="expand">
                <template #default="scope">
                    <div m="4">
                       <div class="imagePreview">
                            <img :src="item" v-for="item in scope.row.image"/>
                       </div>
                       <p>{{ scope.row.description }}</p>
                    </div>
                </template>
            </el-table-column>
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <el-table-column label="年级" align="center" prop="grade" />
            <el-table-column label="奖项内容" align="center" prop="content" />
            <!-- <el-table-column
                label="奖项图片"
                align="center"
                prop="image"
                width="100"
            >
                <template #default="scope"> </template>
            </el-table-column> -->
            <!-- <el-table-column
                label="赛事描述"
                align="center"
                prop="description"
            /> -->
            <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="primary"
                        icon="el-icon-edit"
                        @click="handleUpdate(scope.row)"
                        v-hasPermi="['lms:award:edit']"
                        >修改</el-button
                    >
                    <el-button
                        size="small"
                        type="danger"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:award:remove']"
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

        <!-- 添加或修改奖项对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="年级" prop="grade">
                    <el-input v-model="form.grade" placeholder="请输入年级" />
                </el-form-item>
                <el-form-item label="奖项内容" prop="content">
                    <el-input
                        v-model="form.content"
                        placeholder="请输入奖项内容"
                    />
                </el-form-item>
                <el-form-item label="奖项图片" prop="image">
                    <UploadFileComp v-model="form.image" prefix="/award/" />
                </el-form-item>
                <el-form-item label="赛事描述" prop="description">
                    <el-input
                        v-model="form.description"
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
    </div>
</template>

<script>
import {
    listAward,
    getAward,
    delAward,
    addAward,
    updateAward,
} from "@/api/lms/award";
import WangEditor from "@/components/Editor";
import UploadFileComp from "@/components/UploadFileComp";
import { getPreviewUrl } from "@/api/lms/minio";

export default {
    name: "Award",
    components: { WangEditor, UploadFileComp },
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
            // 奖项表格数据
            awardList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                grade: null,
                content: null,
                image: null,
                description: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                grade: [
                    {
                        required: true,
                        message: "年级不能为空",
                        trigger: "blur",
                    },
                ],
                content: [
                    {
                        required: true,
                        message: "奖项内容不能为空",
                        trigger: "blur",
                    },
                ],
                description: [
                    {
                        required: true,
                        message: "赛事描述不能为空",
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
        /** 查询奖项列表 */
        getList() {
            this.loading = true;
            listAward(this.queryParams).then((response) => {
                response.rows.forEach((r) => {
                    const urls = r.image.split(",");
                    r.image = [];
                    urls.forEach((u) => {
                        getPreviewUrl("lms", u).then((res) => {
                            r.image.push(res.data);
                        });
                    });
                });
                this.awardList = response.rows;
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
                grade: null,
                content: null,
                image: null,
                description: null,
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
            this.title = "添加奖项";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids;
            getAward(id).then((response) => {
                this.form = response.data;
                this.open = true;
                this.title = "修改奖项";
            });
        },
        /** 提交按钮 */
        submitForm() {
            // console.log(this.form);
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.id != null) {
                        updateAward(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addAward(this.form).then((response) => {
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
            const ids = row.id || this.ids;
            this.$modal
                .confirm('是否确认删除奖项编号为"' + ids + '"的数据项？')
                .then(function () {
                    return delAward(ids);
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
                "system/award/export",
                {
                    ...this.queryParams,
                },
                `award_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>
