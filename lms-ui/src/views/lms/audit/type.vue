<template>
    <div class="app-container">
        <div class="type-container" v-for="item in typeList" @click="goToDetail(item.id)">
            <label> {{ item.name }}</label>
            <svg-icon :icon-class="item.image" />
            <span>{{ item.description }}</span>
        </div>
    </div>
</template>

<script>
import { listAllTemplate } from "@/api/lms/template";
import SvgIcon from "@/components/SvgIcon";

export default {
    name: "Type",
    components: { SvgIcon },
    data() {
        return {
            typeList: [],
        };
    },
    methods: {
        goToDetail(id) {
            this.$router.push({ path: '/audit/new', query: { id: id } });
        }
    },
    created() {
        listAllTemplate().then((response) => {
            this.typeList = response.data;
        });
    },
};
</script>

<style scoped lang="scss">
.app-container {
    display: flex;
}
.type-container {
    width: 120px;
    padding: 10px 10px 10px 10px;
    border: black solid;
    border-radius: 5px;
    justify-content: center;
    align-items: center;
    text-align: center;
    margin-right: 50px;

    background-color: #00BFFF;

    padding: 10px 10px 10px 10px;

    label {
        display: block;
    }

    span {
        display: block;
    }

    .svg-icon {
        width: 2em;
        height: 2em;
    }
}
</style>
