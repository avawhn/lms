<template>
    <el-date-picker
        v-model="date"
        type="date"
        :placeholder="placeholder"
        value-format="YYYY-MM-DD"
        :size="size"
    />
</template>

<script>
export default {
    name: "DatePicker",
    props: {
        modelValue: {
            type: String,
            default: "",
            // required: true,
        },
        placeholder: {
            type: String,
            default: "请选择日期",
        },
        size: {
            type: String,
            default: "default",
        },
    },
    data() {
        return {
            date: null,
        };
    },
    watch: {
        // 每当 modelValue 改变时，这个函数就会执行
        modelValue(newV, oldV) {
            if (this.date === this.modelValue) {
                return;
            }
            this.date = newV;
        },
        date(newV, oldV) {
            if (this.date === this.modelValue) {
                return;
            }
            this.$emit("update:modelValue", this.date);
        },
    },
};
</script>
