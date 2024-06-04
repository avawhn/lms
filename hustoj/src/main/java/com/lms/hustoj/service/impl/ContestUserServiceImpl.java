package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.ContestUser;
import com.lms.hustoj.mapper.ContestUserMapper;
import com.lms.hustoj.service.ContestUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whn
 */
@Service
public class ContestUserServiceImpl implements ContestUserService {
    @Resource
    private ContestUserMapper contestUserMapper;


    @Override
    public List<ContestUser> selectContestUserList(ContestUser contestUser) {
        return contestUserMapper.selectContestUserList(contestUser);
    }

    @Override
    public int insertContestUserList(List<ContestUser> contestUserList) {
        return contestUserMapper.insertContestUserList(contestUserList);
    }

    @Override
    public List<ContestUser> selectUserContest(int userId) {
        return contestUserMapper.selectUserContest(userId);
    }

}
