package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.Award;
import com.lms.web.mapper.AwardMapper;
import com.lms.web.service.IAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 奖项Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-20
 */
@Service
public class AwardServiceImpl implements IAwardService {
    @Autowired
    private AwardMapper awardMapper;

    /**
     * 查询奖项
     *
     * @param id 奖项主键
     * @return 奖项
     */
    @Override
    public Award selectAwardById(Long id) {
        return awardMapper.selectAwardById(id);
    }

    /**
     * 查询奖项列表
     *
     * @param award 奖项
     * @return 奖项
     */
    @Override
    public List<Award> selectAwardList(Award award) {
        return awardMapper.selectAwardList(award);
    }

    /**
     * 新增奖项
     *
     * @param award 奖项
     * @return 结果
     */
    @Override
    public int insertAward(Award award) {
        return awardMapper.insertAward(award);
    }

    /**
     * 修改奖项
     *
     * @param award 奖项
     * @return 结果
     */
    @Override
    public int updateAward(Award award) {
        return awardMapper.updateAward(award);
    }

    /**
     * 批量删除奖项
     *
     * @param ids 需要删除的奖项主键
     * @return 结果
     */
    @Override
    public int deleteAwardByIds(Long[] ids) {
        return awardMapper.deleteAwardByIds(ids);
    }

    /**
     * 删除奖项信息
     *
     * @param id 奖项主键
     * @return 结果
     */
    @Override
    public int deleteAwardById(Long id) {
        return awardMapper.deleteAwardById(id);
    }

    @Override
    public Award selectLatestAward() {
        return awardMapper.selectLatestAward();
    }

    @Override
    public List<Award> selectAllGrade() {
        return awardMapper.selectAllGrade();
    }

    @Override
    public Award selectAwardDetailById(Integer id) {
        return awardMapper.selectAwardDetailById(id);
    }
}
