package com.lms.hustoj.domain;

import lombok.Data;

import java.util.List;

/**
 * @author whn
 */
@Data
public class TestCode {
    private Integer problemId;
    private Integer language;
    private List<String> ans;
}
