package com.lms.web.mapper;

import com.lms.common.core.domain.entity.TAuditRecord;

import java.util.List;

/**
 * 审核记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-05-09
 */
public interface TAuditRecordMapper {
    List<TAuditRecord> selectTAuditRecordListByAuditorId(Long id);

    /**
     * 查询审核记录
     *
     * @param id 审核记录主键
     * @return 审核记录
     */
    public TAuditRecord selectTAuditRecordById(Long id);

    /**
     * 查询审核记录列表
     *
     * @param tAuditRecord 审核记录
     * @return 审核记录集合
     */
    public List<TAuditRecord> selectTAuditRecordList(TAuditRecord tAuditRecord);

    /**
     * 新增审核记录
     *
     * @param tAuditRecord 审核记录
     * @return 结果
     */
    public int insertTAuditRecord(TAuditRecord tAuditRecord);

    /**
     * 修改审核记录
     *
     * @param tAuditRecord 审核记录
     * @return 结果
     */
    public int updateTAuditRecord(TAuditRecord tAuditRecord);

    /**
     * 删除审核记录
     *
     * @param id 审核记录主键
     * @return 结果
     */
    public int deleteTAuditRecordById(Long id);

    /**
     * 批量删除审核记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAuditRecordByIds(Long[] ids);

}
