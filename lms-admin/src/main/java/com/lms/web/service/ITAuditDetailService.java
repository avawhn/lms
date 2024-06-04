package com.lms.web.service;

import com.lms.common.core.domain.entity.TAuditDetail;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
public interface ITAuditDetailService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TAuditDetail selectTAuditDetailById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tAuditDetail 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TAuditDetail> selectTAuditDetailList(TAuditDetail tAuditDetail);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tAuditDetail 【请填写功能名称】
     * @return 结果
     */
    public int insertTAuditDetail(TAuditDetail tAuditDetail);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tAuditDetail 【请填写功能名称】
     * @return 结果
     */
    public int updateTAuditDetail(TAuditDetail tAuditDetail);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTAuditDetailByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTAuditDetailById(Long id);
}
