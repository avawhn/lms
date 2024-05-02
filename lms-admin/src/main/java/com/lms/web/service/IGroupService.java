package com.lms.web.service;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Group;

import java.util.List;

/**
 * 组Service接口
 * 
 * @author whn
 * @date 2024-02-21
 */
public interface IGroupService 
{
    /**
     * 查询组
     * 
     * @param id 组主键
     * @return 组
     */
    public Group selectGroupById(Long id);

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
     * 批量删除组
     *
     * @param ids 需要删除的组主键集合
     * @return 结果
     */
    public AjaxResult deleteGroupByIds(Long[] ids);

    /**
     * 删除组信息
     * 
     * @param id 组主键
     * @return 结果
     */
    public int deleteGroupById(Long id);
}
