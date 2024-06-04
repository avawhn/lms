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
            <el-form-item label="日期范围" prop="date">
                <el-date-picker
                    v-model="queryParams.dateRange"
                    type="daterange"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="截至日期"
                    :shortcuts="shortcuts"
                    size="small"
                    value-format="YYYY-MM-DD"
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
                    v-hasPermi="['lms:daily:add']"
                    >新增</el-button
                >
            </el-col>
            <!-- <el-col :span="1.5">
                <el-button
                    type="success"
                    plain
                    icon="Edit"
                    size="small"
                    :disabled="single"
                    @click="handleUpdate"
                    v-hasPermi="['lms:daily:edit']"
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
                    v-hasPermi="['lms:daily:remove']"
                    >删除</el-button
                >
            </el-col> -->
            <el-col :span="1.5">
                <el-button
                    type="warning"
                    plain
                    icon="Download"
                    size="small"
                    @click="handleExport"
                    v-hasPermi="['lms:daily:export']"
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
            :data="dailyList"
            @selection-change="handleSelectionChange"
        >
            <!-- <el-table-column type="expand">
                <template #default="props">
                    <div m="4" class="detail">
                        <p m="t-0 b-2">创建用户: {{ props.row.createBy }}</p>
                        <p m="t-0 b-2">创建时间: {{ props.row.createTime }}</p>
                        <p m="t-0 b-2">更新用户: {{ props.row.updateBy }}</p>
                        <p m="t-0 b-2">更新时间: {{ props.row.updateTime }}</p>
                        <p m="t-0 b-2">备注: {{ props.row.remark }}</p>
                    </div>
                </template>
            </el-table-column> -->

            <el-table-column
                label="姓名"
                align="center"
                prop="user.nickName"
                width="100"
            />

            <el-table-column
                label="日期"
                align="center"
                prop="date"
                width="120"
            >
                <template #default="scope">
                    <span>{{ parseTime(scope.row.date, "{y}-{m}-{d}") }}</span>
                </template>
            </el-table-column>

            <el-table-column
                label="星期"
                align="center"
                prop="date"
                width="120"
            >
                <template #default="scope">
                    <span>{{ convertToWeekNum(scope.row.date) }}</span>
                </template>
            </el-table-column>

            <el-table-column label="工作内容" align="center" prop="content" />

            <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
                width="200"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="primary"
                        icon="Edit"
                        :disabled="isCurWeek(scope.row.date)"
                        @click="handleUpdate(scope.row)"
                        v-hasPermi="['lms:daily:edit']"
                        >修改</el-button
                    >
                    <el-button
                        size="small"
                        type="danger"
                        icon="Delete"
                        :disabled="isCurWeek(scope.row.date)"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:daily:remove']"
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

        <!-- 添加或修改周报对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="日期" prop="date">
                    <!-- <el-date-picker
                        clearable
                        v-model="form.date"
                        type="date"
                        :disabled-date="judgeDate"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择日期"
                    >
                    </el-date-picker> -->

                    <el-select
                        v-model="form.date"
                        placeholder="请选择日期"
                        style="width: 240px"
                    >
                        <el-option
                            v-for="item in curWeekDate"
                            :key="item.date"
                            :label="item.week"
                            :value="item.date"
                        >
                            <span style="float: left">{{ item.week }}</span>
                            <span
                                style="
                                    float: right;
                                    color: var(--el-text-color-secondary);
                                    font-size: 13px;
                                "
                            >
                                {{ item.date }}
                            </span>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="工作内容" prop="content">
                    <el-input
                        v-model="form.content"
                        :autosize="{ minRows: 2, maxRows: 4 }"
                        type="textarea"
                        placeholder="请输入当天工作内容"
                    />
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
    </div>
</template>

<script>
import {
    listDaily,
    getDaily,
    delDaily,
    addDaily,
    updateDaily,
} from "@/api/lms/daily";
import { getAllUser } from "@/api/system/user";
import useUserStore from "@/store/modules/user";

export default {
    name: "Daily",
    setup() {
        const userStore = useUserStore();
        return { userStore };
    },
    data() {
        return {
            monday: null,
            sunday: null,
            curWeekDate: null,
            userId: this.userStore.userId,
            // 控制部分字段的显示与隐藏，0：更新，1：添加
            dialogMod: 0,
            userSelectDisabled: false,
            datePickDisabled: false,
            userList: [],
            shortcuts: [
                {
                    text: "本星期",
                    value: () => {
                        const today = new Date(); // 获取当前日期
                        const dayOfWeek = today.getDay(); // 获取当前是星期几，0 表示星期日，1 表示星期一，依此类推

                        // 计算星期一的日期
                        const start = new Date(today);
                        start.setDate(
                            today.getDate() -
                                dayOfWeek +
                                (dayOfWeek === 0 ? -6 : 1)
                        ); // 如果今天是星期日，返回上周的星期一

                        // 计算星期日的日期
                        const end = new Date(today);
                        end.setDate(today.getDate() - dayOfWeek + 7); // 下周的星期日

                        this.setTimeOfDay(start, 0, 0, 0, 0);
                        this.setTimeOfDay(end, 0, 0, 0, 0);
                        return [start, end];
                    },
                },
                {
                    text: "上星期",
                    value: () => {
                        const today = new Date();
                        const dayOfWeek = today.getDay(); // 获取今天是星期几，0 表示星期日，1 表示星期一，依此类推

                        // 计算上一个星期的星期一
                        const start = new Date(today);
                        start.setDate(today.getDate() - dayOfWeek - 6); // 星期一是上周的星期一

                        // 计算上一个星期的星期日
                        const end = new Date(today);
                        end.setDate(today.getDate() - dayOfWeek); // 星期日是上周的星期日

                        this.setTimeOfDay(start, 0, 0, 0, 0);
                        this.setTimeOfDay(end, 0, 0, 0, 0);
                        return [start, end];
                    },
                },
                {
                    text: "当前月",
                    value: () => {
                        const today = new Date();
                        const year = today.getFullYear();
                        const month = today.getMonth();

                        // 获取本月的第一天
                        const start = new Date(year, month, 1);

                        // 获取下个月的第一天，然后回退一天，即为本月的最后一天
                        const nextMonth = new Date(year, month + 1, 1);
                        const end = new Date(nextMonth - 86400000); // 86400000 毫秒 = 1 天

                        this.setTimeOfDay(start, 0, 0, 0, 0);
                        this.setTimeOfDay(end, 0, 0, 0, 0);
                        return [start, end];
                    },
                },
            ],
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
            // 周报表格数据
            dailyList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                dateRange: [],
                userId: null,
                date: null,
                content: null,
                params: {},
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                userId: [
                    {
                        required: true,
                        message: "用户id不能为空",
                        trigger: "blur",
                    },
                ],
                date: [
                    {
                        required: true,
                        message: "日期不能为空",
                        trigger: "blur",
                    },
                ],
                content: [
                    {
                        required: true,
                        message: "工作内容不能为空",
                        trigger: "blur",
                    },
                ],
                createTime: [
                    {
                        required: true,
                        message: "创建时间不能为空",
                        trigger: "blur",
                    },
                ],
                updateTime: [
                    {
                        required: true,
                        message: "更新时间不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    mounted() {
        const range = this.shortcuts[0].value();
        this.monday = range[0];
        this.sunday = range[1];

        const start = this.formatDateToYYYYMMDD(range[0]);
        const end = this.formatDateToYYYYMMDD(range[1]);
        this.queryParams.params.beginTime = start;
        this.queryParams.params.endTime = end;
        this.queryParams.dateRange = [start, end];
    },
    created() {
        this.getList();
        this.curWeekDate = this.getCurrentWeekDates();
        // getAllUser().then((res) => {
        //     this.userList = res.data;
        // });
    },
    methods: {
        getCurrentWeekDates() {
            const currentDate = new Date(); // 获取当前日期
            const today = currentDate.getDay(); // 获取今天是星期几，0 表示星期日，1 表示星期一，依此类推

            // 计算本周的星期一的日期
            const monday = new Date(currentDate);
            monday.setDate(currentDate.getDate() - today + 1);

            const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];

            // 创建一个数组存储本周的日期和星期几信息
            const weekDates = [];

            // 循环计算本周的每一天的日期和星期几
            for (let i = 0; i < 7; i++) {
                const date = new Date(monday);
                date.setDate(monday.getDate() + i);

                // 格式化日期为 YYYY-MM-DD
                const formattedDate = this.formatDateToYYYYMMDD(date);

                // 获取星期几
                const dayOfWeek = weekdays[date.getDay()];

                // 构造日期和星期几对象，添加到数组中
                weekDates.push({ date: formattedDate, week: dayOfWeek });
            }

            return weekDates;
        },
        setTimeOfDay(date, hours, minutes, seconds, milliseconds) {
            date.setHours(hours);
            date.setMinutes(minutes);
            date.setSeconds(seconds);
            date.setMilliseconds(milliseconds);
            return date;
        },
        judgeDate(date) {
            return !(date >= this.monday && date <= this.sunday);
        },
        isCurWeek(dateStr) {
            const targetDate = new Date(dateStr);

            // 判断目标日期是否在当前星期范围内
            return !(targetDate >= this.monday && targetDate <= this.sunday);
        },
        formatDateToYYYYMMDD(date) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从 0 开始，需要加 1
            const day = String(date.getDate()).padStart(2, "0");

            return `${year}-${month}-${day}`;
        },
        convertToWeekNum(dateStr) {
            const daysOfWeek = [
                "星期日",
                "星期一",
                "星期二",
                "星期三",
                "星期四",
                "星期五",
                "星期六",
            ];

            const date = new Date(dateStr);

            const dayIndex = date.getDay(); // 获取星期几的索引，0 表示星期日，1 表示星期一，依此类推

            return daysOfWeek[dayIndex];
        },
        /** 查询周报列表 */
        getList() {
            this.loading = true;
            this.queryParams.userId = this.userId;
            listDaily(this.queryParams).then((response) => {
                this.dailyList = response.rows;
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
                date: null,
                content: null,
                createTime: null,
                updateTime: null,
                remark: null,
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;

            const dateRange = this.queryParams.dateRange;
            if (dateRange) {
                this.queryParams.params.beginTime = dateRange[0];
                this.queryParams.params.endTime = dateRange[1];
            }

            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.queryParams.dateRange = null;
            this.queryParams.userId = null;
            this.queryParams.params = {};

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

            this.userSelectDisabled = false;
            this.datePickDisabled = true;

            this.open = true;
            this.title = "添加周报";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            this.userSelectDisabled = true;
            this.datePickDisabled = true;

            this.queryParams.date = null;
            this.queryParams.params = {};

            const id = row.id || this.ids;
            getDaily(id).then((response) => {
                this.form = response.data;
                this.open = true;
                this.title = "修改周报";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.form.userId = this.userId;
            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.id != null) {
                        updateDaily(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addDaily(this.form).then((response) => {
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
                .confirm('是否确认删除"' + row.date + '"的数据项？')
                .then(function () {
                    return delDaily(ids);
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
                "lms/daily/export",
                {
                    ...this.queryParams,
                },
                `daily_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>

<style scope lang="scss">
.detail {
    p {
        margin-left: 70px;
    }
}
</style>
