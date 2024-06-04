<template>
    <div class="app-wrapper">
        <DisplayHead />
        <div class="body">
            <h1 class="laboratoryName">{{ laboratoryInfo.name }}</h1>
            <div class="introduce-wrapper">
                <div class="introduce">
                    {{ laboratoryInfo.description }}
                </div>

                <div class="imagePreview">
                    <el-carousel trigger="click" height="400px">
                        <el-carousel-item
                            v-for="item in laboratoryInfo.imageList"
                            :key="item"
                        >
                            <img :src="item.url" />
                        </el-carousel-item>
                    </el-carousel>
                </div>
            </div>

            <div class="notice-wrapper">
                <div class="noticle">
                    <div class="head">
                        <img
                            src="@/assets/images/array-left.png"
                            class="left-array"
                            @click="prevPageNotice"
                            v-if="noticeQueryParams.pageNum > 1"
                        />
                        <img
                            src="@/assets/images/array-right.png"
                            class="right-array"
                            @click="nextPageNotice"
                            v-if="
                                noticeQueryParams.pageNum *
                                    noticeQueryParams.pageSize <
                                noticeTotal
                            "
                        />
                    </div>
                    <el-divider />
                    <div class="noticeItem" v-for="item in noticeList">
                        <el-tag
                            :type="item.noticeType === '1' ? null : 'success'"
                            style="margin-right: 5px"
                            >{{
                                item.noticeType === "1" ? "通知" : "公告"
                            }}</el-tag
                        >

                        <el-link @click="showNotice(item)">{{
                            item.noticeTitle
                        }}</el-link>

                        <span class="publishTime">{{ item.createTime }}</span>
                    </div>
                </div>

                <div class="content">
                    <div class="content-head">公告通知内容</div>
                    <el-divider />
                    <div class="content-body">
                        <p>{{ noticeContent }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import DisplayHead from "@/components/DisplayHead";
import { listNotice } from "@/api/system/notice";
import { getLaboratoryInfo } from "@/api/lms/laboratoryInfo";
import { getPreviewUrl } from "@/api/lms/minio";

export default {
    name: "LmsDisplay",
    components: { DisplayHead },
    data() {
        return {
            laboratoryInfo: {},
            noticeList: [],
            noticeTotal: 0,
            noticeContent: null,
            noticeQueryParams: {
                pageNum: 1,
                pageSize: 10,
            },
        };
    },
    methods: {

        prevPageNotice() {
            this.noticeQueryParams.pageNum--;
            this.noticeContent = null;
            this.getNoticeList();
        },
        nextPageNotice() {
            this.noticeQueryParams.pageNum++;
            this.noticeContent = null;
            this.getNoticeList();
        },
        showNotice(notice) {
            this.noticeContent = notice.noticeContent;
        },
        formatCreateTime(str) {
            const date = new Date(str);
            const year = date.getFullYear();
            const month = date.getMonth() + 1;
            const day = date.getDate();
            return year + "年" + month + "月" + day + "日";
        },
        getNoticeList() {
            listNotice(this.noticeQueryParams).then((res) => {
                res.rows.forEach((r) => {
                    r.createTime = this.formatCreateTime(r.createTime);
                });
                this.noticeList = res.rows;
                this.noticeTotal = res.total;
                console.log(res);
            });
        },
    },
    created() {
        this.getNoticeList();

        getLaboratoryInfo().then((res) => {
            res.data.imageList.forEach((r) => {
                getPreviewUrl("lms", r.value).then((res) => {
                    r.url = res.data;
                });
            });
            this.laboratoryInfo = res.data;
            console.log(this.laboratoryInfo);
        });
    },
};
</script>

<style scoped lang="scss">
.app-wrapper {
    // width: 100%;
    // height: 100%;
    background-image: url("@/assets/images/bg.jpg"); /* 引用背景图像 */
    background-size: cover; /* 设置背景图像覆盖整个 div */
    background-position: center; /* 背景图像居中 */
    background-repeat: no-repeat; /* 背景图像不重复 */
}
.body {
    width: 100%;
    margin: 0px;
    padding: 0px;
}

.laboratoryName {
    color: aliceblue;
    text-align: center;
}

.introduce-wrapper {
    width: 800px;
    margin: 20px auto 10px auto;
    display: flex;
    .introduce {
        width: 350px;
        // border: red solid;
        margin: 100px 25px 10px 25px;
        text-indent: 2em;
        color: aqua;
    }

    .imagePreview {
        width: 400px;
        // border: red solid;
    }
}

.notice-wrapper {
    margin: 50px auto 0px auto;
    width: 900px;
    height: 380px;
    border: #47a3ff solid;
    display: flex;
    :deep(.el-divider--horizontal) {
        margin: 0px;
    }
    .noticle {
        width: 500px;
        // border: #47A3FF solid;
        position: relative;

        border-right: #ffff solid;
        :deep(.el-link) {
            color: #ffff;
        }
        .head {
            height: 20px;
            width: 100%;

            .left-array {
                position: absolute;
                left: 0px;
                height: 20px;
                width: auto;
                margin-left: 5px;
            }

            .right-array {
                position: absolute;
                right: 0px;
                height: 20px;
                width: auto;
                margin-right: 7px;
            }
        }

        .noticeItem {
            margin: 10px 10px 10px 10px;
            color: #ffff;
            position: relative;

            .publishTime {
                position: absolute;
                right: 5px;
                color: gray;
            }
        }
    }

    .content {
        width: 400px;

        // margin: auto 10px auto 10px;
        // text-indent: 2em;
        // text-align: left;
        // font-size: 15px;

        .content-head {
            height: 20px;
            text-align: center;
            color: #ffff;
            width: 100%;
            // border: purple solid;
            margin: 0px;
        }

        .content-body {
            width: 380px;
            margin: auto 10px auto 10px;
            color: #ff850a;
            text-align: left;
            text-indent: 2em;
        }
    }
}
</style>
