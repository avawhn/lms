package com.lms.hustoj.domain;

import lombok.Data;

/**
 * @author whn
 */
@Data
public class SubmitInfo {
    private Integer contestId;
    private Integer userId;
    private Integer score;
    private Integer solutionId;
}
