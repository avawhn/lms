package com.lms.web.mapper;

import com.lms.common.core.domain.MaterialChangeStatisticsEntity;
import com.lms.common.core.domain.entity.MaterialChangeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料变更Mapper接口
 *
 * @author whn
 * @date 2024-05-21
 */
public interface MaterialChangeRecordMapper {
    /**
     * 查询物料变更
     *
     * @param id 物料变更主键
     * @return 物料变更
     */
    public MaterialChangeRecord selectMaterialChangeRecordById(Integer id);

    /**
     * 查询物料变更列表
     *
     * @param materialChangeRecord 物料变更
     * @return 物料变更集合
     */
    public List<MaterialChangeRecord> selectMaterialChangeRecordList(MaterialChangeRecord materialChangeRecord);

    /**
     * 新增物料变更
     *
     * @param materialChangeRecord 物料变更
     * @return 结果
     */
    public int insertMaterialChangeRecord(MaterialChangeRecord materialChangeRecord);

    /**
     * 修改物料变更
     *
     * @param materialChangeRecord 物料变更
     * @return 结果
     */
    public int updateMaterialChangeRecord(MaterialChangeRecord materialChangeRecord);

    /**
     * 删除物料变更
     *
     * @param id 物料变更主键
     * @return 结果
     */
    public int deleteMaterialChangeRecordById(Integer id);

    /**
     * 批量删除物料变更
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialChangeRecordByIds(Integer[] ids);


    List<Integer> selectAllYear();

    List<MaterialChangeStatisticsEntity> statisticsYear(@Param("year") Integer year,
                                                        @Param("type") Character type);
}
