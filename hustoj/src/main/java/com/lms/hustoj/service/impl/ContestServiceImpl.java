package com.lms.hustoj.service.impl;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.utils.DateUtils;
import com.lms.common.utils.SecurityUtils;
import com.lms.hustoj.domain.*;
import com.lms.hustoj.mapper.ContestMapper;
import com.lms.hustoj.mapper.ContestProblemMapper;
import com.lms.hustoj.mapper.ContestUserMapper;
import com.lms.hustoj.service.ContestService;
import com.lms.hustoj.service.SolutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author whn
 */
@Service
public class ContestServiceImpl implements ContestService {
    @Resource
    private ContestMapper contestMapper;


    @Resource
    private ContestUserMapper contestUserMapper;


    @Resource
    private ContestProblemMapper contestProblemMapper;

    @Resource
    private SolutionService solutionService;

    @Resource
    private DelayedUpdateScoreTaskService delayedUpdateScoreTaskService;


    private static final Logger log = LoggerFactory.getLogger(ContestServiceImpl.class);


    @Override
    public Contest selectContestByContestId(Integer contestId) {
        return contestMapper.selectContestByContestId(contestId);
    }

    @Override
    public List<Contest> selectContestList(Contest contest) {
        return contestMapper.selectContestList(contest);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertContest(Contest contest) {
        List<ContestProblem> problemList = contest.getProblemList();
        AtomicInteger totalScore = new AtomicInteger();
        problemList.forEach(p -> totalScore.addAndGet(p.getScore()));
        contest.setTotalScore(totalScore.get());
        contestMapper.insertContest(contest);


        Integer contestId = contest.getContestId();
        problemList.forEach(p -> p.setContestId(contestId));

        contestProblemMapper.insertContestProblemList(problemList);

        List<ContestUser> contestUserList = Arrays.stream(contest.getUserId().split(","))
                .map(id -> {
                    ContestUser user = new ContestUser();
                    user.setContestId(contestId);
                    user.setUserId(Integer.parseInt(id));
                    user.setScore(0);
                    user.setStatus('0');
                    return user;
                }).collect(Collectors.toList());

        contestUserMapper.insertContestUserList(contestUserList);

        return 1;
    }

    @Override
    public int deleteContestByContestIds(Long[] ids) {
        return contestMapper.deleteContestByContestIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult computeScore(ContestSubmit submit) {
        int userId = Math.toIntExact(SecurityUtils.getLoginUser().getUser().getUserId());
        ContestUser contestUser = contestUserMapper.selectContestUserByContestIdAndUserId(
                submit.getContestId(),
                userId
        );

        if (contestUser.getStatus() == '1') {
            return AjaxResult.error("用户已提交");
        }


        ContestProblem contestProblem = new ContestProblem();
        contestProblem.setContestId(submit.getContestId());

        List<ContestProblem> list = contestProblemMapper.selectContestProblemList(contestProblem);
        HashMap<Integer, ContestProblem> map = new HashMap<>(list.size());
        list.forEach(p -> {
            map.put(p.getProblemId(), p);
        });

        List<ContestSubmitAns> ansList = submit.getAnsList();

        // 计算分数
        AtomicInteger testScore = new AtomicInteger();
        ansList.forEach(ans -> {
            ContestProblem contestProblem1 = map.get(ans.getProblemId());
            Integer score = contestProblem1.getScore();
            Problem problem = contestProblem1.getProblem();
            char type = problem.getType();

            int problemScore = 0;
            if (type == '1') {
                if (ans.getAns().size() == 1) {
                    problemScore = this.computeSingleChoice(ans.getAns().get(0), problem.getAnswerList(), score);
                }
            } else if (type == '2') {
                if (!ans.getAns().isEmpty()) {
                    problemScore = this.computeMultiChoice(ans.getAns(), problem.getAnswerList(), score);
                }
            } else if (type == '3') {
                problemScore = this.computeBlank(ans.getAns(), problem.getAnswerList(), score);
            } else {
                if (!ans.getAns().isEmpty()) {
                    this.computeAlgorithm(ans, submit.getContestId(), userId, score);
                }
            }

            testScore.addAndGet(problemScore);
        });


        contestUser.setStatus('1');
        contestUser.setScore(testScore.get());
        contestUser.setSubmitTime(DateUtils.getNowDate());
        // 更新提交用户
        contestUserMapper.updateContestUser(contestUser);

        return AjaxResult.success();
    }

    private int computeSingleChoice(String str, List<ProblemAnswer> ansList, int score) {
        String correct = null;
        for (ProblemAnswer ans : ansList) {
            if (ans.getCorrect()) {
                correct = ans.getValue();
                break;
            }
        }
        return correct != null && correct.equals(str) ? score : 0;
    }

    private int computeMultiChoice(List<String> strList, List<ProblemAnswer> answerList, int score) {
        int correctCount = 0;
        Set<String> correctSet = new HashSet<>();
        answerList.forEach(ans -> {
            if (ans.getCorrect()) {
                correctSet.add(ans.getValue());
            }
        });

        for (String s : strList) {
            if (!correctSet.contains(s)) {
                correctCount = 0;
                break;
            } else {
                correctCount++;
                correctSet.remove(s);
            }
        }

        return correctCount * score / (correctSet.size() + correctCount);
    }

    private int computeBlank(List<String> strList, List<ProblemAnswer> ans, int score) {
        if (strList.size() != ans.size()) {
            return 0;
        }
        int correctCount = 0;
        for (int i = 0; i < ans.size(); i++) {
            if (strList.get(i).equals(ans.get(i).getValue())) {
                correctCount++;
            }
        }
        return correctCount * score / ans.size();
    }

    private void computeAlgorithm(ContestSubmitAns ans, int contestId, int userId, int score) {
        SourceCode sourceCode = new SourceCode();
        sourceCode.setSource(ans.getAns().get(0));

        Solution solution = new Solution();
        solution.setLanguage(ans.getLanguage());
        solution.setProblemId(ans.getProblemId());
        solution.setSourceCode(sourceCode);

        int solutionId = solutionService.insertSolution(solution);

        SubmitInfo submitInfo = new SubmitInfo();
        submitInfo.setSolutionId(solutionId);
        submitInfo.setContestId(contestId);
        submitInfo.setUserId(userId);
        submitInfo.setScore(score);

        delayedUpdateScoreTaskService.getQueue().offer(submitInfo);
        delayedUpdateScoreTaskService.callDelayedUpdateScore();
    }
}
