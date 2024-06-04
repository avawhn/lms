package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.TAuditTemplate;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.ITAuditTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 审核模板Controller
 *
 * @author whn
 * @date 2024-05-06
 */
@RestController
@RequestMapping("/lms/template")
public class TAuditTemplateController extends BaseController {
    @Autowired
    private ITAuditTemplateService tAuditTemplateService;


    /**
     * 查询审核模板列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:list')")
    @GetMapping("/list/all")
    public AjaxResult listAll(TAuditTemplate tAuditTemplate) {
        List<TAuditTemplate> list = tAuditTemplateService.selectTAuditTemplateList(tAuditTemplate);
        return AjaxResult.success(list);
    }


    /**
     * 查询审核模板列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAuditTemplate tAuditTemplate) {
        startPage();
        List<TAuditTemplate> list = tAuditTemplateService.selectTAuditTemplateList(tAuditTemplate);
        return getDataTable(list);
    }

    /**
     * 导出审核模板列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:export')")
    @Log(title = "审核模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAuditTemplate tAuditTemplate) {
        List<TAuditTemplate> list = tAuditTemplateService.selectTAuditTemplateList(tAuditTemplate);
        ExcelUtil<TAuditTemplate> util = new ExcelUtil<TAuditTemplate>(TAuditTemplate.class);
        util.exportExcel(response, list, "审核模板数据");
    }

    /**
     * 获取审核模板详细信息
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAuditTemplateService.selectTAuditTemplateById(id));
    }


    // @PreAuthorize("@ss.hasPermi('lms:template:add')")
    @Log(title = "审核模板", businessType = BusinessType.INSERT)
    @PostMapping("/v2")
    public AjaxResult addV2(@RequestBody TAuditTemplate tAuditTemplate) {
        return toAjax(tAuditTemplateService.insertTAuditTemplateV2(tAuditTemplate));
    }


    /**
     * 新增审核模板
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:add')")
    @Log(title = "审核模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAuditTemplate tAuditTemplate) {
        return toAjax(tAuditTemplateService.insertTAuditTemplate(tAuditTemplate));
    }

    /**
     * 修改审核模板
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:edit')")
    @Log(title = "审核模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAuditTemplate tAuditTemplate) {
        return toAjax(tAuditTemplateService.updateTAuditTemplate(tAuditTemplate));
    }

    /**
     * 删除审核模板
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:remove')")
    @Log(title = "审核模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAuditTemplateService.deleteTAuditTemplateByIds(ids));
    }
}
