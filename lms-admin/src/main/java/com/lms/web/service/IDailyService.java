package com.lms.web.service;

import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Daily;

import java.util.List;

/**
 * 日报管理Service接口
 *
 * @author whn
 * @date 2024-03-25
 */
public interface IDailyService {
    int updateDailyList(List<Daily> dailyList);


    /**
     * 查询日报管理
     *
     * @param id 日报管理主键
     * @return 日报管理
     */
    public Daily selectDailyById(Long id);

    /**
     * 查询日报管理列表
     *
     * @param daily 日报管理
     * @return 日报管理集合
     */
    public List<Daily> selectDailyList(Daily daily);

    /**
     * 新增日报管理
     *
     * @param daily 日报管理
     * @return 结果
     */
    public AjaxResult insertDaily(Daily daily);

    /**
     * 修改日报管理
     *
     * @param daily 日报管理
     * @return 结果
     */
    public AjaxResult updateDaily(Daily daily);

    /**
     * 批量删除日报管理
     *
     * @param ids 需要删除的日报管理主键集合
     * @return 结果
     */
    public int deleteDailyByIds(Long[] ids);

    /**
     * 删除日报管理信息
     *
     * @param id 日报管理主键
     * @return 结果
     */
    public int deleteDailyById(Long id);

}
