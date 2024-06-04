package com.lms.web.controller.hustoj;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.SysUser;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.hustoj.domain.Contest;
import com.lms.hustoj.domain.ContestProblem;
import com.lms.hustoj.domain.ContestSubmit;
import com.lms.hustoj.domain.ContestUser;
import com.lms.hustoj.service.ContestProblemService;
import com.lms.hustoj.service.ContestService;
import com.lms.hustoj.service.ContestUserService;
import com.lms.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author whn
 */
@RequestMapping("/hustoj/contest")
@RestController
public class ContestController extends BaseController {
    @Resource
    private ContestService contestService;

    @Resource
    private ContestProblemService contestProblemService;

    @Resource
    private ContestUserService contestUserService;

    @Resource
    private ISysUserService userService;


    @GetMapping("/list")
    public TableDataInfo list(Contest contest) {
        startPage();
        List<Contest> list = contestService.selectContestList(contest);
        return getDataTable(list);
    }

    @GetMapping("/{contestId}")
    public AjaxResult getContestByContestId(@PathVariable("contestId") Integer contestId) {
        Contest contest = contestService.selectContestByContestId(contestId);
        return AjaxResult.success(contest);
    }

    @GetMapping("/user/list")
    public TableDataInfo getUserContestByUserId(ContestUser contestUser) {
        startPage();
        List<ContestUser> list = contestUserService.selectUserContest(contestUser.getUserId());
        return getDataTable(list);
    }

    @GetMapping("/detail/{contestId}")
    public AjaxResult getContestDetailByContestId(@PathVariable("contestId") Integer contestId) {
        Contest contest = contestService.selectContestByContestId(contestId);
        ContestProblem contestProblem = new ContestProblem();
        contestProblem.setContestId(contestId);
        List<ContestProblem> list = contestProblemService.selectContestProblemList(contestProblem);
        contest.setProblemList(list);
        return AjaxResult.success(contest);
    }


    @PostMapping
    public AjaxResult add(@RequestBody Contest contest) {
        return toAjax(contestService.insertContest(contest));
    }


    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(contestService.deleteContestByContestIds(ids));
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody ContestSubmit submit) {
        return contestService.computeScore(submit);
    }

    @PostMapping("/export/score/{contestId}")
    public void export(HttpServletResponse response, @PathVariable("contestId") Integer contestId) {
        ExcelUtil<ContestUser> util = new ExcelUtil<ContestUser>(ContestUser.class);
        ContestUser contestUser = new ContestUser();
        contestUser.setContestId(contestId);
        List<ContestUser> list = contestUserService.selectContestUserList(contestUser);
        list.forEach(u -> {
            SysUser user = userService.selectUserById(Long.valueOf(u.getUserId()));
            u.setUser(user);
        });
        util.exportExcel(response, list, "日报管理数据");
    }
}
