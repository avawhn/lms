package com.lms.hustoj.service;

import com.lms.common.core.domain.AjaxResult;
import com.lms.hustoj.domain.Contest;
import com.lms.hustoj.domain.ContestSubmit;

import java.util.List;

/**
 * @author whn
 */
public interface ContestService {
    Contest selectContestByContestId(Integer contestId);

    List<Contest> selectContestList(Contest contest);


    int insertContest(Contest contest);

    int deleteContestByContestIds(Long[] ids);

    AjaxResult computeScore(ContestSubmit submit);
}
