package com.lms.web.controller.lms;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.LaboratoryInfo;
import com.lms.common.core.domain.entity.LaboratoryInfoVo;
import com.lms.web.service.ILaboratoryInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author whn
 */
@RestController
@RequestMapping("/lms/laboratory/info")
public class LaboratoryInfoController extends BaseController {
    @Resource
    private ILaboratoryInfoService service;


    @GetMapping
    public AjaxResult getInfo() {
        return success(service.getLaboratoryInfo());
    }

    @PutMapping
    public AjaxResult update(@RequestBody LaboratoryInfoVo infoVo) {
        return success(service.updateInfo(infoVo));
    }

    @PostMapping
    public AjaxResult add(@RequestBody LaboratoryInfo info) {
        return success(service.addImage(info));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult delete(@PathVariable("ids") Integer[] ids) {
        return success(service.deleteImageByIds(ids));
    }
}
