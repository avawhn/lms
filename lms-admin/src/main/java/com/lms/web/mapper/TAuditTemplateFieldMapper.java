package com.lms.web.mapper;

import com.lms.common.core.domain.entity.TAuditTemplateField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模板字段Mapper接口
 *
 * @author whn
 * @date 2024-05-06
 */
public interface TAuditTemplateFieldMapper {
    List<TAuditTemplateField> selectTAuditTemplateFieldByTemplateId(Long templateId);

    int insertTAuditTemplateFieldList(@Param("templateId") Long templateId,
                                      @Param("fieldList") List<TAuditTemplateField> fieldList);


    /**
     * 查询模板字段
     *
     * @param id 模板字段主键
     * @return 模板字段
     */
    public TAuditTemplateField selectTAuditTemplateFieldById(Long id);

    /**
     * 查询模板字段列表
     *
     * @param tAuditTemplateField 模板字段
     * @return 模板字段集合
     */
    public List<TAuditTemplateField> selectTAuditTemplateFieldList(TAuditTemplateField tAuditTemplateField);

    /**
     * 新增模板字段
     *
     * @param tAuditTemplateField 模板字段
     * @return 结果
     */
    public int insertTAuditTemplateField(TAuditTemplateField tAuditTemplateField);

    /**
     * 修改模板字段
     *
     * @param tAuditTemplateField 模板字段
     * @return 结果
     */
    public int updateTAuditTemplateField(TAuditTemplateField tAuditTemplateField);

    /**
     * 删除模板字段
     *
     * @param id 模板字段主键
     * @return 结果
     */
    public int deleteTAuditTemplateFieldById(Long id);

    /**
     * 批量删除模板字段
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAuditTemplateFieldByIds(Long[] ids);

}
