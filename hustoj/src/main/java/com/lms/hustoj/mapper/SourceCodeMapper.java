package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.SourceCode;

public interface SourceCodeMapper {
    int insertSourceCode(SourceCode code);

    SourceCode selectSourceCodeBySolutionId(Integer solutionId);
}
