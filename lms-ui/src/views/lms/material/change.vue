<template>
    <div class="app-container">
        <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="物料ID" prop="materialId">
        <el-input
          v-model="queryParams.materialId"
          placeholder="请输入物料ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变化数量" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入变化数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:record:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:record:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->
        <div style="margin-bottom: 20px">
            <span>选择年度：</span>
            <el-select
                v-model="statisticsForm.year"
                placeholder="请选择年"
                style="width: 100px"
            >
                <el-option
                    v-for="item in yearOption"
                    :key="item"
                    :label="item + '年'"
                    :value="item"
                />
            </el-select>
        </div>
        <div class="static-wrapper">
            <span style="margin-top: 5px">入库数量统计：</span>
            <el-button type="primary" plain @click="addStaticsYear"
                >年度统计</el-button
            >
            <el-button type="success" plain @click="addStatics"
                >全部统计</el-button
            >
        </div>

        <div class="static-wrapper">
            <span style="margin-top: 5px">消耗数量统计：</span>
            <el-button type="primary" plain @click="subStaticsYear"
                >年度统计</el-button
            >
            <el-button type="success" plain @click="subStatics"
                >全部统计</el-button
            >
        </div>

        <el-table
            v-loading="loading"
            :data="recordList"
            @selection-change="handleSelectionChange"
        >
            <el-table-column label="变更编号" align="center" prop="id" />
            <el-table-column
                label="物料"
                align="center"
                prop="material.materialName"
            />
            <el-table-column label="用户" align="center" prop="user.nickName" />
            <el-table-column label="变化数量" align="center" prop="number" />
            <el-table-column label="类型" align="center" prop="type" />
            <el-table-column label="用处" align="center" prop="work" />
            <el-table-column label="时间" align="center" prop="createTime" />
            <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:record:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
        </el-table>

        <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
        />

        <!-- 添加或修改物料变更对话框 -->
        <el-dialog
            :title="title"
            :visible.sync="open"
            width="500px"
            append-to-body
        >
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="物料ID" prop="materialId">
                    <el-input
                        v-model="form.materialId"
                        placeholder="请输入物料ID"
                    />
                </el-form-item>
                <el-form-item label="用户ID" prop="userId">
                    <el-input
                        v-model="form.userId"
                        placeholder="请输入用户ID"
                    />
                </el-form-item>
                <el-form-item label="变化数量" prop="number">
                    <el-input
                        v-model="form.number"
                        placeholder="请输入变化数量"
                    />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <Chart
            v-model:open="chartOpen"
            :option="option"
            v-model:loading="chartLoading"
        />
    </div>
</template>

<script>
import {
    listMaterialRecord,
    getMaterialRecord,
    delMaterialRecord,
    addMaterialRecord,
    updateMaterialRecord,
    listAllYear,
    statisticsYear,
} from "@/api/lms/materialRecord";
import { ElMessage } from "element-plus";
import Chart from "@/components/Chart";

export default {
    name: "Record",
    components: { Chart },
    data() {
        return {
            chartOpen: false,
            chartLoading: false,
            yearOption: [],
            statisticsForm: {
                year: null,
                type: null,
            },
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
            // 物料变更表格数据
            recordList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                materialId: null,
                userId: null,
                number: null,
                type: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                materialId: [
                    {
                        required: true,
                        message: "物料ID不能为空",
                        trigger: "blur",
                    },
                ],
                userId: [
                    {
                        required: true,
                        message: "用户ID不能为空",
                        trigger: "blur",
                    },
                ],
                number: [
                    {
                        required: true,
                        message: "变化数量不能为空",
                        trigger: "blur",
                    },
                ],
                type: [
                    {
                        required: true,
                        message: "类型：0：入库，1：消耗不能为空",
                        trigger: "change",
                    },
                ],
                createTime: [
                    {
                        required: true,
                        message: "入库、消耗时间不能为空",
                        trigger: "blur",
                    },
                ],
            },

            option: {
                tooltip: {
                    trigger: "axis",
                    axisPointer: {
                        type: "shadow",
                    },
                },
                grid: {
                    left: "3%",
                    right: "4%",
                    bottom: "3%",
                    containLabel: true,
                },
                xAxis: [
                    {
                        type: "category",
                        data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
                        axisTick: {
                            alignWithLabel: true,
                        },
                    },
                ],
                yAxis: [
                    {
                        type: "value",
                    },
                ],
                series: [
                    {
                        name: "Direct",
                        type: "bar",
                        barWidth: "60%",
                        data: [10, 52, 200, 334, 390, 330, 220],
                    },
                ],
            },
        };
    },
    created() {
        this.getList();
        listAllYear().then((res) => {
            this.yearOption = res.data;
        });
    },
    methods: {
        resetOptionData() {
            this.option.xAxis[0].data = [];
            this.option.series[0].data = [];
        },
        asertYearParam() {
            if (this.statisticsForm.year === null) {
                ElMessage({
                    message: "还未选择年度.",
                    type: "warning",
                });
                return false;
            }
            return true;
        },
        addStaticsYear() {
            if (!this.asertYearParam()) {
                return;
            }
            this.statisticsForm.type = "0";
            this.resetOptionData();
            statisticsYear(this.statisticsForm).then((res) => {
                const xdata = [];
                const seriesData = [];
                res.data.forEach((r) => {
                    xdata.push(r.materialName);
                    seriesData.push(r.number);
                });
                this.option.xAxis[0].data = xdata;
                this.option.series[0].data = seriesData;
                this.chartLoading = false;
                this.chartOpen = true;
            });
        },
        addStatics() {
            this.resetOptionData();
            statisticsYear({ type: "0" }).then((res) => {
                const xdata = [];
                const seriesData = [];
                res.data.forEach((r) => {
                    xdata.push(r.materialName);
                    seriesData.push(r.number);
                });
                this.option.xAxis[0].data = xdata;
                this.option.series[0].data = seriesData;
                this.chartLoading = false;
                this.chartOpen = true;
            });
        },
        subStaticsYear() {
            if (!this.asertYearParam()) {
                return;
            }
            this.statisticsForm.type = "1";
            this.resetOptionData();
            statisticsYear(this.statisticsForm).then((res) => {
                const xdata = [];
                const seriesData = [];
                res.data.forEach((r) => {
                    xdata.push(r.materialName);
                    seriesData.push(r.number);
                });
                this.option.xAxis[0].data = xdata;
                this.option.series[0].data = seriesData;

                console.log(xdata);
                this.chartLoading = false;
                this.chartOpen = true;
            });
        },
        subStatics() {
            this.statisticsForm.type = "1";
            this.resetOptionData();
            statisticsYear({ type: "1" }).then((res) => {
                const xdata = [];
                const seriesData = [];
                res.data.forEach((r) => {
                    xdata.push(r.materialName);
                    seriesData.push(r.number);
                });
                this.option.xAxis[0].data = xdata;
                this.option.series[0].data = seriesData;
                this.chartLoading = false;
                this.chartOpen = true;
            });
        },
        /** 查询物料变更列表 */
        getList() {
            this.loading = true;
            listMaterialRecord(this.queryParams).then((response) => {
                response.rows.forEach((r) => {
                    r.type = r.type === "0" ? "入库" : "消耗";
                });
                this.recordList = response.rows;
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
                materialId: null,
                userId: null,
                number: null,
                type: null,
                createTime: null,
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
            this.title = "添加物料变更";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids;
            getMaterialRecord(id).then((response) => {
                this.form = response.data;
                this.open = true;
                this.title = "修改物料变更";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.id != null) {
                        updateMaterialRecord(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addMaterialRecord(this.form).then((response) => {
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
                .confirm('是否确认删除物料变更编号为"' + ids + '"的数据项？')
                .then(function () {
                    return delMaterialRecord(ids);
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
                "lms/material/record/export",
                {
                    ...this.queryParams,
                },
                `record_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>

<style scoped lang="scss">
.static-wrapper {
    margin-bottom: 20px;
    display: flex;
}
</style>
