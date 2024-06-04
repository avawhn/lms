package com.lms.web.service;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.TAuditRecord;

import java.util.List;

/**
 * 审核记录Service接口
 *
 * @author ruoyi
 * @date 2024-05-09
 */
public interface ITAuditRecordService {

    List<TAuditRecord> selectTAuditRecordListByAuditorId(Long id);


    AjaxResult insertTAuditRecordV2(TAuditRecord tAuditRecord);

    AjaxResult revokeTAuditRecord(Long recordId);

    AjaxResult passTAuditRecord(Long recordId);

    AjaxResult refuseTAuditRecord(Long recordId);



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
     * 批量删除审核记录
     *
     * @param ids 需要删除的审核记录主键集合
     * @return 结果
     */
    public int deleteTAuditRecordByIds(Long[] ids);

    /**
     * 删除审核记录信息
     *
     * @param id 审核记录主键
     * @return 结果
     */
    public int deleteTAuditRecordById(Long id);

}
