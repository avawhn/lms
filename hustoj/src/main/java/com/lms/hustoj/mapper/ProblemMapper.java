package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.Problem;

import java.util.List;

/**
 * @author whn
 */
public interface ProblemMapper {
    int insertProblem(Problem problem);

    List<Problem> selectProblemList(Problem problem);

    List<Problem> selectProblemListWithAns(Problem problem);

    int deleteProblemByIds(Integer[] ids);

    int updateProblem(Problem problem);

    Problem selectProblemByProblemId(Integer id);


    Problem selectProblemWithAnsByProblemId(Integer id);


}
