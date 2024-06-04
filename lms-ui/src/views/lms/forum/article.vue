<template>
    <DisplayHead/>
    <div class="article" v-html="article.content"></div>
</template>

<script>
import { getArticle } from "@/api/lms/article";
import DisplayHead from "@/components/DisplayHead";
export default {
    name: "Article",
    components: { DisplayHead },
    data() {
        return {
            article: {},
        };
    },
    methods: {},
    created() {
        const articleId = this.$route.query.id;
        getArticle(articleId).then((response) => {
            this.article = response.data;
        });
    },
};
</script>

<style scoped lang="scss">
.article {
    margin: 10px auto 10px auto;
    width: 800px;
    text-indent: 2em;
    text-align: left;
    // border: red solid;
}



</style>
