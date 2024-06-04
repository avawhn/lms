package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.ContestProblem;
import com.lms.hustoj.mapper.ContestProblemMapper;
import com.lms.hustoj.mapper.ProblemMapper;
import com.lms.hustoj.service.ContestProblemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whn
 */
@Service
public class ContestProblemServiceImpl implements ContestProblemService {

    @Resource
    private ContestProblemMapper contestProblemMapper;

    @Resource
    private ProblemMapper problemMapper;

    @Override
    public int insertContestProblemList(List<ContestProblem> problemList) {
        return contestProblemMapper.insertContestProblemList(problemList);
    }

    @Override
    public List<ContestProblem> selectContestProblemList(ContestProblem contestProblem) {
        return contestProblemMapper.selectContestProblemList(contestProblem);
    }
}
