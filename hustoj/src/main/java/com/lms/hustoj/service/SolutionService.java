package com.lms.hustoj.service;

import com.lms.hustoj.domain.Solution;

/**
 * @author whn
 */
public interface SolutionService {
    Solution selectSolutionBySolutionId(int solutionId);
}
