package com.lms.hustoj.service;

import com.lms.hustoj.domain.PassRateCount;
import com.lms.hustoj.domain.Solution;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author whn
 */
@Service
public interface SolutionService {
    Solution selectSolutionBySolutionId(int solutionId);

    int insertSolution(Solution solution);

    List<Solution> selectSolutionList(Solution solution);

    List<Solution> selectSolutionRecordList(Solution solution);

    List<PassRateCount> selectPassRateCount(String userId);
}
