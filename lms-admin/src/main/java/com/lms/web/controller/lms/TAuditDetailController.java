package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.TAuditDetail;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.ITAuditDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 审核详情Controller
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/lms/audit/detail")
public class TAuditDetailController extends BaseController
{
    @Autowired
    private ITAuditDetailService tAuditDetailService;

    /**
     * 查询审核详情列表
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAuditDetail tAuditDetail)
    {
        startPage();
        List<TAuditDetail> list = tAuditDetailService.selectTAuditDetailList(tAuditDetail);
        return getDataTable(list);
    }

    /**
     * 导出审核详情列表
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "审核详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAuditDetail tAuditDetail)
    {
        List<TAuditDetail> list = tAuditDetailService.selectTAuditDetailList(tAuditDetail);
        ExcelUtil<TAuditDetail> util = new ExcelUtil<TAuditDetail>(TAuditDetail.class);
        util.exportExcel(response, list, "审核详情数据");
    }

    /**
     * 获取审核详情详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tAuditDetailService.selectTAuditDetailById(id));
    }

    /**
     * 新增审核详情
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "审核详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAuditDetail tAuditDetail)
    {
        return toAjax(tAuditDetailService.insertTAuditDetail(tAuditDetail));
    }

    /**
     * 修改审核详情
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "审核详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAuditDetail tAuditDetail)
    {
        return toAjax(tAuditDetailService.updateTAuditDetail(tAuditDetail));
    }

    /**
     * 删除审核详情
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "审核详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tAuditDetailService.deleteTAuditDetailByIds(ids));
    }
}
