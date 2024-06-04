<template>
    <div class="head">
        <pre style="color: #ffffff; font-size: 6px" @click="goToHome">
                    
,--.   ,--.   ,--. ,---.   
|  |   |   `.'   |'   .-'  
|  |   |  |'.'|  |`.  `-.  
|  '--.|  |   |  |.-'    | 
`-----'`--'   `--'`-----'  
        </pre>

        <div class="menu">
            <div class="menuItem" @click="goToAwards">历届奖项</div>
            <div class="menuItem" @click="goToArticlePage">文章</div>
        </div>
    </div>
</template>

<script>
export default {
    name: "DisplayHead",
    methods: {
        goToAwards() {
            this.$router.push({ path: "/lms/list/awards" });
        },
        goToHome() {
            this.$router.push({ path: "/lms/display" });
        },

        goToArticlePage() {
            this.$router.push({ path: "/list/article" });
        },
    },
};
</script>

<style scoped lang="scss">
.head {
    width: 100%;
    height: 50px;
    background-color: #2b2b2b;
    position: relative;
    display: flex;

    pre {
        width: 100px;
    }
    .menu {
        display: flex;
        color: #ffffff;
        font-size: 22px;
        position: absolute;
        right: 10px;

        .menuItem {
            text-align: center;
            padding: 10px 10px 10px 10px;
        }

        .menuItem:hover {
            background-color: #ff6637;
        }
    }
}
</style>
