package com.lms.hustoj.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author whn
 */
@Data
public class Problem {
    private Integer problemId;
    private String title;
    private String description;
    private String input;
    private String output;
    private String sampleInput;
    private String sampleOutput;
    private Character spj;
    private String hint;
    private String source;
    private Date inDate;
    private BigDecimal timeLimit;
    private Integer memoryLimit;
    private Character defunct;
    private Integer accepted;
    private Integer submit;
    private Integer solved;
    private String remoteOj;
    private String remoteId;
    private Character type;

    private List<ProblemAnswer> answerList;
}
