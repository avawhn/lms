package com.lms.hustoj.service;

import com.lms.hustoj.domain.SourceCode;

/**
 * @author whn
 */
public interface SourceCodeService {
    SourceCode selectSourceCodeBySolutionId(Integer solutionId);
}
