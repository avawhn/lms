package com.lms.web.service;

import com.lms.common.core.domain.entity.CompetitionVideo;

import java.util.List;

/**
 * 比赛视频Service接口
 *
 * @author ruoyi
 * @date 2024-05-20
 */
public interface ICompetitionVideoService {
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
     * 批量删除比赛视频
     *
     * @param ids 需要删除的比赛视频主键集合
     * @return 结果
     */
    public int deleteCompetitionVideoByIds(Integer[] ids);

    /**
     * 删除比赛视频信息
     *
     * @param id 比赛视频主键
     * @return 结果
     */
    public int deleteCompetitionVideoById(Integer id);
}
