package com.lms.common.core.domain.entity;

import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 审核模板对象 t_audit_template
 *
 * @author whn
 * @date 2024-05-06
 */
@Data
public class TAuditTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 模板名称
     */
    @Excel(name = "模板名称")
    private String name;

    /**
     * 图片
     */
    @Excel(name = "图片")
    private String image;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;


    List<TAuditTemplateField> fieldList;
}

