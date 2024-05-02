package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.Solution;
import com.lms.hustoj.service.SolutionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author whn
 */
@Service
public class SolutionServiceImpl implements SolutionService {
    @Resource
    private com.lms.hustoj.mapper.SolutionMapper solutionMapper;

    @Override
    public Solution selectSolutionBySolutionId(int solutionId) {
        return solutionMapper.selectSolutionBySolutionId(solutionId);
    }
}
