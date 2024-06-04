package com.lms.web.mapper;

import com.lms.common.core.domain.entity.TAuditTemplate;

import java.util.List;

/**
 * 审核模板Mapper接口
 *
 * @author whn
 * @date 2024-05-06
 */
public interface TAuditTemplateMapper {
    /**
     * 查询审核模板
     *
     * @param id 审核模板主键
     * @return 审核模板
     */
    public TAuditTemplate selectTAuditTemplateById(Long id);

    /**
     * 查询审核模板列表
     *
     * @param tAuditTemplate 审核模板
     * @return 审核模板集合
     */
    public List<TAuditTemplate> selectTAuditTemplateList(TAuditTemplate tAuditTemplate);

    /**
     * 新增审核模板
     *
     * @param tAuditTemplate 审核模板
     * @return 结果
     */
    public int insertTAuditTemplate(TAuditTemplate tAuditTemplate);

    /**
     * 修改审核模板
     *
     * @param tAuditTemplate 审核模板
     * @return 结果
     */
    public int updateTAuditTemplate(TAuditTemplate tAuditTemplate);

    /**
     * 删除审核模板
     *
     * @param id 审核模板主键
     * @return 结果
     */
    public int deleteTAuditTemplateById(Long id);

    /**
     * 批量删除审核模板
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAuditTemplateByIds(Long[] ids);
}
