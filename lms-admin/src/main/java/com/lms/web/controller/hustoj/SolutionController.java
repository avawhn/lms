package com.lms.web.controller.hustoj;


import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.page.TableDataInfo;
import com.lms.hustoj.domain.PassRateCount;
import com.lms.hustoj.domain.Solution;
import com.lms.hustoj.domain.SourceCode;
import com.lms.hustoj.domain.TestCode;
import com.lms.hustoj.service.SolutionService;
import com.lms.hustoj.service.SourceCodeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/hustoj/solution")
@RestController
public class SolutionController extends BaseController {
    @Resource
    private SolutionService solutionService;

    @Resource
    private SourceCodeService sourceCodeService;


    @GetMapping("/list")
    public TableDataInfo list(Solution solution) {
        startPage();
        List<Solution> list = solutionService.selectSolutionList(solution);
        return getDataTable(list);
    }

    @GetMapping("/record/list")
    public TableDataInfo listRecord(Solution solution) {
        startPage();
        List<Solution> list = solutionService.selectSolutionRecordList(solution);
        return getDataTable(list);
    }

    @GetMapping("/{solutionId}")
    public AjaxResult getSolutionBySolutionId(@PathVariable("solutionId") Integer solutionId) {
        Solution solution = solutionService.selectSolutionBySolutionId(solutionId);
        return AjaxResult.success(solution);
    }

    @GetMapping("/detail/{solutionId}")
    public AjaxResult getSolutionDetailBySolutionId(@PathVariable("solutionId") Integer solutionId) {
        Solution solution = solutionService.selectSolutionBySolutionId(solutionId);
        SourceCode sourceCode = sourceCodeService.selectSourceCodeBySolutionId(solutionId);
        solution.setSourceCode(sourceCode);
        return AjaxResult.success(solution);
    }


    @PostMapping
    public AjaxResult add(@RequestBody Solution solution) {
        int res = solutionService.insertSolution(solution);
        return AjaxResult.success(res);
    }


    @PostMapping("/test")
    public AjaxResult test(@RequestBody TestCode testCode) {
        SourceCode sourceCode = new SourceCode();
        sourceCode.setSource(testCode.getAns().get(0));

        Solution solution = new Solution();
        solution.setLanguage(testCode.getLanguage());
        solution.setSourceCode(sourceCode);
        solution.setProblemId(testCode.getProblemId());

        int res = solutionService.insertSolution(solution);
        return AjaxResult.success(res);
    }

    @GetMapping("/passRate/statistics/{userId}")
    public AjaxResult passRateStatistics(@PathVariable("userId") String userId) {
        List<PassRateCount> statistics = solutionService.selectPassRateCount(userId);
        return AjaxResult.success(statistics);
    }
}
