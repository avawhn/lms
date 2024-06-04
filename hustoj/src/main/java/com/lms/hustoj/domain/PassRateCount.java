package com.lms.hustoj.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author whn
 */
@Data
public class PassRateCount {
    private BigDecimal passRate;
    private Integer count;
}
