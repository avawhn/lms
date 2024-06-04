package com.lms.hustoj.service;

import com.lms.hustoj.domain.ContestUser;

import java.util.List;

/**
 * @author whn
 */
public interface ContestUserService {
    List<ContestUser> selectContestUserList(ContestUser contestUser);
    int insertContestUserList(List<ContestUser> contestUserList);

    List<ContestUser> selectUserContest(int userId);

}
