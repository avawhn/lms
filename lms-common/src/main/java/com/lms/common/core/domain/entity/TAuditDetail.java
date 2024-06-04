package com.lms.common.core.domain.entity;

import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 【请填写功能名称】对象 t_audit_detail
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Data
public class TAuditDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 标签
     */
    @Excel(name = "标签")
    private String label;

    /**
     * 值
     */
    @Excel(name = "值")
    private String value;

    /**
     * 审核ID
     */
    @Excel(name = "审核ID")
    private Long recordId;
}
