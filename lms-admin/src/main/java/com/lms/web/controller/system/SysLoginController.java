package com.lms.web.controller.system;

import com.lms.common.constant.Constants;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.SysMenu;
import com.lms.common.core.domain.entity.SysUser;
import com.lms.common.core.domain.model.LoginBody;
import com.lms.common.utils.SecurityUtils;
import com.lms.common.utils.minio.MinioUtils;
import com.lms.framework.web.service.SysLoginService;
import com.lms.framework.web.service.SysPermissionService;
import com.lms.system.service.ISysMenuService;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;


    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.duration.get}")
    private int getMethodDuration;


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() throws CloneNotSupportedException {
        SysUser user = SecurityUtils.getLoginUser().getUser().clone();

        String url = minioUtils
                .generatePresignedObjectUrl(Method.GET,
                        bucket, user.getAvatar(),
                        getMethodDuration, TimeUnit.SECONDS,
                        null
                );
        user.setAvatar(url);

        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
