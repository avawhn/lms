package com.lms.web.controller.lms;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Robot;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.IRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 机器人Controller
 *
 * @author ruoyi
 * @date 2024-05-20
 */
@RestController
@RequestMapping("/lms/robot")
public class RobotController extends BaseController {
    @Autowired
    private IRobotService robotService;

    /**
     * 查询机器人列表
     */
    // @PreAuthorize("@ss.hasPermi('system:robot:list')")
    @GetMapping("/list")
    public TableDataInfo list(Robot robot) {
        startPage();
        List<Robot> list = robotService.selectRobotList(robot);
        return getDataTable(list);
    }

    /**
     * 导出机器人列表
     */
    // @PreAuthorize("@ss.hasPermi('system:robot:export')")
    // @Log(title = "机器人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Robot robot) {
        List<Robot> list = robotService.selectRobotList(robot);
        ExcelUtil<Robot> util = new ExcelUtil<Robot>(Robot.class);
        util.exportExcel(response, list, "机器人数据");
    }

    /**
     * 获取机器人详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:robot:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(robotService.selectRobotById(id));
    }

    /**
     * 新增机器人
     */
    // @PreAuthorize("@ss.hasPermi('system:robot:add')")
    // @Log(title = "机器人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Robot robot) {
        return toAjax(robotService.insertRobot(robot));
    }

    /**
     * 修改机器人
     */
    // @PreAuthorize("@ss.hasPermi('system:robot:edit')")
    // @Log(title = "机器人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Robot robot) {
        return toAjax(robotService.updateRobot(robot));
    }

    /**
     * 删除机器人
     */
    // @PreAuthorize("@ss.hasPermi('system:robot:remove')")
    // @Log(title = "机器人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(robotService.deleteRobotByIds(ids));
    }
}
