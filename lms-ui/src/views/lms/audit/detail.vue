<template>
    <div class="app-container">

    </div>
</template>

<script>
import { getRecord } from "@/api/lms/audit-record";

export default {
    name: "AuditDetail",
    data() {
        return {
            templateId: null,
            record: null,
        };
    },
    methods: {},

    created() {
        this.templateId = this.$route.query.id;
        getRecord(this.templateId).then((response) => {
            this.record = response.data;
        });
    },
};
</script>
