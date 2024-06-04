package com.lms.web.mapper;

import com.lms.common.core.domain.entity.TAuditDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审核详情Mapper接口
 *
 * @author ruoyi
 * @date 2024-05-09
 */
public interface TAuditDetailMapper {
    int insertTAuditDetailList(@Param("detail") List<TAuditDetail> detail);


    List<TAuditDetail> selectTAuditDetailByRecordId(Long recordId);

    /**
     * 查询审核详情
     *
     * @param id 审核详情主键
     * @return 审核详情
     */
    public TAuditDetail selectTAuditDetailById(Long id);

    /**
     * 查询审核详情列表
     *
     * @param tAuditDetail 审核详情
     * @return 审核详情集合
     */
    public List<TAuditDetail> selectTAuditDetailList(TAuditDetail tAuditDetail);

    /**
     * 新增审核详情
     *
     * @param tAuditDetail 审核详情
     * @return 结果
     */
    public int insertTAuditDetail(TAuditDetail tAuditDetail);

    /**
     * 修改审核详情
     *
     * @param tAuditDetail 审核详情
     * @return 结果
     */
    public int updateTAuditDetail(TAuditDetail tAuditDetail);

    /**
     * 删除审核详情
     *
     * @param id 审核详情主键
     * @return 结果
     */
    public int deleteTAuditDetailById(Long id);

    /**
     * 批量删除审核详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAuditDetailByIds(Long[] ids);

}
