package com.lms.web.service.impl;

import com.lms.common.core.domain.MaterialChangeStatisticsEntity;
import com.lms.common.core.domain.entity.MaterialChangeRecord;
import com.lms.common.utils.DateUtils;
import com.lms.web.mapper.MaterialChangeRecordMapper;
import com.lms.web.mapper.MaterialMapper;
import com.lms.web.service.IMaterialChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物料变更Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-21
 */
@Service
public class MaterialChangeRecordServiceImpl implements IMaterialChangeRecordService {
    @Autowired
    private MaterialChangeRecordMapper materialChangeRecordMapper;


    @Resource
    private MaterialMapper materialMapper;

    /**
     * 查询物料变更
     *
     * @param id 物料变更主键
     * @return 物料变更
     */
    @Override
    public MaterialChangeRecord selectMaterialChangeRecordById(Integer id) {
        return materialChangeRecordMapper.selectMaterialChangeRecordById(id);
    }

    /**
     * 查询物料变更列表
     *
     * @param materialChangeRecord 物料变更
     * @return 物料变更
     */
    @Override
    public List<MaterialChangeRecord> selectMaterialChangeRecordList(MaterialChangeRecord materialChangeRecord) {
        return materialChangeRecordMapper.selectMaterialChangeRecordList(materialChangeRecord);
    }

    /**
     * 新增物料变更
     *
     * @param materialChangeRecord 物料变更
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertMaterialChangeRecord(MaterialChangeRecord materialChangeRecord) {
        Integer number = materialChangeRecord.getNumber();
        String type = materialChangeRecord.getType();
        materialMapper.updateMaterialCount(materialChangeRecord.getMaterialId(), "0".equals(type) ? number : -number);
        materialChangeRecord.setCreateTime(DateUtils.getNowDate());
        return materialChangeRecordMapper.insertMaterialChangeRecord(materialChangeRecord);
    }

    /**
     * 修改物料变更
     *
     * @param materialChangeRecord 物料变更
     * @return 结果
     */
    @Override
    public int updateMaterialChangeRecord(MaterialChangeRecord materialChangeRecord) {
        return materialChangeRecordMapper.updateMaterialChangeRecord(materialChangeRecord);
    }

    /**
     * 批量删除物料变更
     *
     * @param ids 需要删除的物料变更主键
     * @return 结果
     */
    @Override
    public int deleteMaterialChangeRecordByIds(Integer[] ids) {
        return materialChangeRecordMapper.deleteMaterialChangeRecordByIds(ids);
    }

    /**
     * 删除物料变更信息
     *
     * @param id 物料变更主键
     * @return 结果
     */
    @Override
    public int deleteMaterialChangeRecordById(Integer id) {
        return materialChangeRecordMapper.deleteMaterialChangeRecordById(id);
    }

    @Override
    public List<Integer> selectAllYear() {
        return materialChangeRecordMapper.selectAllYear();
    }

    @Override
    public List<MaterialChangeStatisticsEntity> statisticsYear(Integer year, Character type) {
        return materialChangeRecordMapper.statisticsYear(year,type);
    }
}
