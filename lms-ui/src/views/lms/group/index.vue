<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="60px">
            <el-form-item label="分组名称" prop="groupName">
                <el-input v-model="queryParams.groupName" placeholder="请输入分组名称" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="上级分组" prop="parentId">
                <el-tree-select v-model="queryParams.parentId" :data="groupList" style="width: 240px" :props="treeProps"
                    node-key="groupId" check-strictly>
                </el-tree-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" size="small" @click="handleQuery">搜索</el-button>
                <el-button size="small" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain size="small" @click="handleAdd"
                    v-hasPermi="['lms:group:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain size="small" @click="handleExport"
                    v-hasPermi="['lms:group:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="groupList" style="width: 100%; margin-bottom: 20px" row-key="groupId"
            border default-expand-all>
            <el-table-column label="组名称" prop="groupName" width="200" />
            <el-table-column label="组工作内容" prop="groupDuty" width="600" />
            <el-table-column label="备注" prop="remark" />
            <el-table-column label="操作" width="120" align="center">
                <template #default="scope">
                    <el-button type="primary" :icon="Edit" circle @click="handleUpdate(scope.row)"
                        v-hasPermi="['lms:group:edit']" />
                    <el-button type="danger" :icon="Delete" circle @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:group:remove']" />
                </template>
            </el-table-column>
        </el-table>



        <!-- 添加或修改分组管理对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="分组名称" prop="groupName">
                    <el-input v-model="form.groupName" placeholder="请输入组名称" />
                </el-form-item>
                <el-form-item label="分组工作内容" prop="groupDuty">
                    <el-input v-model="form.groupDuty" :rows="2" type="textarea" placeholder="请输入分组工作内容" />
                </el-form-item>
                <el-form-item label="上级分组" prop="parentId">
                    <el-tree-select v-model="form.parentId" :data="groupList" style="width: 240px" :props="treeProps"
                        node-key="groupId" check-strictly :render-after-expand="false">
                    </el-tree-select>
                </el-form-item>
                <el-form-item label="显示排序" prop="orderNum">
                    <el-input-number v-model="form.orderNum" :min="1" :max="1000" />
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" :rows="2" type="textarea" placeholder="请输入备注" />
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
import { listGroup, getGroup, delGroup, addGroup, updateGroup } from "@/api/lms/group";
import {
    Delete,
    Edit,
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'


export default {
    name: "Group",
    data() {
        return {
            Delete: markRaw(Delete),
            Edit: markRaw(Edit),
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
            // 分组管理表格数据
            groupList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                groupName: null,
                groupDuty: null,
                parentId: null,
                fullPath: null,
                orderNum: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                groupName: [
                    { required: true, message: "分组名称不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询分组管理列表 */
        getList() {
            this.loading = true;
            listGroup(this.queryParams).then(response => {
                this.groupList = this.handleTree(response.data, "groupId");
                // this.total = response.total;
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
                groupId: null,
                groupName: null,
                groupDuty: null,
                parentId: null,
                fullPath: null,
                orderNum: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null
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
            this.ids = selection.map(item => item.groupId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加分组管理";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const groupId = row.groupId || this.ids;
            getGroup(groupId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改分组管理";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.groupId != null) {
                        updateGroup(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addGroup(this.form).then(response => {
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
            if (row.children) {
                ElMessage({
                    message: '含有子项，删除失败.',
                    type: 'warning',
                });
                return;
            }

            const groupIds = row.groupId || this.ids;


            this.$modal.confirm("是否确认删除" + row.groupName + "分组").then(function () {
                return delGroup(groupIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('lms/group/export', {
                ...this.queryParams
            }, `group_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>
