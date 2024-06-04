package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.Contest;

import java.util.List;

/**
 * @author whn
 */
public interface ContestMapper {
    Contest selectContestByContestId(Integer contestId);

    List<Contest> selectContestList(Contest contest);
    int insertContest(Contest contest);

    int deleteContestByContestIds(Long[] ids);
}
