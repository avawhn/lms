package com.lms.web.mapper;

import com.lms.common.core.domain.entity.Daily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日报管理Mapper接口
 *
 * @author whn
 * @date 2024-03-25
 */
public interface DailyMapper {

    Daily selectDailyByUserIdAndDate(@Param("userId") Long userId, @Param("date") Date date);


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
    public int insertDaily(Daily daily);

    /**
     * 修改日报管理
     *
     * @param daily 日报管理
     * @return 结果
     */
    public int updateDaily(Daily daily);

    /**
     * 删除日报管理
     *
     * @param id 日报管理主键
     * @return 结果
     */
    public int deleteDailyById(Long id);

    /**
     * 批量删除日报管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDailyByIds(Long[] ids);

}
