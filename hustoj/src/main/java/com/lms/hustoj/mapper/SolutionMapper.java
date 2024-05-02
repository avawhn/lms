package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.Solution;

public interface SolutionMapper {
    Solution selectSolutionBySolutionId(int solutionId);
}
