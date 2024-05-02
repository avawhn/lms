package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.common.core.domain.entity.Group;
import com.lms.web.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组Controller
 *
 * @author whn
 * @date 2024-02-21
 */
@RestController
@RequestMapping("/lms/group")
public class GroupController extends BaseController {
    @Autowired
    private IGroupService groupService;

    /**
     * 查询组列表
     */
    @PreAuthorize("@ss.hasPermi('lms:group:list')")
    @GetMapping("/list/v2")
    public AjaxResult listV2(Group group) {
        List<Group> list = groupService.selectGroupList(group);
        return AjaxResult.success(list);
    }

    // ##################################### 下面为框架生成代码 #####################################


    /**
     * 查询组列表
     */
    @PreAuthorize("@ss.hasPermi('lms:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(Group group) {
        startPage();
        List<Group> list = groupService.selectGroupList(group);
        return getDataTable(list);
    }

    /**
     * 导出组列表
     */
    @PreAuthorize("@ss.hasPermi('lms:group:export')")
    @Log(title = "组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Group group) {
        List<Group> list = groupService.selectGroupList(group);
        ExcelUtil<Group> util = new ExcelUtil<Group>(Group.class);
        util.exportExcel(response, list, "组数据");
    }

    /**
     * 获取组详细信息
     */
    @PreAuthorize("@ss.hasPermi('lms:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(groupService.selectGroupById(id));
    }

    /**
     * 新增组
     */
    @PreAuthorize("@ss.hasPermi('lms:group:add')")
    @Log(title = "组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Group group) {
        return toAjax(groupService.insertGroup(group));
    }

    /**
     * 修改组
     */
    @PreAuthorize("@ss.hasPermi('lms:group:edit')")
    @Log(title = "组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Group group) {
        return toAjax(groupService.updateGroup(group));
    }

    /**
     * 删除组
     */
    @PreAuthorize("@ss.hasPermi('lms:group:remove')")
    @Log(title = "组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return groupService.deleteGroupByIds(ids);
    }
}
