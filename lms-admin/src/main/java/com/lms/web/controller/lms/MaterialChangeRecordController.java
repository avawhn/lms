package com.lms.web.controller.lms;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.MaterialChangeStatisticsBody;
import com.lms.common.core.domain.entity.MaterialChangeRecord;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.IMaterialChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 物料变更Controller
 *
 * @author ruoyi
 * @date 2024-05-21
 */
@RestController
@RequestMapping("/lms/material/record")
public class MaterialChangeRecordController extends BaseController {
    @Autowired
    private IMaterialChangeRecordService materialChangeRecordService;

    /**
     * 查询物料变更列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(MaterialChangeRecord materialChangeRecord) {
        startPage();
        List<MaterialChangeRecord> list = materialChangeRecordService.selectMaterialChangeRecordList(materialChangeRecord);
        return getDataTable(list);
    }

    @GetMapping("/year")
    public AjaxResult listYearByType() {
        return success(materialChangeRecordService.selectAllYear());
    }

    @PostMapping("/statistics/year")
    public AjaxResult statisticsYear(@RequestBody MaterialChangeStatisticsBody body) {
        return success(materialChangeRecordService.statisticsYear(body.getYear(), body.getType()));
    }

    /**
     * 导出物料变更列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:record:export')")
    // @Log(title = "物料变更", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MaterialChangeRecord materialChangeRecord) {
        List<MaterialChangeRecord> list = materialChangeRecordService.selectMaterialChangeRecordList(materialChangeRecord);
        ExcelUtil<MaterialChangeRecord> util = new ExcelUtil<MaterialChangeRecord>(MaterialChangeRecord.class);
        util.exportExcel(response, list, "物料变更数据");
    }

    /**
     * 获取物料变更详细信息
     */
    // @PreAuthorize("@ss.hasPermi('lms:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(materialChangeRecordService.selectMaterialChangeRecordById(id));
    }

    /**
     * 新增物料变更
     */
    // @PreAuthorize("@ss.hasPermi('lms:record:add')")
    // @Log(title = "物料变更", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialChangeRecord materialChangeRecord) {
        return toAjax(materialChangeRecordService.insertMaterialChangeRecord(materialChangeRecord));
    }

    /**
     * 修改物料变更
     */
    // @PreAuthorize("@ss.hasPermi('lms:record:edit')")
    // @Log(title = "物料变更", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialChangeRecord materialChangeRecord) {
        return toAjax(materialChangeRecordService.updateMaterialChangeRecord(materialChangeRecord));
    }

    /**
     * 删除物料变更
     */
    // @PreAuthorize("@ss.hasPermi('lms:record:remove')")
    // @Log(title = "物料变更", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(materialChangeRecordService.deleteMaterialChangeRecordByIds(ids));
    }
}
