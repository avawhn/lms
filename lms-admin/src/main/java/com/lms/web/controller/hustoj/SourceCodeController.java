package com.lms.web.controller.hustoj;

import com.lms.common.core.domain.AjaxResult;
import com.lms.hustoj.domain.SourceCode;
import com.lms.hustoj.service.SourceCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author whn
 */
@RequestMapping("/hustoj/source")
@RestController
public class SourceCodeController {
    @Resource
    private SourceCodeService sourceCodeService;


    @GetMapping("/{solutionId}")
    public AjaxResult getBySolutionId(@PathVariable("solutionId") Integer solutionId) {
        SourceCode sourceCode = sourceCodeService.selectSourceCodeBySolutionId(solutionId);
        return AjaxResult.success(sourceCode);
    }

}
