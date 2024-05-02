package com.lms.hustoj.domain;

import lombok.Data;

import java.sql.Timestamp;


/**
 * @author whn
 */
@Data
public class Solution {
    private Integer solutionId;
    private Integer problemId;
    private String userId;
    private String nick;
    private Integer time;
    private Integer memory;
    private Timestamp inDate;
    private Integer result;
    private Integer language;
    private String ip;
    private Integer contestId;
    private Integer valid;
    private Integer num;
    private Integer codeLength;
    private Timestamp judgetime;
    private Double passRate;
    private Integer lintError;
    private String judger;
    private String remoteOj;
    private String remoteId;
}
