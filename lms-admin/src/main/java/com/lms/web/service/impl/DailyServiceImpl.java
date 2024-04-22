package com.lms.web.service.impl;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Daily;
import com.lms.common.core.domain.entity.SysUser;
import com.lms.common.utils.DateUtils;
import com.lms.system.mapper.SysUserMapper;
import com.lms.web.mapper.DailyMapper;
import com.lms.web.service.IDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 日报管理Service业务层处理
 *
 * @author whn
 * @date 2024-03-25
 */
@Service
public class DailyServiceImpl implements IDailyService {
    @Autowired
    private DailyMapper dailyMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDailyList(List<Daily> dailyList) {
        dailyList.forEach(d -> {
            dailyMapper.updateDaily(d);
        });
        return dailyList.size();
    }

    /**
     * 查询日报管理
     *
     * @param id 日报管理主键
     * @return 日报管理
     */
    @Override
    public Daily selectDailyById(Long id) {
        return dailyMapper.selectDailyById(id);
    }

    /**
     * 查询日报管理列表
     *
     * @param daily 日报管理
     * @return 日报管理
     */
    @Override
    public List<Daily> selectDailyList(Daily daily) {
        return dailyMapper.selectDailyList(daily);
    }

    /**
     * 新增日报管理
     *
     * @param daily 日报管理
     * @return 结果
     */
    @Override
    public AjaxResult insertDaily(Daily daily) {
        Daily param = new Daily();
        param.setDate(daily.getDate());
        param.setUserId(daily.getUserId());
        List<Daily> dailies = dailyMapper.selectDailyList(param);
        if (!dailies.isEmpty()) {
            SysUser user = sysUserMapper.selectUserById(daily.getUserId());
            return AjaxResult.error(user.getNickName() + "已经填写" + SF.format(daily.getDate()) + "日报");
        }
        Date now = DateUtils.getNowDate();
        daily.setCreateTime(now);
        daily.setUpdateTime(now);
        dailyMapper.insertDaily(daily);
        return AjaxResult.success();
    }

    /**
     * 修改日报管理
     *
     * @param daily 日报管理
     * @return 结果
     */
    @Override
    public AjaxResult updateDaily(Daily daily) {
        Daily d = dailyMapper.selectDailyByUserIdAndDate(daily.getUserId(), daily.getDate());
        if (d != null && !d.getId().equals(daily.getId())) {
            String msg = d.getNickName() + SF.format(daily.getDate()) + "日报已存在";
            return AjaxResult.error(msg);
        }
        daily.setUpdateTime(DateUtils.getNowDate());
        int count = dailyMapper.updateDaily(daily);
        return count == 0 ? AjaxResult.error("更新失败") : AjaxResult.success();
    }

    /**
     * 批量删除日报管理
     *
     * @param ids 需要删除的日报管理主键
     * @return 结果
     */
    @Override
    public int deleteDailyByIds(Long[] ids) {
        return dailyMapper.deleteDailyByIds(ids);
    }

    /**
     * 删除日报管理信息
     *
     * @param id 日报管理主键
     * @return 结果
     */
    @Override
    public int deleteDailyById(Long id) {
        return dailyMapper.deleteDailyById(id);
    }
}
