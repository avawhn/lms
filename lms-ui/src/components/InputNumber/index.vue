<template>
    <el-input-number
        v-model="num"
        :min="0"
        :step="0.5"
        :placeholder="placeholder"
        @change="handleChange"
    />
</template>

<script>
export default {
    name: "InputNumber",
    props: {
        // 数字名称
        modelValue: {
            type: Number,
            default: 1,
            // required: true,
        },
        placeholder: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            num: null,
        };
    },
    methods: {
        handleChange() {
            this.$emit("update:modelValue", this.num);
        },
    },
    watch: {
        // 每当 modelValue 改变时，这个函数就会执行
        modelValue(newV, oldV) {
            this.num = newV;
        },
    },
};
</script>
