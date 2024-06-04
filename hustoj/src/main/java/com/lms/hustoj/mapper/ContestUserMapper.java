package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.ContestUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whn
 */
public interface ContestUserMapper {
    List<ContestUser> selectContestUserList(ContestUser contestUser);

    List<ContestUser> selectUserContest(int userId);

    int insertContestUserList(List<ContestUser> contestUserList);

    ContestUser selectContestUserByContestIdAndUserId(@Param("contestId") Integer contestId,
                                                      @Param("userId") Integer userId);

    int addScore(@Param("contestId") int contestId,
                 @Param("userId") int userId,
                 @Param("score") int score);


    int updateContestUser(ContestUser contestUser);

}
