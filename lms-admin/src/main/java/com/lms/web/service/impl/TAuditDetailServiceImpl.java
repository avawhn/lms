package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.TAuditDetail;
import com.lms.web.mapper.TAuditDetailMapper;
import com.lms.web.service.ITAuditDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
public class TAuditDetailServiceImpl implements ITAuditDetailService
{
    @Autowired
    private TAuditDetailMapper tAuditDetailMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TAuditDetail selectTAuditDetailById(Long id)
    {
        return tAuditDetailMapper.selectTAuditDetailById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tAuditDetail 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TAuditDetail> selectTAuditDetailList(TAuditDetail tAuditDetail)
    {
        return tAuditDetailMapper.selectTAuditDetailList(tAuditDetail);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param tAuditDetail 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTAuditDetail(TAuditDetail tAuditDetail)
    {
        return tAuditDetailMapper.insertTAuditDetail(tAuditDetail);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param tAuditDetail 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTAuditDetail(TAuditDetail tAuditDetail)
    {
        return tAuditDetailMapper.updateTAuditDetail(tAuditDetail);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTAuditDetailByIds(Long[] ids)
    {
        return tAuditDetailMapper.deleteTAuditDetailByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTAuditDetailById(Long id)
    {
        return tAuditDetailMapper.deleteTAuditDetailById(id);
    }
}
