package com.lms.hustoj.service.impl;

import com.lms.common.core.domain.entity.SysUser;
import com.lms.common.utils.SecurityUtils;
import com.lms.hustoj.domain.PassRateCount;
import com.lms.hustoj.domain.Solution;
import com.lms.hustoj.domain.SourceCode;
import com.lms.hustoj.mapper.SolutionMapper;
import com.lms.hustoj.mapper.SourceCodeMapper;
import com.lms.hustoj.service.SolutionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whn
 */
@Service
public class SolutionServiceImpl implements SolutionService {
    @Resource
    private SolutionMapper solutionMapper;

    @Resource
    private SourceCodeMapper sourceCodeMapper;

    @Override
    public Solution selectSolutionBySolutionId(int solutionId) {
        return solutionMapper.selectSolutionBySolutionId(solutionId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertSolution(Solution solution) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        solution.setUserId(user.getUserName());
        solution.setNick(user.getNickName());
        solution.setJudger("admin");

        SourceCode sourceCode = solution.getSourceCode();
        solution.setCodeLength(sourceCode.getSource().length());
        solution.setIp("192.168.56.1");

        // Web插入Solution表获得solution_id，result设为14
        solution.setResult(14);
        solutionMapper.insertSolution(solution);


        // 插入source_code表准备需要评测的源码。
        sourceCode.setSolutionId(solution.getSolutionId());
        sourceCodeMapper.insertSourceCode(sourceCode);


        // 更新solution的result为0，标志准备好进入评测阶段。
        Solution newSolution = new Solution();
        newSolution.setSolutionId(solution.getSolutionId());
        newSolution.setResult(0);
        solutionMapper.updateSolution(newSolution);

        return solution.getSolutionId();
    }

    @Override
    public List<Solution> selectSolutionList(Solution solution) {
        return solutionMapper.selectSolutionList(solution);
    }

    @Override
    public List<Solution> selectSolutionRecordList(Solution solution) {
        return solutionMapper.selectSolutionRecordList(solution);
    }

    @Override
    public List<PassRateCount> selectPassRateCount(String userId) {
        return solutionMapper.selectPassRateCount(userId);
    }
}
