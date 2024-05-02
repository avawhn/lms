package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 组对象 t_group
 *
 * @author whn
 * @date 2024-02-21
 */
@Setter
@Getter
@ToString
public class Group extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     * Long 64 位，JavaScript 最大 53 位，直接返回会导致溢出，序列化时转为字符串
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /**
     * 组名称
     */
    @Excel(name = "组名称")
    private String groupName;

    /**
     * 组工作内容
     */
    @Excel(name = "组工作内容")
    private String groupDuty;

    /**
     * 父id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Excel(name = "父id")
    private Long parentId;

    /**
     * 全路径
     */
    @Excel(name = "全路径")
    private String fullPath;

    /**
     *
     */
    @Excel(name = "排序")
    private Integer orderNum;
}
