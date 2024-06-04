package com.lms.common.core.domain.entity;

import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 模板字段对象 t_audit_template_field
 *
 * @author whn
 * @date 2024-05-06
 */
@Data
public class TAuditTemplateField extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 字段ID
     */
    private Long id;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 字段标注
     */
    @Excel(name = "字段标注")
    private String label;

    /**
     * 组件名称
     */
    @Excel(name = "组件名称")
    private String componentName;

    /**
     * 占位符
     */
    @Excel(name = "占位符")
    private String placeholder;

    /**
     * 是否必填
     */
    @Excel(name = "是否必填")
    private Boolean required;
}
