package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Device;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备Controller
 *
 * @author whn
 * @date 2024-02-21
 */
@RestController
@RequestMapping("/lms/device")
public class DeviceController extends BaseController {
    @Autowired
    private IDeviceService deviceService;




    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('lms:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(Device device) {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    @GetMapping("/statistics/type/count")
    public AjaxResult statisticsTypeCount(){
        return success(deviceService.statisticsTypeCount());
    }

    /**
     * 导出设备列表
     */
    @PreAuthorize("@ss.hasPermi('lms:device:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Device device) {
        List<Device> list = deviceService.selectDeviceList(device);
        ExcelUtil<Device> util = new ExcelUtil<Device>(Device.class);
        util.exportExcel(response, list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('lms:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(deviceService.selectDeviceById(id));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('lms:device:add')")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Device device) {
        return toAjax(deviceService.insertDevice(device));
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('lms:device:edit')")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Device device) {
        return toAjax(deviceService.updateDevice(device));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('lms:device:remove')")
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(deviceService.deleteDeviceByIds(ids));
    }
}
