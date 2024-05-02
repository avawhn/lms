package com.lms.web.service.impl;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.utils.DateUtils;
import com.lms.common.utils.IdGenerator;
import com.lms.common.core.domain.entity.Group;
import com.lms.web.mapper.GroupMapper;
import com.lms.web.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组Service业务层处理
 *
 * @author whn
 * @date 2024-02-21
 */
@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private GroupMapper groupMapper;

    @Resource
    private IdGenerator idGenerator;

    /**
     * 查询组
     *
     * @param id 组主键
     * @return 组
     */
    @Override
    public Group selectGroupById(Long id) {
        return groupMapper.selectGroupByGroupId(id);
    }

    /**
     * 查询组列表
     *
     * @param group 组
     * @return 组
     */
    @Override
    public List<Group> selectGroupList(Group group) {
        return groupMapper.selectGroupList(group);
    }

    /**
     * 新增组
     *
     * @param group 组
     * @return 结果
     */
    @Override
    public int insertGroup(Group group) {
        group.setGroupId(idGenerator.nextId());
        group.setCreateTime(DateUtils.getNowDate());

        // 全路径
        Long parentId = group.getParentId();
        if (parentId != null) {
            Group parent = groupMapper.selectGroupByGroupId(parentId);
            group.setFullPath(parent.getFullPath() + "/" + group.getGroupId());
        } else {
            group.setFullPath("/" + group.getGroupId());
        }

        return groupMapper.insertGroup(group);
    }

    /**
     * 修改组
     *
     * @param group 组
     * @return 结果
     */
    @Override
    public int updateGroup(Group group) {
        Group currentGroup = groupMapper.selectGroupByGroupId(group.getGroupId());
        // 父级id修改，需要修改全路径
        Long parentId = group.getParentId();
        if (parentId != null && !currentGroup.getParentId().equals(parentId)) {
            Group parent = groupMapper.selectGroupByGroupId(parentId);
            group.setFullPath(parent.getFullPath() + "/" + group.getGroupId());
        }
        group.setUpdateTime(DateUtils.getNowDate());
        return groupMapper.updateGroup(group);
    }

    /**
     * 批量删除组
     *
     * @param ids 需要删除的组主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteGroupByIds(Long[] ids) {
        for (Long id : ids) {
            List<Group> groups = groupMapper.selectGroupByParentId(id);
            if (!groups.isEmpty()) {
                return AjaxResult.error("含有子项, 删除失败");
            }
        }
        int res = groupMapper.deleteGroupByGroupIds(ids);
        return res > 0 ? AjaxResult.success() : AjaxResult.error("删除失败");
    }

    /**
     * 删除组信息
     *
     * @param id 组主键
     * @return 结果
     */
    @Override
    public int deleteGroupById(Long id) {
        return groupMapper.deleteGroupByGroupId(id);
    }
}
