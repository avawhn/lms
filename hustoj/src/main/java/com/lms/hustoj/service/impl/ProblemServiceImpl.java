package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.Problem;
import com.lms.hustoj.domain.ProblemAnswer;
import com.lms.hustoj.mapper.ProblemAnswerMapper;
import com.lms.hustoj.mapper.ProblemMapper;
import com.lms.hustoj.service.ProblemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    @Resource
    private ProblemMapper problemMapper;

    @Resource
    private ProblemAnswerMapper answerMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertProblem(Problem problem) {
        problemMapper.insertProblem(problem);
        Integer problemId = problem.getProblemId();
        List<ProblemAnswer> answerList = problem.getAnswerList();
        answerList.forEach(answer -> {
            answer.setProblemId(problemId);
        });
        answerMapper.insertProblemAnswerList(answerList);
        return 1;
    }

    @Override
    public List<Problem> selectProblemListWithAns(Problem problem) {
        return problemMapper.selectProblemListWithAns(problem);
    }

    @Override
    public List<Problem> selectProblemList(Problem problem) {
        return problemMapper.selectProblemList(problem);
    }

    @Override
    public int deleteProblemByIds(Integer[] ids) {
        return problemMapper.deleteProblemByIds(ids);
    }

    @Override
    public int updateProblem(Problem problem) {
        return problemMapper.updateProblem(problem);
    }

    @Override
    public Problem selectProblemByProblemId(Integer id) {
        return problemMapper.selectProblemByProblemId(id);
    }
}
