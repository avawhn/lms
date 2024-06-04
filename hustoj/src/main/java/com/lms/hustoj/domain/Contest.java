package com.lms.hustoj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author whn
 */
@Data
public class Contest {
    private Integer contestId;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Character defunct;
    private String description;
    private Integer tinyint;
    private Integer langmask;
    private String password;
    private String userId;
    private Integer contestTime;
    private Integer totalScore;

    private List<ContestProblem> problemList;
}
