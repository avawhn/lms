package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Award;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.IAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 奖项Controller
 *
 * @author ruoyi
 * @date 2024-05-20
 */
@RestController
@RequestMapping("/lms/award")
public class AwardController extends BaseController {
    @Autowired
    private IAwardService awardService;

    /**
     * 查询奖项列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:award:list')")
    @GetMapping("/list")
    public TableDataInfo list(Award award) {
        startPage();
        List<Award> list = awardService.selectAwardList(award);
        return getDataTable(list);
    }

    @GetMapping("/latest")
    public AjaxResult latest(){
        Award award = awardService.selectLatestAward();
        return AjaxResult.success(award);
    }

    @GetMapping("/grade/all")
    public AjaxResult getAllGrade(){
        List<Award> list = awardService.selectAllGrade();
        return AjaxResult.success(list);
    }

    /**
     * 导出奖项列表
     */
    // @PreAuthorize("@ss.hasPermi('lms:award:export')")
    @Log(title = "奖项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Award award) {
        List<Award> list = awardService.selectAwardList(award);
        ExcelUtil<Award> util = new ExcelUtil<Award>(Award.class);
        util.exportExcel(response, list, "奖项数据");
    }

    /**
     * 获取奖项详细信息
     */
    // @PreAuthorize("@ss.hasPermi('lms:award:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(awardService.selectAwardById(id));
    }


    @GetMapping("/detail/{id}")
    public AjaxResult getDetail(@PathVariable("id")Integer id){
        return success(awardService.selectAwardDetailById(id));
    }

    /**
     * 新增奖项
     */
    // @PreAuthorize("@ss.hasPermi('lms:award:add')")
    // @Log(title = "奖项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Award award) {
        return toAjax(awardService.insertAward(award));
    }

    /**
     * 修改奖项
     */
    // @PreAuthorize("@ss.hasPermi('lms:award:edit')")
    // @Log(title = "奖项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Award award) {
        return toAjax(awardService.updateAward(award));
    }

    /**
     * 删除奖项
     */
    // @PreAuthorize("@ss.hasPermi('lms:award:remove')")
    // @Log(title = "奖项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(awardService.deleteAwardByIds(ids));
    }
}
