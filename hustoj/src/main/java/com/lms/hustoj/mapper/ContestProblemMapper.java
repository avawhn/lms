package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.ContestProblem;

import java.util.List;

/**
 * @author whn
 */
public interface ContestProblemMapper {
    int insertContestProblemList(List<ContestProblem> problemList);

    List<ContestProblem> selectContestProblemList(ContestProblem contestProblem);

}
