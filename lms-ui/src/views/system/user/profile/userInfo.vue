<template>
  <el-form ref="userRef" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item label="学院" prop="college">
      <el-input v-model="user.college" maxlength="30" />
    </el-form-item>
    <el-form-item label="专业" prop="major">
      <el-input v-model="user.major" maxlength="30" />
    </el-form-item>

    <el-form-item label="年级" prop="grade">
      <el-input-number v-model="user.grade" :min="1" />
    </el-form-item>

    <el-form-item label="学号" prop="stuNum">
      <el-input v-model="user.stuNum" maxlength="30" />
    </el-form-item>

    <el-form-item label="入队时间" prop="teamGrade">
      <el-input-number v-model="user.teamGrade" :min="1" />
    </el-form-item>


    <el-form-item label="手机号码" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
      </el-radio-group>
    </el-form-item>


    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { updateUserProfile } from "@/api/system/user";

const props = defineProps({
  user: {
    type: Object
  }
});

const { proxy } = getCurrentInstance();

const rules = ref({
  nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
  email: [{ required: true, message: "邮箱地址不能为空", trigger: "blur" }, { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
  phonenumber: [{ required: true, message: "手机号码不能为空", trigger: "blur" }, { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }],
  college: [{ required: true, message: "用户学院不能为空", trigger: "blur" }],
  major: [{ required: true, message: "用户专业不能为空", trigger: "blur" }],
  grade: [{ required: true, message: "用户年级不能为空", trigger: "blur" }],
  stuNum: [{ required: true, message: "用户学号不能为空", trigger: "blur" }],
  teamGrade: [{ required: true, message: "入队年级不能为空", trigger: "blur" }],
});

/** 提交按钮 */
function submit() {
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      console.log(props.user);
      updateUserProfile(props.user).then(response => {
        proxy.$modal.msgSuccess("修改成功");
      });
    }
  });
};
/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
};
</script>
