<template>
    <div class="app-wrapper">
        <DisplayHead />
        <div class="body">
            <div class="grade">
                <el-tag
                    :type="item.grade === curGrade ? 'success' : null"
                    v-for="item in gradeOption"
                    @click="switchGrade(item)"
                    >{{ item.grade }} 届</el-tag
                >
            </div>

            <div class="awardImage">
                <el-carousel :interval="5000" arrow="always">
                    <el-carousel-item v-for="item in award.image" :key="item">
                        <img :src="item" style="width: 100%; height: auto" />
                    </el-carousel-item>
                </el-carousel>
            </div>

            <p class="awardDesc">{{ award.description }}</p>

            <div v-for="item in award.robotList" class="robot">
                <div class="work">
                    {{ item.workContent }}
                </div>

                <div class="robotImage">
                    <el-carousel :interval="5000" arrow="always">
                        <el-carousel-item v-for="img in item.image" :key="img">
                            <img :src="img" style="width: 100%; height: auto" />
                        </el-carousel-item>
                    </el-carousel>
                </div>
            </div>

            <div class="competitionVideo">
                <el-carousel :interval="5000" arrow="always">
                    <el-carousel-item v-for="item in videoList" :key="item">
                        <video
                            ref="videoPlayer"
                            width="640"
                            height="360"
                            controls
                        >
                            <source :src="item.url" type="video/mp4" />
                        </video>
                    </el-carousel-item>
                </el-carousel>
            </div>
        </div>
    </div>
</template>

<script>
import DisplayHead from "@/components/DisplayHead";
import {
    getLatestAward,
    getAllGrade,
    getAward,
    getAwardDetail,
} from "@/api/lms/award";
import { getPreviewUrl } from "@/api/lms/minio";
import { getVideoListByGrade } from "@/api/lms/video";

export default {
    name: "Awards",
    components: { DisplayHead },
    data() {
        return {
            gradeOption: [],
            curGrade: null,
            award: {},
            videoList: [],
            video: null,
        };
    },
    methods: {
        switchGrade(item) {
            // console.log(item);
            this.curGrade = item.grade;
            this.getAwardAndResolve(item.id);

            getVideoListByGrade(this.curGrade).then((res) => {
                this.videoList = res.data;
            });
        },

        getAwardAndResolve(id) {
            getAwardDetail(id).then((res) => {
                this.award = res.data;
                console.log(this.award);
                const urls = this.award.image.split(",");
                this.award.image = [];
                urls.forEach((u) => {
                    getPreviewUrl("lms", u).then((res) => {
                        this.award.image.push(res.data);
                    });
                });

                this.award.robotList.forEach((r) => {
                    const urls = r.image.split(",");
                    r.image = [];
                    urls.forEach((u) => {
                        getPreviewUrl("lms", u).then((res) => {
                            r.image.push(res.data);
                        });
                    });
                });
            });
        },
    },
    created() {
        getAllGrade().then((res) => {
            res.data.sort((a, b) => a.grade - b.grade);
            this.gradeOption = res.data;
            if (this.gradeOption.length > 0) {
                const latest = this.gradeOption[this.gradeOption.length - 1];
                this.curGrade = latest.grade;
                getVideoListByGrade(this.curGrade).then((res) => {
                    this.videoList = res.data;
                });
                this.getAwardAndResolve(latest.id);
            }
        });
    },
};
</script>

<style scoped lang="scss">
html,
body {
    margin: 0;
    padding: 0;
    height: 100%;
}
.app-wrapper {
    // width: 100%;
    // height: 100%;
    background-image: url("@/assets/images/bg.jpg"); /* 引用背景图像 */
    background-size: 100%; /* 设置背景图像覆盖整个 div */
    background-position: center; /* 背景图像居中 */
    background-repeat: repeat; /* 背景图像不重复 */
}
.body {
    width: 100%;
    height: 100%;
    margin: 0px;
    padding: 0px;
    text-align: center;
}

.awardImage {
    width: 500px;
    margin: 10px auto 10px auto;
}

.awardDesc {
    color: #ffff;
    width: 800px;
    margin: 10px auto 10px auto;
    text-indent: 2em;
    text-align: left;
}

.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
}

.robot {
    width: 900px;
    margin: 10px auto 10px auto;
    display: flex;

    .work {
        width: 580px;
        align-items: center;
        margin: auto 10px auto 10px;
        text-indent: 2em;
        text-align: left;
        color: yellow;
    }
    .robotImage {
        width: 300px;
    }
}

.competitionVideo {
    width: 500px;
    margin: 10px auto 10px auto;
}
</style>
