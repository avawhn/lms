package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 物料管理对象 t_material
 *
 * @author whn
 * @date 2024-02-21
 */
@Setter
@Getter
@ToString
public class Material extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     * Long 64 位，JavaScript 最大 53 位，直接返回会导致溢出，序列化时转为字符串
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long materialId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 组id
     */
    @Excel(name = "组id")
    private Long groupId;

    /**
     * 组名称
     */
    @Excel(name = "组名称")
    private String groupName;

    /**
     * 图片url
     */
    @Excel(name = "图片url")
    private String imageUrl;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long materialNumber;

    /**
     * 负责人id
     */
    @Excel(name = "负责人id")
    private String chargePersonId;

    @Excel(name = "负责人名称")
    private String chargePersonName;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String telephone;
}
