package com.lms.web.service.impl;

import com.lms.common.utils.DateUtils;
import com.lms.common.core.domain.entity.Material;
import com.lms.web.mapper.MaterialMapper;
import com.lms.web.service.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物料管理Service业务层处理
 *
 * @author whn
 * @date 2024-02-21
 */
@Service
public class MaterialServiceImpl implements IMaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 查询物料管理
     *
     * @param id 物料管理主键
     * @return 物料管理
     */
    @Override
    public Material selectMaterialById(Long id) {
        return materialMapper.selectMaterialByMaterialId(id);
    }

    /**
     * 查询物料管理列表
     *
     * @param material 物料管理
     * @return 物料管理
     */
    @Override
    public List<Material> selectMaterialList(Material material) {
        return materialMapper.selectMaterialList(material);
    }

    /**
     * 新增物料管理
     *
     * @param material 物料管理
     * @return 结果
     */
    @Override
    public int insertMaterial(Material material) {
        material.setCreateTime(DateUtils.getNowDate());
        return materialMapper.insertMaterial(material);
    }

    /**
     * 修改物料管理
     *
     * @param material 物料管理
     * @return 结果
     */
    @Override
    public int updateMaterial(Material material) {
        material.setUpdateTime(DateUtils.getNowDate());
        return materialMapper.updateMaterial(material);
    }

    /**
     * 批量删除物料管理
     *
     * @param ids 需要删除的物料管理主键
     * @return 结果
     */
    @Override
    public int deleteMaterialByIds(Long[] ids) {
        return materialMapper.deleteMaterialByMaterialIds(ids);
    }

    /**
     * 删除物料管理信息
     *
     * @param id 物料管理主键
     * @return 结果
     */
    @Override
    public int deleteMaterialById(Long id) {
        return materialMapper.deleteMaterialByMaterialId(id);
    }
}
