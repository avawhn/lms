package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.TAuditRecord;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.ITAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 审核记录Controller
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/lms/audit/record")
public class TAuditRecordController extends BaseController {
    @Autowired
    private ITAuditRecordService tAuditRecordService;

    /**
     * 查询审核记录列表
     */
    // @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAuditRecord tAuditRecord) {
        startPage();
        List<TAuditRecord> list = tAuditRecordService.selectTAuditRecordList(tAuditRecord);
        return getDataTable(list);
    }


    @PutMapping("/revoke/{recordId}")
    public AjaxResult revoke(@PathVariable("recordId") Long recordId) {
        return tAuditRecordService.revokeTAuditRecord(recordId);
    }

    @PutMapping("/pass/{recordId}")
    public AjaxResult pass(@PathVariable("recordId") Long recordId) {
        return tAuditRecordService.passTAuditRecord(recordId);
    }

    @PutMapping("/refuse/{recordId}")
    public AjaxResult refuse(@PathVariable("recordId") Long recordId) {
        return tAuditRecordService.refuseTAuditRecord(recordId);
    }


    @GetMapping("/list/{auditorId}")
    public TableDataInfo listByAuditId(@PathVariable("auditorId") Long id) {
        List<TAuditRecord> list = tAuditRecordService.selectTAuditRecordListByAuditorId(id);
        return getDataTable(list);
    }


    @GetMapping("/list/user/{userId}")
    public TableDataInfo listByUserId(@PathVariable("userId") Long userId) {
        startPage();
        TAuditRecord record = new TAuditRecord();
        record.setCreateUserId(userId);
        List<TAuditRecord> list = tAuditRecordService.selectTAuditRecordList(record);
        return getDataTable(list);
    }

    /**
     * 导出审核记录列表
     */
    // @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "审核记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAuditRecord tAuditRecord) {
        List<TAuditRecord> list = tAuditRecordService.selectTAuditRecordList(tAuditRecord);
        ExcelUtil<TAuditRecord> util = new ExcelUtil<TAuditRecord>(TAuditRecord.class);
        util.exportExcel(response, list, "审核记录数据");
    }

    /**
     * 获取审核记录详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAuditRecordService.selectTAuditRecordById(id));
    }

    /**
     * 新增审核记录
     */
    // @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "审核记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAuditRecord tAuditRecord) {
        return tAuditRecordService.insertTAuditRecordV2(tAuditRecord);
    }

    /**
     * 修改审核记录
     */
    // @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "审核记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAuditRecord tAuditRecord) {
        return toAjax(tAuditRecordService.updateTAuditRecord(tAuditRecord));
    }

    /**
     * 删除审核记录
     */
    // @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "审核记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAuditRecordService.deleteTAuditRecordByIds(ids));
    }
}
