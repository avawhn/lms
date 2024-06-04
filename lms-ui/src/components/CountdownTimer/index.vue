<template>
    <div style="text-align: center">
        <p>剩余时间：{{ formattedTime }}</p>
    </div>
</template>

<script>
import { ref, onMounted, onUnmounted, computed } from 'vue';

export default {
    name: "CountdownTimer",
    props: ["contestTime"],
    setup(props,{ emit }) {
        const totalSeconds = 60 * props.contestTime; // 1 hour in seconds
        const remainingTime = ref(totalSeconds);

        const formattedTime = computed(() => {
            const hours = Math.floor(remainingTime.value / 3600);
            const minutes = Math.floor((remainingTime.value % 3600) / 60);
            const seconds = remainingTime.value % 60;
            return `${hours.toString().padStart(2, "0")}:${minutes
                .toString()
                .padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
        });

        const countdown = () => {
            if (remainingTime.value > 0) {
                remainingTime.value--;
            } else {
                emit('timeEnd');
                clearInterval(timer);
            }
        };

        let timer;
        onMounted(() => {
            timer = setInterval(countdown, 1000);
        });

        onUnmounted(() => {
            clearInterval(timer);
        });

        return {
            formattedTime,
        };
    },
    data() {
        return {};
    },
    methods: {},
    created() {},
};
</script>
