<template>
    <el-input
        v-model="text"
        style="width: 240px"
        :autosize="{ minRows: 2, maxRows: 6 }"
        type="textarea"
        :placeholder="placeholder"
    />
</template>

<script>
export default {
    name: "TextArea",
    props: {
        // 图片名称
        modelValue: {
            type: String,
            default: ""
        },
        placeholder: {
            type: String,
            default: "请输入内容",
        },
    },
    data() {
        return {
            text: "",
        };
    },
    watch: {
        text(newV) {
            if (this.text === this.modelValue) {
                return;
            }
            this.$emit("update:modelValue", newV);
        },
        modelValue(newV) {
            if (this.text === this.modelValue) {
                return;
            }
            this.text = this.modelValue;
        },
    },
};
</script>
