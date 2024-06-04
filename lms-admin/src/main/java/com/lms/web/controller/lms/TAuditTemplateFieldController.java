package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.TAuditTemplateField;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.ITAuditTemplateFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 模板字段Controller
 *
 * @author whn
 * @date 2024-05-06
 */
@RestController
@RequestMapping("/lms/audit/template/field")
public class TAuditTemplateFieldController extends BaseController {
    @Autowired
    private ITAuditTemplateFieldService tAuditTemplateFieldService;

    /**
     * 查询模板字段列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:field:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAuditTemplateField tAuditTemplateField) {
        startPage();
        List<TAuditTemplateField> list = tAuditTemplateFieldService.selectTAuditTemplateFieldList(tAuditTemplateField);
        return getDataTable(list);
    }


    @GetMapping("/tid/{templateId}")
    public AjaxResult getByTemplateId(@PathVariable("templateId")Long templateId){
        List<TAuditTemplateField> fieldList =  tAuditTemplateFieldService.selectTAuditTemplateFieldByTemplateId(templateId);
        return AjaxResult.success(fieldList);
    }

    /**
     * 导出模板字段列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:field:export')")
    @Log(title = "模板字段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAuditTemplateField tAuditTemplateField) {
        List<TAuditTemplateField> list = tAuditTemplateFieldService.selectTAuditTemplateFieldList(tAuditTemplateField);
        ExcelUtil<TAuditTemplateField> util = new ExcelUtil<TAuditTemplateField>(TAuditTemplateField.class);
        util.exportExcel(response, list, "模板字段数据");
    }

    /**
     * 获取模板字段详细信息
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:field:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAuditTemplateFieldService.selectTAuditTemplateFieldById(id));
    }

    /**
     * 新增模板字段
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:field:add')")
    @Log(title = "模板字段", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAuditTemplateField tAuditTemplateField) {
        return toAjax(tAuditTemplateFieldService.insertTAuditTemplateField(tAuditTemplateField));
    }

    /**
     * 修改模板字段
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:field:edit')")
    @Log(title = "模板字段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAuditTemplateField tAuditTemplateField) {
        return toAjax(tAuditTemplateFieldService.updateTAuditTemplateField(tAuditTemplateField));
    }

    /**
     * 删除模板字段
     */
    // @PreAuthorize("@ss.hasPermi('lms:template:field:remove')")
    @Log(title = "模板字段", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAuditTemplateFieldService.deleteTAuditTemplateFieldByIds(ids));
    }
}
