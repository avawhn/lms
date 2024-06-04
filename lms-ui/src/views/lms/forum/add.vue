<template>
    <div class="app-container">
        <el-form
            :model="form"
            label-width="auto"
            style="max-width: 600px"
            :rules="rules"
            ref="formRef"
        >
            <el-form-item label="标题" prop="title">
                <el-input v-model="form.title" />
            </el-form-item>
        </el-form>

        <WangEditor v-model="form.content" height="500" />

        <div style="text-align: center">
            <el-button type="primary" plain @click="submitForm"
                >发布文章</el-button
            >
        </div>
    </div>
</template>

<script>
import WangEditor from "@/components/Editor";
import useUserStore from "@/store/modules/user";
import { addArticle, updateArticle, getArticle } from "@/api/lms/article";
import { ElMessage } from "element-plus";

export default {
    name: "AddArticle",
    components: { WangEditor },
    setup() {
        const userStore = useUserStore();
        return { userStore };
    },
    data() {
        return {
            form: {
                userId: this.userStore.userId,
            },
            rules: {
                title: [
                    {
                        required: true,
                        message: "标题不能为空",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        updateImage() {
            
        },
        submitForm() {
            console.log(this.form);
            this.$refs["formRef"].validate((valid) => {
                if (valid) {
                    if (this.form.id) {
                        updateArticle(this.form).then((res) => {
                            ElMessage({
                                message: "修改成功.",
                                type: "success",
                            });
                        });
                    } else {
                        addArticle(this.form).then((response) => {
                            ElMessage({
                                message: "添加成功.",
                                type: "success",
                            });
                        });
                    }
                }
            });
        },
    },
    created() {
        const id = this.$route.query.id;
        if (id) {
            getArticle(id).then((res) => {
                this.form = res.data;
            });
        }
    },
    mounted() {
        
    }
};
</script>
