package com.lms.common.core.domain.entity;

import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 比赛视频对象 competition_video
 *
 * @author whn
 * @date 2024-05-20
 */
@Data
public class CompetitionVideo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 图片路径
     */
    @Excel(name = "图片路径")
    private String url;

    /**
     * 年级
     */
    @Excel(name = "年级")
    private Integer grade;
}
