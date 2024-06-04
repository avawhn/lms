package com.lms.web.service.impl;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.TAuditDetail;
import com.lms.common.core.domain.entity.TAuditRecord;
import com.lms.common.utils.DateUtils;
import com.lms.common.utils.SecurityUtils;
import com.lms.web.mapper.TAuditDetailMapper;
import com.lms.web.mapper.TAuditRecordMapper;
import com.lms.web.service.ITAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审核记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
public class TAuditRecordServiceImpl implements ITAuditRecordService {
    @Autowired
    private TAuditRecordMapper tAuditRecordMapper;


    @Resource
    private TAuditDetailMapper auditDetailMapper;

    @Override
    public List<TAuditRecord> selectTAuditRecordListByAuditorId(Long id) {
        return tAuditRecordMapper.selectTAuditRecordListByAuditorId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult insertTAuditRecordV2(TAuditRecord tAuditRecord) {
        tAuditRecord.setStatus("0");
        tAuditRecord.setCreateTime(DateUtils.getNowDate());

        String[] ids = tAuditRecord.getAuditorIds().split(",");
        tAuditRecord.setCurAuditorId(Long.parseLong(ids[ids.length - 1]));


        tAuditRecordMapper.insertTAuditRecord(tAuditRecord);

        List<TAuditDetail> detail = tAuditRecord.getDetail();
        detail.forEach(item -> {
            item.setRecordId(tAuditRecord.getId());
        });

        auditDetailMapper.insertTAuditDetailList(detail);


        return AjaxResult.success();
    }

    @Override
    public AjaxResult revokeTAuditRecord(Long recordId) {
        TAuditRecord record = new TAuditRecord();
        record.setId(recordId);
        record.setStatus("3");
        tAuditRecordMapper.updateTAuditRecord(record);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult passTAuditRecord(Long recordId) {
        TAuditRecord record = tAuditRecordMapper.selectTAuditRecordById(recordId);

        if ("3".equals(record.getStatus())) {
            return AjaxResult.error("申请已被发起者撤销");
        }

        String[] ids = record.getAuditorIds().split(",");

        TAuditRecord target = new TAuditRecord();
        target.setId(recordId);

        String userId = SecurityUtils.getLoginUser().getUserId().toString();

        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equals(userId)) {
                int next = i + 1;
                if (next >= ids.length) {
                    target.setStatus("1");
                } else {
                    target.setCurAuditorId(Long.parseLong(ids[next]));
                }
                break;
            }
        }

        tAuditRecordMapper.updateTAuditRecord(target);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult refuseTAuditRecord(Long recordId) {
        TAuditRecord record = tAuditRecordMapper.selectTAuditRecordById(recordId);
        if ("3".equals(record.getStatus())) {
            return AjaxResult.error("申请已被发起者撤销");
        }


        TAuditRecord target = new TAuditRecord();
        target.setId(recordId);
        target.setStatus("2");
        tAuditRecordMapper.updateTAuditRecord(target);
        return AjaxResult.success();
    }

    /**
     * 查询审核记录
     *
     * @param id 审核记录主键
     * @return 审核记录
     */
    @Override
    public TAuditRecord selectTAuditRecordById(Long id) {
        return tAuditRecordMapper.selectTAuditRecordById(id);
    }

    /**
     * 查询审核记录列表
     *
     * @param tAuditRecord 审核记录
     * @return 审核记录
     */
    @Override
    public List<TAuditRecord> selectTAuditRecordList(TAuditRecord tAuditRecord) {
        return tAuditRecordMapper.selectTAuditRecordList(tAuditRecord);
    }

    /**
     * 新增审核记录
     *
     * @param tAuditRecord 审核记录
     * @return 结果
     */
    @Override
    public int insertTAuditRecord(TAuditRecord tAuditRecord) {
        tAuditRecord.setCreateTime(DateUtils.getNowDate());
        return tAuditRecordMapper.insertTAuditRecord(tAuditRecord);
    }

    /**
     * 修改审核记录
     *
     * @param tAuditRecord 审核记录
     * @return 结果
     */
    @Override
    public int updateTAuditRecord(TAuditRecord tAuditRecord) {
        return tAuditRecordMapper.updateTAuditRecord(tAuditRecord);
    }

    /**
     * 批量删除审核记录
     *
     * @param ids 需要删除的审核记录主键
     * @return 结果
     */
    @Override
    public int deleteTAuditRecordByIds(Long[] ids) {
        return tAuditRecordMapper.deleteTAuditRecordByIds(ids);
    }

    /**
     * 删除审核记录信息
     *
     * @param id 审核记录主键
     * @return 结果
     */
    @Override
    public int deleteTAuditRecordById(Long id) {
        return tAuditRecordMapper.deleteTAuditRecordById(id);
    }
}
