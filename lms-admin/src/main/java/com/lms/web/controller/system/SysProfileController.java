package com.lms.web.controller.system;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.SysUser;
import com.lms.common.core.domain.model.LoginUser;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.SecurityUtils;
import com.lms.common.utils.StringUtils;
import com.lms.common.utils.minio.MinioUtils;
import com.lms.common.utils.uuid.IdUtils;
import com.lms.framework.web.service.TokenService;
import com.lms.system.service.ISysUserService;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.lms.common.core.domain.entity.Constants.AVATAR_PREFIX;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.duration.get}")
    private int getMethodDuration;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser)) {
            return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
            return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(currentUser) > 0) {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return error("新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(userName, newPassword) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            // 上传至 MinIO

            String fileName = AVATAR_PREFIX + IdUtils.randomUUID() + file.getOriginalFilename();

            InputStream stream = file.getInputStream();
            minioUtils.putObject("lms", fileName, stream, file.getSize(), -1);
            stream.close();

            if (userService.updateUserAvatar(loginUser.getUsername(), fileName)) {
                AjaxResult ajax = AjaxResult.success();
                String url = minioUtils
                        .generatePresignedObjectUrl(Method.GET,
                                bucket, fileName,
                                getMethodDuration, TimeUnit.SECONDS,
                                null
                        );
                ajax.put("imgUrl", url);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(fileName);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }


            // 原代码，上传图片至本地机器
            // String avatar = FileUploadUtils.upload(LmsConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            // if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
            //     AjaxResult ajax = AjaxResult.success();
            //     ajax.put("imgUrl", avatar);
            //     // 更新缓存用户头像
            //     loginUser.getUser().setAvatar(avatar);
            //     tokenService.setLoginUser(loginUser);
            //     return ajax;
            // }
        }
        return error("上传图片异常，请联系管理员");
    }
}
