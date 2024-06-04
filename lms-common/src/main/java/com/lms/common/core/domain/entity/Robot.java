package com.lms.common.core.domain.entity;

import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 机器人对象 robot
 *
 * @author whn
 * @date 2024-05-20
 */
@Data
public class Robot extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 机器人名称
     */
    @Excel(name = "机器人名称")
    private String robotName;

    /**
     * 年级
     */
    @Excel(name = "年级")
    private Long grade;

    /**
     * 负责内容
     */
    @Excel(name = "负责内容")
    private String workContent;

    /**
     * 机器人图片
     */
    @Excel(name = "机器人图片")
    private String image;
}
