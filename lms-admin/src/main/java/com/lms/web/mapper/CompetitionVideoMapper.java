package com.lms.web.mapper;

import com.lms.common.core.domain.entity.CompetitionVideo;

import java.util.List;

/**
 * 比赛视频Mapper接口
 *
 * @author whn
 * @date 2024-05-20
 */
public interface CompetitionVideoMapper {
    /**
     * 查询比赛视频
     *
     * @param id 比赛视频主键
     * @return 比赛视频
     */
    public CompetitionVideo selectCompetitionVideoById(Integer id);

    /**
     * 查询比赛视频列表
     *
     * @param competitionVideo 比赛视频
     * @return 比赛视频集合
     */
    public List<CompetitionVideo> selectCompetitionVideoList(CompetitionVideo competitionVideo);

    /**
     * 新增比赛视频
     *
     * @param competitionVideo 比赛视频
     * @return 结果
     */
    public int insertCompetitionVideo(CompetitionVideo competitionVideo);

    /**
     * 修改比赛视频
     *
     * @param competitionVideo 比赛视频
     * @return 结果
     */
    public int updateCompetitionVideo(CompetitionVideo competitionVideo);

    /**
     * 删除比赛视频
     *
     * @param id 比赛视频主键
     * @return 结果
     */
    public int deleteCompetitionVideoById(Integer id);

    /**
     * 批量删除比赛视频
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCompetitionVideoByIds(Integer[] ids);
}
