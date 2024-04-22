package com.lms.web.service;

import com.lms.common.core.domain.entity.Material;

import java.util.List;

/**
 * 物料管理Service接口
 * 
 * @author whn
 * @date 2024-02-21
 */
public interface IMaterialService 
{
    /**
     * 查询物料管理
     * 
     * @param id 物料管理主键
     * @return 物料管理
     */
    public Material selectMaterialById(Long id);

    /**
     * 查询物料管理列表
     * 
     * @param material 物料管理
     * @return 物料管理集合
     */
    public List<Material> selectMaterialList(Material material);

    /**
     * 新增物料管理
     * 
     * @param material 物料管理
     * @return 结果
     */
    public int insertMaterial(Material material);

    /**
     * 修改物料管理
     * 
     * @param material 物料管理
     * @return 结果
     */
    public int updateMaterial(Material material);

    /**
     * 批量删除物料管理
     * 
     * @param ids 需要删除的物料管理主键集合
     * @return 结果
     */
    public int deleteMaterialByIds(Long[] ids);

    /**
     * 删除物料管理信息
     * 
     * @param id 物料管理主键
     * @return 结果
     */
    public int deleteMaterialById(Long id);
}
