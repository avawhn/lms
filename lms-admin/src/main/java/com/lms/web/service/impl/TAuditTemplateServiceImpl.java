package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.TAuditTemplate;
import com.lms.web.mapper.TAuditTemplateFieldMapper;
import com.lms.web.mapper.TAuditTemplateMapper;
import com.lms.web.service.ITAuditTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审核模板Service业务层处理
 *
 * @author whn
 * @date 2024-05-06
 */
@Service
public class TAuditTemplateServiceImpl implements ITAuditTemplateService {
    @Autowired
    private TAuditTemplateMapper tAuditTemplateMapper;

    @Resource
    private TAuditTemplateFieldMapper fieldMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertTAuditTemplateV2(TAuditTemplate tAuditTemplate) {
        tAuditTemplateMapper.insertTAuditTemplate(tAuditTemplate);
        fieldMapper.insertTAuditTemplateFieldList(tAuditTemplate.getId(), tAuditTemplate.getFieldList());
        return 1;
    }

    /**
     * 查询审核模板
     *
     * @param id 审核模板主键
     * @return 审核模板
     */
    @Override
    public TAuditTemplate selectTAuditTemplateById(Long id) {
        return tAuditTemplateMapper.selectTAuditTemplateById(id);
    }

    /**
     * 查询审核模板列表
     *
     * @param tAuditTemplate 审核模板
     * @return 审核模板
     */
    @Override
    public List<TAuditTemplate> selectTAuditTemplateList(TAuditTemplate tAuditTemplate) {
        return tAuditTemplateMapper.selectTAuditTemplateList(tAuditTemplate);
    }

    /**
     * 新增审核模板
     *
     * @param tAuditTemplate 审核模板
     * @return 结果
     */
    @Override
    public int insertTAuditTemplate(TAuditTemplate tAuditTemplate) {
        return tAuditTemplateMapper.insertTAuditTemplate(tAuditTemplate);
    }

    /**
     * 修改审核模板
     *
     * @param tAuditTemplate 审核模板
     * @return 结果
     */
    @Override
    public int updateTAuditTemplate(TAuditTemplate tAuditTemplate) {
        return tAuditTemplateMapper.updateTAuditTemplate(tAuditTemplate);
    }

    /**
     * 批量删除审核模板
     *
     * @param ids 需要删除的审核模板主键
     * @return 结果
     */
    @Override
    public int deleteTAuditTemplateByIds(Long[] ids) {
        return tAuditTemplateMapper.deleteTAuditTemplateByIds(ids);
    }

    /**
     * 删除审核模板信息
     *
     * @param id 审核模板主键
     * @return 结果
     */
    @Override
    public int deleteTAuditTemplateById(Long id) {
        return tAuditTemplateMapper.deleteTAuditTemplateById(id);
    }
}
