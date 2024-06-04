<template>
    <div class="app-wrapper">
        <DisplayHead />
        <div class="body">
            <div v-for="item in articleList" class="article">
                <el-link type="primary" class="title" @click="goToArticle(item)">{{ item.title }}</el-link>
                <el-tag type="primary" class="author">{{ item.user.nickName }}</el-tag>
                <span class="publisTime">{{ item.publishTime }}</span>
            </div>

            <pagination
                v-show="total > 0"
                :total="total"
                v-model:current-page="queryParams.pageNum"
                v-model:page-size="queryParams.pageSize"
                @pagination="getList"
            />
        </div>
    </div>
</template>

<script>
import { listArticle } from "@/api/lms/article";
import DisplayHead from "@/components/DisplayHead";

export default {
    name: "ArticleList",
    components: { DisplayHead },
    data() {
        return {
            articleList: [],
            total: 0,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
            },
        };
    },
    methods: {
        goToArticle(article) {
            this.$router.push({ path: "/article/detail", query: { id: article.id } });
        },
        getList() {
            listArticle(this.queryParams).then((res) => {
                this.articleList = res.rows;
                this.total = res.total;
            });
        },
    },
    created() {
        this.getList();
    },
};
</script>

<style scoped lang="scss">
.app-wrapper {
    position: absolute;
    width: 100%;
    height: 100%;
    background-image: url("@/assets/images/bg.jpg"); /* 引用背景图像 */
    background-size: cover; /* 设置背景图像覆盖整个 div */
    background-position: center; /* 背景图像居中 */
    background-repeat: no-repeat; /* 背景图像不重复 */
    z-index: 1;
}

.body {
    position: relative;
    z-index: 2;
    width: 800px;
    margin: 50px auto 10px auto;
    text-align: center;
    border: gray solid;
    border-radius: 5px;
    min-height: 500px;
}

.article {
    position: relative;
    display: flex;
    align-items: center; /* 垂直居中 */
    width: 500px;
    height: 40px;
    margin: 10px auto 10px auto;
    text-align: left;
    border: #ffffff solid;
    border-radius: 2px;

    .title {
        margin-left: 5px;
        color: orangered;
    }

    .publisTime {
        position: absolute;
        right: 5px;
        color: #ffffff;
    }

    .author {
        margin-left: 5px;
    }
}

:deep(.pagination-container){
    background: none;
}

:deep(.el-pagination__jump){
    color: white;
}

:deep(.el-pagination__total){
    color: white;
}
</style>
