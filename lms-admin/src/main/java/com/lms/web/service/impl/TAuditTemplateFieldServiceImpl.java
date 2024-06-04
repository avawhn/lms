package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.TAuditTemplateField;
import com.lms.web.mapper.TAuditTemplateFieldMapper;
import com.lms.web.service.ITAuditTemplateFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板字段Service业务层处理
 *
 * @author whn
 * @date 2024-05-06
 */
@Service
public class TAuditTemplateFieldServiceImpl implements ITAuditTemplateFieldService {
    @Autowired
    private TAuditTemplateFieldMapper tAuditTemplateFieldMapper;

    @Override
    public List<TAuditTemplateField> selectTAuditTemplateFieldByTemplateId(Long templateId) {
        return tAuditTemplateFieldMapper.selectTAuditTemplateFieldByTemplateId(templateId);
    }

    /**
     * 查询模板字段
     *
     * @param id 模板字段主键
     * @return 模板字段
     */
    @Override
    public TAuditTemplateField selectTAuditTemplateFieldById(Long id) {
        return tAuditTemplateFieldMapper.selectTAuditTemplateFieldById(id);
    }

    /**
     * 查询模板字段列表
     *
     * @param tAuditTemplateField 模板字段
     * @return 模板字段
     */
    @Override
    public List<TAuditTemplateField> selectTAuditTemplateFieldList(TAuditTemplateField tAuditTemplateField) {
        return tAuditTemplateFieldMapper.selectTAuditTemplateFieldList(tAuditTemplateField);
    }

    /**
     * 新增模板字段
     *
     * @param tAuditTemplateField 模板字段
     * @return 结果
     */
    @Override
    public int insertTAuditTemplateField(TAuditTemplateField tAuditTemplateField) {
        return tAuditTemplateFieldMapper.insertTAuditTemplateField(tAuditTemplateField);
    }

    /**
     * 修改模板字段
     *
     * @param tAuditTemplateField 模板字段
     * @return 结果
     */
    @Override
    public int updateTAuditTemplateField(TAuditTemplateField tAuditTemplateField) {
        return tAuditTemplateFieldMapper.updateTAuditTemplateField(tAuditTemplateField);
    }

    /**
     * 批量删除模板字段
     *
     * @param ids 需要删除的模板字段主键
     * @return 结果
     */
    @Override
    public int deleteTAuditTemplateFieldByIds(Long[] ids) {
        return tAuditTemplateFieldMapper.deleteTAuditTemplateFieldByIds(ids);
    }

    /**
     * 删除模板字段信息
     *
     * @param id 模板字段主键
     * @return 结果
     */
    @Override
    public int deleteTAuditTemplateFieldById(Long id) {
        return tAuditTemplateFieldMapper.deleteTAuditTemplateFieldById(id);
    }
}
