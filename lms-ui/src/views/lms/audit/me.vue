<template>
    <div class="app-container">
        <el-table
            v-loading="loading"
            :data="recordList"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="expand">
                <template #default="props">
                    <div m="4" class="expand-container">
                        <p v-for="item in props.row.detail">
                            {{ item.label }}：
                            <el-link
                                v-if="
                                    getCompName(item, props.row) ===
                                    'UploadFileComp'
                                "
                                type="primary"
                                v-for="url in item.value"
                                :href="url"
                                target="_blank"
                            >
                                文件
                            </el-link>

                            <span v-else>
                                {{ item.value }}
                            </span>
                        </p>
                    </div>
                </template>
            </el-table-column>

            <el-table-column
                label="申请类型"
                align="center"
                prop="template.name"
                width="80px"
            />
            <el-table-column
                label="申请人"
                align="center"
                prop="createUser.nickName"
            />

            <el-table-column
                label="申请时间"
                align="center"
                prop="createTime"
            />
            <el-table-column
                label="审核人员"
                align="center"
                prop="auditorStr"
            />

            <el-table-column
                label="当前审核"
                align="center"
                prop="curAuditor.nickName"
            />

            <el-table-column label="状态" align="center" prop="statusText" />

            <el-table-column
                label="操作"
                align="center"
                class-name="small-padding fixed-width"
                width="200"
            >
                <template #default="scope">
                    <el-button
                        size="small"
                        type="warning"
                        icon="Edit"
                        :disabled="revokeIsAvail(scope.row)"
                        @click="revokeAudit(scope.row)"
                        >撤销</el-button
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
    </div>
</template>

<script>
import {
    listRecord,
    listRecordByUserId,
    revokeRecord,
} from "@/api/lms/audit-record";
import { getPreviewUrl } from "@/api/lms/minio";
import useUserStore from "@/store/modules/user";
import { ElMessage } from "element-plus";

export default {
    name: "AuditRecord",
    setup() {
        const userStore = useUserStore();
        return { userStore };
    },
    data() {
        return {
            userId: this.userStore.userId,
            recordList: [],
            // 遮罩层
            loading: true,
            // 总条数
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
            },
        };
    },
    methods: {
        revokeIsAvail(row) {
            return row.status !== "0";
        },

        revokeAudit(row) {
            console.log(row);
            revokeRecord(row.id).then((res) => {
                ElMessage({
                    message: "撤销成功.",
                    type: "success",
                });
                this.getList();
            });
        },
        getCompName(detail, row) {
            const label = detail.label;
            return row.template.fieldList.find((item) => item.label === label)
                .componentName;
        },
        toToDetail(row) {
            this.$router.push({ path: "/audit/detail", query: { id: row.id } });
        },
        convertAuditorsToStr(order, list) {
            const ids = order.split(",");
            const array = ids.map((id) =>
                list.find((item) => item.userId === id)
            );
            let str = "";
            for (let i = 0; i < array.length; i++) {
                str += array[i].nickName;
                if (i < array.length - 1) {
                    str += " -> ";
                }
            }
            return str;
        },
        getStatusText(status) {
            if (status === "0") {
                return "申请中";
            } else if (status === "1") {
                return "通过";
            } else if (status === "2") {
                return "拒绝";
            }else if (status === "3") {
                return "已撤销";
            }
        },
        getList() {
            this.loading = true;
            listRecordByUserId(this.userId).then((response) => {
                this.recordList = response.rows;
                this.total = response.total;
                this.loading = false;

                this.recordList.forEach((r) => {
                    r.auditorStr = this.convertAuditorsToStr(
                        r.auditorIds,
                        r.auditors
                    );
                    r.statusText = this.getStatusText(r.status);

                    const fieldList = r.template.fieldList;

                    const curAuditorId = r.curAuditorId + "";

                    r.curAuditor =
                        r.status === "1"
                            ? { nickName: "无" }
                            : r.auditors.find(
                                  (item) => item.userId === curAuditorId
                              );

                    r.detail.forEach((d) => {
                        const field = fieldList.find((item) => {
                            return item.label === d.label;
                        });

                        if (field && field.componentName === "UploadFileComp") {
                            const url = d.value.split(",");
                            d.value = [];
                            url.forEach((u) => {
                                getPreviewUrl("lms", u).then((res) => {
                                    d.value.push(res.data);
                                });
                            });
                        }
                    });
                });

                console.log(this.recordList);
            });
        },
        // 多选框选中数据
        handleSelectionChange(selection) {},
    },
    created() {
        this.getList();
    },
};
</script>

<style scoped lang="scss">
.expand-container {
    margin-left: 130px;
}

.el-link {
    margin-right: 8px;
}
.el-link .el-icon--right.el-icon {
    vertical-align: text-bottom;
}
</style>
