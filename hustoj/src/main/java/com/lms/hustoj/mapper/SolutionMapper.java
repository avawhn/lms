package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.PassRateCount;
import com.lms.hustoj.domain.Solution;

import java.util.List;

public interface SolutionMapper {
    Solution selectSolutionBySolutionId(int solutionId);

    int insertSolution(Solution solution);

    int updateSolution(Solution solution);

    List<Solution> selectSolutionList(Solution solution);

    List<Solution> selectSolutionRecordList(Solution solution);

    List<PassRateCount> selectPassRateCount(String userId);
}
