package com.lms.web.controller.hustoj;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.page.TableDataInfo;
import com.lms.hustoj.domain.Problem;
import com.lms.hustoj.domain.ProblemAnswer;
import com.lms.hustoj.service.ProblemAnswerService;
import com.lms.hustoj.service.ProblemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whn
 */
@RequestMapping("/hustoj/problem")
@RestController
public class ProblemController extends BaseController {
    @Resource
    private ProblemService problemService;

    @Resource
    private ProblemAnswerService answerService;


    @GetMapping("/list")
    public TableDataInfo list(Problem problem) {
        startPage();
        List<Problem> list = problemService.selectProblemList(problem);
        list.forEach(item -> {
            ProblemAnswer ans = new ProblemAnswer();
            ans.setProblemId(item.getProblemId());
            List<ProblemAnswer> answerList = answerService.selectProblemAnswer(ans);
            item.setAnswerList(answerList);
        });
        return getDataTable(list);
    }


    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") Integer id) {
        Problem problem = problemService.selectProblemByProblemId(id);
        return AjaxResult.success(problem);
    }

    @PostMapping
    public AjaxResult add(@RequestBody Problem problem) {
        return toAjax(problemService.insertProblem(problem));
    }


    @PutMapping
    public AjaxResult update(@RequestBody Problem problem) {
        return toAjax(problemService.updateProblem(problem));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(problemService.deleteProblemByIds(ids));
    }
}
