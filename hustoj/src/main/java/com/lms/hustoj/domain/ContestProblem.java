package com.lms.hustoj.domain;

import lombok.Data;

/**
 * @author whn
 */
@Data
public class ContestProblem {
    private Integer problemId;
    private Integer contestId;
    private Integer score;

    private Problem problem;
}
