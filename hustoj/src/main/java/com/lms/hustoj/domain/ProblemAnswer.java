package com.lms.hustoj.domain;

import lombok.Data;

/**
 * @author whn
 */
@Data
public class ProblemAnswer {
    private Integer id;
    private Integer problemId;
    private String value;
    private Boolean correct;
}
