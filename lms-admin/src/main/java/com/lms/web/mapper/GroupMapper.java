package com.lms.web.mapper;

import com.lms.common.core.domain.entity.Group;

import java.util.List;

/**
 * 组Mapper接口
 * 
 * @author whn
 * @date 2024-02-21
 */
public interface GroupMapper
{
    /**
     * 查询组
     * 
     * @param id 组主键
     * @return 组
     */
    public Group selectGroupByGroupId(Long id);

    /**
     * 查询组列表
     * 
     * @param group 组
     * @return 组集合
     */
    public List<Group> selectGroupList(Group group);

    /**
     * 新增组
     * 
     * @param group 组
     * @return 结果
     */
    public int insertGroup(Group group);

    /**
     * 修改组
     * 
     * @param group 组
     * @return 结果
     */
    public int updateGroup(Group group);

    /**
     * 删除组
     * 
     * @param id 组主键
     * @return 结果
     */
    public int deleteGroupByGroupId(Long id);

    /**
     * 批量删除组
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGroupByGroupIds(Long[] ids);
}
