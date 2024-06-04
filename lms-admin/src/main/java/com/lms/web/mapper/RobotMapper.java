package com.lms.web.mapper;

import com.lms.common.core.domain.entity.Robot;

import java.util.List;

/**
 * 机器人Mapper接口
 *
 * @author ruoyi
 * @date 2024-05-20
 */
public interface RobotMapper {
    /**
     * 查询机器人
     *
     * @param id 机器人主键
     * @return 机器人
     */
    public Robot selectRobotById(Integer id);

    /**
     * 查询机器人列表
     *
     * @param robot 机器人
     * @return 机器人集合
     */
    public List<Robot> selectRobotList(Robot robot);

    /**
     * 新增机器人
     *
     * @param robot 机器人
     * @return 结果
     */
    public int insertRobot(Robot robot);

    /**
     * 修改机器人
     *
     * @param robot 机器人
     * @return 结果
     */
    public int updateRobot(Robot robot);

    /**
     * 删除机器人
     *
     * @param id 机器人主键
     * @return 结果
     */
    public int deleteRobotById(Integer id);

    /**
     * 批量删除机器人
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotByIds(Integer[] ids);
}
