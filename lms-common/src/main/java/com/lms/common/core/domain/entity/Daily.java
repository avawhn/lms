package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lms.common.annotation.Excel;
import com.lms.common.annotation.Excels;
import com.lms.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 日报管理对象 t_daily
 *
 * @author whn
 * @date 2024-03-25
 */
@Getter
@Setter
public class Daily extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    /**
     * 工作内容
     */
    @Excel(name = "工作内容")
    private String content;


    @Excels({
            @Excel(name = "姓名", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser user;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("date", getDate())
                .append("content", getContent())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}