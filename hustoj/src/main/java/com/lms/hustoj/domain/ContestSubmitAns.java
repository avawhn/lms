package com.lms.hustoj.domain;

import lombok.Data;

import java.util.List;

/**
 * @author whn
 */
@Data
public class ContestSubmitAns {
    private Integer problemId;
    private Integer language;
    private Character type;
    private List<String> ans;
}
