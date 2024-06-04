package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author whn
 */
@Data
public class Article extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Long userId;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    private SysUser user;
}

