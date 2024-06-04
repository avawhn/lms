package com.lms.common.core.domain.entity;

import lombok.Data;

import java.util.List;

/**
 * @author whn
 */
@Data
public class LaboratoryInfoVo {
    private String name;
    private String description;
    private List<LaboratoryInfo> imageList;
}
