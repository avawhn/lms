package com.lms.common.core.domain.entity;

import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * award
 *
 * @author whn
 * @date 2024-05-20
 */
@Data
public class Award extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "年级")
    private Long grade;

    @Excel(name = "奖项内容")
    private String content;

    @Excel(name = "奖项图片")
    private String image;

    @Excel(name = "描述")
    private String description;

    private List<Robot> robotList;
}
