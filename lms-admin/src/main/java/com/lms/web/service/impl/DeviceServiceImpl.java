package com.lms.web.service.impl;

import com.lms.common.utils.DateUtils;
import com.lms.common.core.domain.entity.Device;
import com.lms.web.mapper.DeviceMapper;
import com.lms.web.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备Service业务层处理
 * 
 * @author whn
 * @date 2024-02-21
 */
@Service
public class DeviceServiceImpl implements IDeviceService 
{
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    @Override
    public Device selectDeviceById(Long id)
    {
        return deviceMapper.selectDeviceByDeviceId(id);
    }

    /**
     * 查询设备列表
     * 
     * @param device 设备
     * @return 设备
     */
    @Override
    public List<Device> selectDeviceList(Device device)
    {
        return deviceMapper.selectDeviceList(device);
    }

    /**
     * 新增设备
     * 
     * @param device 设备
     * @return 结果
     */
    @Override
    public int insertDevice(Device device)
    {
        device.setCreateTime(DateUtils.getNowDate());
        return deviceMapper.insertDevice(device);
    }

    /**
     * 修改设备
     * 
     * @param device 设备
     * @return 结果
     */
    @Override
    public int updateDevice(Device device)
    {
        device.setUpdateTime(DateUtils.getNowDate());
        return deviceMapper.updateDevice(device);
    }

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceByIds(Long[] ids)
    {
        return deviceMapper.deleteDeviceByDeviceIds(ids);
    }

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    @Override
    public int deleteDeviceById(Long id)
    {
        return deviceMapper.deleteDeviceByDeviceId(id);
    }
}
