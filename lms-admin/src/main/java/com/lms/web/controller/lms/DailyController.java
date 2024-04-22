package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Daily;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.IDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日报管理Controller
 *
 * @author whn
 * @date 2024-03-25
 */
@RestController
@RequestMapping("/lms/daily")
public class DailyController extends BaseController {
    @Autowired
    private IDailyService dailyService;

    /**
     * 查询日报管理列表
     */
    @PreAuthorize("@ss.hasPermi('lms:daily:list')")
    @GetMapping("/list")
    public TableDataInfo list(Daily daily) {
        startPage();
        List<Daily> list = dailyService.selectDailyList(daily);
        return getDataTable(list);
    }

    /**
     * 导出日报管理列表
     */
    @PreAuthorize("@ss.hasPermi('lms:daily:export')")
    @Log(title = "日报管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Daily daily) {
        List<Daily> list = dailyService.selectDailyList(daily);
        ExcelUtil<Daily> util = new ExcelUtil<Daily>(Daily.class);
        util.exportExcel(response, list, "日报管理数据");
    }

    /**
     * 获取日报管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('lms:daily:list')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dailyService.selectDailyById(id));
    }

    /**
     * 新增日报管理
     */
    @PreAuthorize("@ss.hasPermi('lms:daily:add')")
    @Log(title = "日报管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Daily daily) {
        return dailyService.insertDaily(daily);
    }

    /**
     * 修改日报管理
     */
    @PreAuthorize("@ss.hasPermi('lms:daily:edit')")
    @Log(title = "日报管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Daily daily) {
        return dailyService.updateDaily(daily);
    }


    @PreAuthorize("@ss.hasPermi('lms:daily:edit')")
    @Log(title = "日报管理", businessType = BusinessType.UPDATE)
    @PutMapping("/list")
    public AjaxResult editV2(@RequestBody List<Daily> dailyList) {
        return toAjax(dailyService.updateDailyList(dailyList));
    }

    /**
     * 删除日报管理
     */
    @PreAuthorize("@ss.hasPermi('lms:daily:remove')")
    @Log(title = "日报管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dailyService.deleteDailyByIds(ids));
    }
}
