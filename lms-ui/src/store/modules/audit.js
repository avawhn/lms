import {
    listRecordByAuditorId,
    passRecord,
    refuseRecord,
} from "@/api/lms/audit-record";
import { getPreviewUrl } from "@/api/lms/minio";
import { ElMessage } from "element-plus";
import useUserStore from "@/store/modules/user";

const useAuditStore = defineStore("audit", {
    state: () => ({
        recordList: [],
    }),
    actions: {
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
            } else if (status === "3") {
                return "已撤销";
            }
        },

        getAudit(userId) {
            if (!userId) {
                userId = useUserStore().userId;
            }
            listRecordByAuditorId(userId).then((response) => {
                this.recordList = response.rows;
                this.recordList.forEach((r) => {
                    r.auditorStr = this.convertAuditorsToStr(
                        r.auditorIds,
                        r.auditors
                    );
                    r.statusText = this.getStatusText(r.status);
                    const fieldList = r.template.fieldList;
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
            });
        },
        passAudit(id) {
            passRecord(id)
                .then((res) => {
                    ElMessage({
                        message: "通过成功.",
                        type: "success",
                    });
                })
                .finally(() => {
                    this.getAudit();
                });
        },

        refuseAudit(id) {
            refuseRecord(id)
                .then((res) => {
                    ElMessage({
                        message: "拒绝成功.",
                        type: "success",
                    });
                })
                .finally(() => {
                    this.getAudit();
                });
        },
    },
});

export default useAuditStore;
