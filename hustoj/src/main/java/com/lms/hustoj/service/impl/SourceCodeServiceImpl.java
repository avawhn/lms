package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.SourceCode;
import com.lms.hustoj.mapper.SourceCodeMapper;
import com.lms.hustoj.service.SourceCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author whn
 */
@Service
public class SourceCodeServiceImpl implements SourceCodeService {
    @Resource
    private SourceCodeMapper sourceCodeMapper;

    @Override
    public SourceCode selectSourceCodeBySolutionId(Integer solutionId) {
        return sourceCodeMapper.selectSourceCodeBySolutionId(solutionId);
    }
}
