package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.common.annotation.Excel;
import com.lms.common.annotation.Excels;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 物料变更对象 material_change_record
 *
 * @author whn
 * @date 2024-05-21
 */
@Data
public class MaterialChangeRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 物料ID
     */
    private Long materialId;


    /**
     * 用户ID
     */
    private Long userId;

    @Excels({@Excel(name = "用户", targetAttr = "nickName", type = Excel.Type.EXPORT)})
    private SysUser user;

    @Excels({@Excel(name = "物料", targetAttr = "materialName", type = Excel.Type.EXPORT)})
    private Material material;


    /**
     * 变化数量
     */
    @Excel(name = "变化数量")
    private Integer number;

    /**
     * 类型：0：入库，1：消耗
     */
    @Excel(name = "类型：0：入库，1：消耗")
    private String type;


    @Excel(name = "用途")
    private String work;


    @Excel(name = "变更时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
