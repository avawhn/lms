package com.lms.hustoj.service;

import com.lms.hustoj.domain.ContestProblem;

import java.util.List;

/**
 * @author whn
 */
public interface ContestProblemService {
    int insertContestProblemList(List<ContestProblem> problemList);

    List<ContestProblem> selectContestProblemList(ContestProblem contestProblem);

}
