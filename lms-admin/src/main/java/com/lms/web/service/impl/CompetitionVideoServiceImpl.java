package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.CompetitionVideo;
import com.lms.web.mapper.CompetitionVideoMapper;
import com.lms.web.service.ICompetitionVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 比赛视频Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-20
 */
@Service
public class CompetitionVideoServiceImpl implements ICompetitionVideoService 
{
    @Autowired
    private CompetitionVideoMapper competitionVideoMapper;

    /**
     * 查询比赛视频
     * 
     * @param id 比赛视频主键
     * @return 比赛视频
     */
    @Override
    public CompetitionVideo selectCompetitionVideoById(Integer id)
    {
        return competitionVideoMapper.selectCompetitionVideoById(id);
    }

    /**
     * 查询比赛视频列表
     * 
     * @param competitionVideo 比赛视频
     * @return 比赛视频
     */
    @Override
    public List<CompetitionVideo> selectCompetitionVideoList(CompetitionVideo competitionVideo)
    {
        return competitionVideoMapper.selectCompetitionVideoList(competitionVideo);
    }

    /**
     * 新增比赛视频
     * 
     * @param competitionVideo 比赛视频
     * @return 结果
     */
    @Override
    public int insertCompetitionVideo(CompetitionVideo competitionVideo)
    {
        return competitionVideoMapper.insertCompetitionVideo(competitionVideo);
    }

    /**
     * 修改比赛视频
     * 
     * @param competitionVideo 比赛视频
     * @return 结果
     */
    @Override
    public int updateCompetitionVideo(CompetitionVideo competitionVideo)
    {
        return competitionVideoMapper.updateCompetitionVideo(competitionVideo);
    }

    /**
     * 批量删除比赛视频
     * 
     * @param ids 需要删除的比赛视频主键
     * @return 结果
     */
    @Override
    public int deleteCompetitionVideoByIds(Integer[] ids)
    {
        return competitionVideoMapper.deleteCompetitionVideoByIds(ids);
    }

    /**
     * 删除比赛视频信息
     * 
     * @param id 比赛视频主键
     * @return 结果
     */
    @Override
    public int deleteCompetitionVideoById(Integer id)
    {
        return competitionVideoMapper.deleteCompetitionVideoById(id);
    }
}
