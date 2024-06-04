package com.lms.hustoj.service;

import com.lms.hustoj.domain.Problem;

import java.util.List;

/**
 * @author whn
 */
public interface ProblemService {
    int insertProblem(Problem problem);

    List<Problem> selectProblemListWithAns(Problem problem);

    List<Problem> selectProblemList(Problem problem);

    int deleteProblemByIds(Integer[] ids);

    int updateProblem(Problem problem);

    Problem selectProblemByProblemId(Integer id);
}
