package com.lms.web.service;

import com.lms.common.core.domain.entity.Award;

import java.util.List;

/**
 * 奖项Service接口
 *
 * @author ruoyi
 * @date 2024-05-20
 */
public interface IAwardService {
    /**
     * 查询奖项
     *
     * @param id 奖项主键
     * @return 奖项
     */
    public Award selectAwardById(Long id);

    /**
     * 查询奖项列表
     *
     * @param award 奖项
     * @return 奖项集合
     */
    public List<Award> selectAwardList(Award award);

    /**
     * 新增奖项
     *
     * @param award 奖项
     * @return 结果
     */
    public int insertAward(Award award);

    /**
     * 修改奖项
     *
     * @param award 奖项
     * @return 结果
     */
    public int updateAward(Award award);

    /**
     * 批量删除奖项
     *
     * @param ids 需要删除的奖项主键集合
     * @return 结果
     */
    public int deleteAwardByIds(Long[] ids);

    /**
     * 删除奖项信息
     *
     * @param id 奖项主键
     * @return 结果
     */
    public int deleteAwardById(Long id);

    Award selectLatestAward();

    List<Award> selectAllGrade();

    Award selectAwardDetailById(Integer id);
}
