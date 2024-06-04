<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            size="small"
            :inline="true"
            v-show="showSearch"
        >
            <el-form-item label="作者" prop="userId">
                <el-select
                    v-model="queryParams.userId"
                    placeholder="请选择作者"
                    style="width: 100px"
                >
                    <el-option
                        v-for="item in userList"
                        :key="item.userId"
                        :label="item.nickName"
                        :value="item.userId"
                    />
                </el-select>
            </el-form-item>
            <!-- <el-form-item label="发布时间" prop="publishTime">
                <el-date-picker
                    clearable
                    v-model="queryParams.publishTime"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="请选择发布时间"
                >
                </el-date-picker>
            </el-form-item> -->
            <el-form-item>
                <el-button
                    type="primary"
                    icon="el-icon-search"
                    size="small"
                    @click="handleQuery"
                    >搜索</el-button
                >
                <el-button
                    icon="el-icon-refresh"
                    size="small"
                    @click="resetQuery"
                    >重置</el-button
                >
            </el-form-item>
        </el-form>

        <el-table
            v-loading="loading"
            :data="articleList"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="标题" align="center" prop="title" />
            <el-table-column label="作者" align="center" prop="user.nickName" />
            <el-table-column
                label="发布时间"
                align="center"
                prop="publishTime"
                width="180"
            >
                <template #default="scope">
                    <span>{{
                        parseTime(scope.row.publishTime, "{y}-{m}-{d}")
                    }}</span>
                </template>
            </el-table-column>
            <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="text"
                        icon="el-icon-edit"
                        @click="handleUpdate(scope.row)"
                        v-hasPermi="['system:article:edit']"
                        >修改</el-button
                    >
                    <el-button
                        size="small"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['system:article:remove']"
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

        <!-- 添加或修改文章对话框 -->
        <el-dialog
            :title="title"
            :visible.sync="open"
            width="500px"
            append-to-body
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="作者ID" prop="userId">
                    <el-input
                        v-model="form.userId"
                        placeholder="请输入作者ID"
                    />
                </el-form-item>
                <el-form-item label="内容">
                    <editor v-model="form.content" :min-height="192" />
                </el-form-item>
                <el-form-item label="发布时间" prop="publishTime">
                    <el-date-picker
                        clearable
                        v-model="form.publishTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择发布时间"
                    >
                    </el-date-picker>
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
    listArticle,
    getArticle,
    delArticle,
    addArticle,
    updateArticle,
} from "@/api/lms/article";
import { getAllUser } from "@/api/system/user";

export default {
    name: "Article",
    data() {
        return {
            userList: [],
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
            // 文章表格数据
            articleList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                userId: null,
                content: null,
                publishTime: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                userId: [
                    {
                        required: true,
                        message: "作者ID不能为空",
                        trigger: "blur",
                    },
                ],
                content: [
                    {
                        required: true,
                        message: "内容不能为空",
                        trigger: "blur",
                    },
                ],
                publishTime: [
                    {
                        required: true,
                        message: "发布时间不能为空",
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
    },
    methods: {
        /** 查询文章列表 */
        getList() {
            this.loading = true;
            listArticle(this.queryParams).then((response) => {
                console.log(response);
                this.articleList = response.rows;
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
                userId: null,
                content: null,
                publishTime: null,
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
            this.title = "添加文章";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            // this.reset();
            // const id = row.id || this.ids;
            // getArticle(id).then((response) => {
            //     this.form = response.data;
            //     this.open = true;
            //     this.title = "修改文章";
            // });
            this.$router.push({ path: "/forum/editArticle", query: { id: row.id } });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.id != null) {
                        updateArticle(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addArticle(this.form).then((response) => {
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
                .confirm('是否确认删除 "' + row.title + '" 文章？')
                .then(function () {
                    return delArticle(ids);
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
                "system/article/export",
                {
                    ...this.queryParams,
                },
                `article_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>
