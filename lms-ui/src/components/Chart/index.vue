<template>
    <div class="myOverlay" v-show="open" @click="closeOverLay"></div>

    <div ref="main" class="charts" v-show="open"></div>
</template>

<script setup name="Chart">
import * as echarts from "echarts";
import { ref, onMounted } from "vue";

const emit = defineEmits(["update:modelValue","update:loading"]);



const props = defineProps({
    open: {
        type: Boolean,
        default: false,
    },
    loading: {
        type: Boolean,
        required: true,
    },
    option: {
        type: Object,
        required: true
    },
});


watch(
    () => props.option,
    (newValue, oldValue) => {
        chart.setOption(newValue);
    },{ deep: true }
);


watch(() => props.loading, (val) => {
    if(val){
        chart.showLoading();
    }else {
        chart.hideLoading();
    }
})

const main = ref(null);
let chart = null;

onMounted(() => {
    // 基于准备好的dom，初始化echarts实例
    chart = echarts.init(main.value);
    // 绘制图表
    chart.setOption(props.option);
});

function closeOverLay() {
    emit("update:open", false);
    emit("update:loading", true);
}

function handleUserProportion() {
    chartOpen.value = true;
}
</script>

<style scoped lang="scss">
.charts {
    position: absolute;
    width: 900px;
    height: 400px;
    z-index: 20001;
    background-color: #ffffff;
    align-items: center;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}
</style>
