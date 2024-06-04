package com.lms.common.core.domain.entity;


import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 【请填写功能名称】对象 t_audit_record
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Data
public class TAuditRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private Long templateId;


    private Long createUserId;

    /**
     * 状态，0：申请中，1：通过，2：不通过，3：撤销
     */
    @Excel(name = "状态，0：申请中，1：通过，2：不通过，3：撤销")
    private String status;

    /**
     * 审核人员ID，通过逗号分割
     */
    @Excel(name = "审核人员ID，通过逗号分割")
    private String auditorIds;

    /**
     * 当前审核人员ID
     */
    @Excel(name = "当前审核人员ID")
    private Long curAuditorId;


    private List<TAuditDetail> detail;


    private SysUser createUser;


    private List<SysUser> auditors;


    private TAuditTemplate template;

    
}

