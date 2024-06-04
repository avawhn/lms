package com.lms.web.mapper;

import com.lms.common.core.domain.entity.Material;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料管理Mapper接口
 *
 * @author whn
 * @date 2024-02-21
 */
public interface MaterialMapper {
    /**
     * 查询物料管理
     *
     * @param id 物料管理主键
     * @return 物料管理
     */
    public Material selectMaterialByMaterialId(Long id);

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
     * 删除物料管理
     *
     * @param id 物料管理主键
     * @return 结果
     */
    public int deleteMaterialByMaterialId(Long id);

    /**
     * 批量删除物料管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMaterialByMaterialIds(Long[] ids);

    int updateMaterialCount(@Param("materialId") Long materialId,
                            @Param("number") int number);
}
