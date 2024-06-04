package com.lms.hustoj.domain;

import lombok.Data;

import java.util.List;

/**
 * @author whn
 */
@Data
public class ContestSubmit {
    private Integer contestId;
    private List<ContestSubmitAns> ansList;
}
