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
            <el-form-item label="设备名称" prop="deviceName">
                <el-input
                    v-model="queryParams.deviceName"
                    placeholder="请输入设备名称"
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
                    v-hasPermi="['lms:device:add']"
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
                    v-hasPermi="['lms:device:edit']"
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
                    v-hasPermi="['lms:device:remove']"
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
                    v-hasPermi="['lms:device:export']"
                    >导出</el-button
                >
            </el-col>

            <el-col :span="1.5">
                <el-button
                    type="success"
                    plain
                    icon="PieChart"
                    size="small"
                    @click="handelStatisticsCount"
                    v-hasPermi="['lms:device:list']"
                    >统计正常设备数量</el-button
                >
            </el-col>

            <right-toolbar
                :showSearch.sync="showSearch"
                @queryTable="getList"
            ></right-toolbar>
        </el-row>

        <el-table
            v-loading="loading"
            :data="deviceList"
            @selection-change="handleSelectionChange"
        >
            <!-- <el-table-column type="selection" width="55" align="center" /> -->

            <el-table-column type="expand">
                <template #default="props">
                    <div m="4" style="margin-left: 80px">
                        <p m="t-0 b-2">
                            所属分组: {{ props.row.group.groupName }}
                        </p>
                        <p m="t-0 b-2">负责人: {{ props.row.uesrNames }}</p>
                        <p m="t-0 b-2">联系电话: {{ props.row.telephones }}</p>
                        <p m="t-0 b-2">备注: {{ props.row.remark }}</p>
                    </div>
                </template>
            </el-table-column>

            <el-table-column
                label="设备名称"
                align="center"
                prop="deviceName"
            />

            <el-table-column label="品牌" align="center" prop="brand" />

            <el-table-column
                label="设备类型"
                align="center"
                prop="deviceTypeId"
            >
                <template #default="scope">
                    <dict-tag
                        :options="lms_device_type"
                        :value="scope.row.deviceTypeId"
                    />
                </template>
            </el-table-column>

            <el-table-column label="状态" align="center" prop="status">
                <template #default="scope">
                    <dict-tag
                        :options="lms_device_status"
                        :value="scope.row.status"
                    />
                </template>
            </el-table-column>

            <el-table-column label="数量" align="center" prop="deviceNumber" />

            <el-table-column
                label="出厂日期"
                align="center"
                prop="manufactureDate"
            />
            <el-table-column
                label="报废日期"
                align="center"
                prop="retirementDate"
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
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="primary"
                        icon="Edit"
                        @click="handleUpdate(scope.row)"
                        v-hasPermi="['lms:device:edit']"
                        >修改</el-button
                    >
                    <el-button
                        size="small"
                        type="danger"
                        icon="Delete"
                        @click="handleDelete(scope.row)"
                        v-hasPermi="['lms:device:remove']"
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

        <!-- 添加或修改设备对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="设备名称" prop="deviceName">
                    <el-input
                        v-model="form.deviceName"
                        placeholder="请输入设备名称"
                    />
                </el-form-item>

                <el-form-item label="设备类型" prop="deviceTypeId">
                    <el-select
                        v-model="form.deviceTypeId"
                        placeholder="请选择设备类型"
                        style="width: 240px"
                    >
                        <el-option
                            v-for="item in lms_device_type"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>

                <el-form-item label="品牌" prop="brand">
                    <el-input
                        v-model="form.brand"
                        placeholder="请输入设备品牌"
                    />
                </el-form-item>

                <el-form-item label="出厂日期" prop="manufactureDate">
                    <el-date-picker
                        v-model="form.manufactureDate"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择出厂日期"
                    />
                </el-form-item>

                <el-form-item label="报废日期" prop="retirementDate">
                    <el-date-picker
                        v-model="form.retirementDate"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择报废日期"
                    />
                </el-form-item>

                <el-form-item label="状态" prop="status">
                    <el-select
                        v-model="form.status"
                        placeholder="请选择设备状态"
                        style="width: 240px"
                    >
                        <el-option
                            v-for="item in lms_device_status"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
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

                <el-form-item label="数量" prop="deviceNumber">
                    <el-input-number v-model="form.deviceNumber" :min="0" />
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

        <Chart
            v-model:open="chartOpen"
            :option="option"
            v-model:loading="chartLoading"
        />
    </div>
</template>

<script>
import {
    listDevice,
    getDevice,
    delDevice,
    addDevice,
    updateDevice,
    statisticsTypeCount,
} from "@/api/lms/device";
import { listGroup } from "@/api/lms/group";
import { getUploadPreSignedUrl, uploadFile, deleteFile } from "@/api/lms/minio";
import { getUuid } from "@/utils/uuid";
import { genFileId, ElMessage } from "element-plus";
import { getAllUser } from "@/api/system/user";
import Chart from "@/components/Chart";

export default {
    name: "Device",
    components: { Chart },
    setup() {
        const { proxy } = getCurrentInstance();
        const { lms_device_type, lms_device_status } = proxy.useDict(
            "lms_device_type",
            "lms_device_status"
        );
        return { lms_device_type, lms_device_status };
    },
    data() {
        return {
            chartOpen: false,
            chartLoading: false,
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
            // 设备表格数据
            deviceList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                deviceName: null,
                groupId: null,
                deviceNumber: null,
                imageUrl: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                deviceName: [
                    {
                        required: true,
                        message: "设备名称不能为空",
                        trigger: "blur",
                    },
                ],
                groupId: [
                    {
                        required: true,
                        message: "所属组不能为空",
                        trigger: "blur",
                    },
                ],
                deviceNumber: [
                    {
                        required: true,
                        message: "数量不能为空",
                        trigger: "blur",
                    },
                ],
                imageUrl: [
                    {
                        required: true,
                        message: "图片不能为空",
                        trigger: "blur",
                    },
                ],
                userIdList: [
                    {
                        required: true,
                        message: "图片不能为空",
                        trigger: "blur",
                    },
                ],
                status: [
                    {
                        required: true,
                        message: "请选择设备状态",
                        trigger: "blur",
                    },
                ],
                deviceTypeId: [
                    {
                        required: true,
                        message: "请选择设备类型",
                        trigger: "blur",
                    },
                ],
                brand: [
                    {
                        required: true,
                        message: "请输入设备品牌",
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
                        data: [],
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
                        data: [],
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
        handelStatisticsCount() {
            this.chartLoading = true;
            this.option.xAxis[0].data = [];
            this.option.series[0].data = [];
            statisticsTypeCount().then((res) => {
                const xdata = [];
                const seriesData = [];
                res.data.forEach((r) => {
                    const device = this.lms_device_type.find(
                        (item) => item.value === r.label
                    );
                    xdata.push(device.label);
                    seriesData.push(r.number);
                });

                this.option.xAxis[0].data = xdata;
                this.option.series[0].data = seriesData;
                this.chartOpen = true;
                this.chartLoading = false;
            });
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
            const newFilename = this.generateUuidFileName("/device/", file);
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

        /** 查询设备列表 */
        getList() {
            this.loading = true;
            listDevice(this.queryParams).then((response) => {
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

                this.deviceList = response.rows;
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
            this.fileList = [];
            this.form = {
                deviceId: null,
                deviceName: null,
                groupId: null,
                userIdList: [],
                userIds: "",
                deviceNumber: 0,
                imageUrl: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null,

                deviceTypeId: null,
                brand: null,
                manufactureDate: null,
                retirementDate: null,
                status: null,
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
            this.queryParams.userIds = null;
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map((item) => item.deviceId);
            this.single = selection.length !== 1;
            this.multiple = !selection.length;
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加设备";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const deviceId = row.deviceId || this.ids;
            getDevice(deviceId).then((response) => {
                this.form = response.data;
                this.form.userIdList = this.form.userIds.split(",").map(Number);
                this.open = true;
                this.title = "修改设备";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.form.userIds = this.form.userIdList.join(",");

            this.$refs["form"].validate((valid) => {
                if (valid) {
                    if (this.form.deviceId != null) {
                        updateDevice(this.form).then((response) => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addDevice(this.form).then((response) => {
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
            const deviceIds = row.deviceId || this.ids;
            this.$modal
                .confirm('是否确认删除设备编号为"' + deviceIds + '"的数据项？')
                .then(function () {
                    return delDevice(deviceIds);
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
                "lms/device/export",
                {
                    ...this.queryParams,
                },
                `device_${new Date().getTime()}.xlsx`
            );
        },
    },
};
</script>
