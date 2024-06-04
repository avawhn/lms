package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.Robot;
import com.lms.web.mapper.RobotMapper;
import com.lms.web.service.IRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 机器人Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-20
 */
@Service
public class RobotServiceImpl implements IRobotService {
    @Autowired
    private RobotMapper robotMapper;

    /**
     * 查询机器人
     *
     * @param id 机器人主键
     * @return 机器人
     */
    @Override
    public Robot selectRobotById(Integer id) {
        return robotMapper.selectRobotById(id);
    }

    /**
     * 查询机器人列表
     *
     * @param robot 机器人
     * @return 机器人
     */
    @Override
    public List<Robot> selectRobotList(Robot robot) {
        return robotMapper.selectRobotList(robot);
    }

    /**
     * 新增机器人
     *
     * @param robot 机器人
     * @return 结果
     */
    @Override
    public int insertRobot(Robot robot) {
        return robotMapper.insertRobot(robot);
    }

    /**
     * 修改机器人
     *
     * @param robot 机器人
     * @return 结果
     */
    @Override
    public int updateRobot(Robot robot) {
        return robotMapper.updateRobot(robot);
    }

    /**
     * 批量删除机器人
     *
     * @param ids 需要删除的机器人主键
     * @return 结果
     */
    @Override
    public int deleteRobotByIds(Integer[] ids) {
        return robotMapper.deleteRobotByIds(ids);
    }

    /**
     * 删除机器人信息
     *
     * @param id 机器人主键
     * @return 结果
     */
    @Override
    public int deleteRobotById(Integer id) {
        return robotMapper.deleteRobotById(id);
    }
}
