<template>
    <div class="app-container">
        <el-form
            :model="queryParams"
            ref="queryForm"
            size="small"
            :inline="true"
            v-show="showSearch"
        >
            <!-- <el-form-item label="机器人名称" prop="robotName">
                <el-input
                    v-model="queryParams.robotName"
                    placeholder="请输入机器人名称"
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
                    v-hasPermi="['lms:robot:add']"
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
                    v-hasPermi="['lms:robot:edit']"
                    >修改</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    plain
                    icon="el-icon-delete"
                    size="small"
                    :disabled="multiple"
                    @click="handleDelete"
                    v-hasPermi="['lms:robot:remove']"
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
                    v-hasPermi="['lms:robot:export']"
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
            :data="robotList"
            @selection-change="handleSelectionChange"
        >
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <el-table-column type="expand">
                <template #default="scope">
                    <div m="4">
                        <div>
                            <img :src="item" v-for="item in scope.row.image" />
                        </div>
                        <p>{{ scope.row.workContent }}</p>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                label="机器人名称"
                align="center"
                prop="robotName"
            />
            <el-table-column label="年级" align="center" prop="grade" />
            <!-- <el-table-column
                label="负责内容"
                align="center"
                prop="workContent"
            /> -->
            <!-- <el-table-column
                label="机器人图片"
                align="center"
                prop="image"
                width="100"
            >
                <template #default="scope"> </template>
            </el-table-column> -->
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
                        v-hasPermi="['lms:robot:edit']"
                        >修改</el-button
                    >
                    <el-button
                        size="small"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:robot:remove']"
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

        <!-- 添加或修改机器人对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form
                ref="form"
                :model="form"
                :rules="rules"
                label-width="100px"
            >
                <el-form-item label="机器人名称" prop="robotName">
                    <el-input
                        v-model="form.robotName"
                        placeholder="请输入机器人名称"
                    />
                </el-form-item>
                <el-form-item label="年级" prop="grade">
                    <el-input v-model="form.grade" placeholder="请输入年级" />
                </el-form-item>
                <el-form-item label="负责内容">
                    <el-input
                        v-model="form.workContent"
                        :autosize="{ minRows: 4, maxRows: 8 }"
                        type="textarea"
                        placeholder="请输入工作内容"
                    />
                </el-form-item>
                <el-form-item label="机器人图片" prop="image">
                    <UploadFileComp v-model="form.image" prefix="/robot/" />
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
    listRobot,
    getRobot,
    delRobot,
    addRobot,
    updateRobot,
} from "@/api/lms/robot";
import UploadFileComp from "@/components/UploadFileComp";
import { getPreviewUrl } from "@/api/lms/minio";

export default {
    name: "Robot",
    components: { UploadFileComp },
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
            // 机器人表格数据
            robotList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                robotName: null,
                grade: null,
                workContent: null,
                image: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                robotName: [
                    {
                        required: true,
                        message: "机器人名称不能为空",
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
                workContent: [
                    {
                        required: true,
                        message: "负责内容不能为空",
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
        /** 查询机器人列表 */
        getList() {
            this.loading = true;
            listRobot(this.queryParams).then((response) => {
                response.rows.forEach((r) => {
                    const images = r.image.split(",");
                    r.image = [];
                    images.forEach((img) => {
                        getPreviewUrl("lms", img).then((res) => {
                            r.image.push(res.data);
                        });
                    });
                });
                this.robotList = response.rows;
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
                robotName: null,
                grade: null,
                workContent: null,
                image: null,
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
            this.title = "添加机器人";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids;
            getRobot(id).then((response) => {
                this.form = response.data;
                this.open = true;
                this.title = "修改机器人";
            });
        },
        /** 提交按钮 */
        submitForm() {

            // console.log(this.form);
            // this.reset();
            // return;
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.id != null) {
                        updateRobot(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addRobot(this.form).then((response) => {
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
                .confirm('是否确认删除机器人编号为"' + ids + '"的数据项？')
                .then(function () {
                    return delRobot(ids);
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
                "lms/robot/export",
                {
                    ...this.queryParams,
                },
                `robot_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>
