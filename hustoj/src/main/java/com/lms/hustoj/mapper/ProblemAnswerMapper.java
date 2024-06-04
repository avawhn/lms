package com.lms.hustoj.mapper;

import com.lms.hustoj.domain.ProblemAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author whn
 */
public interface ProblemAnswerMapper {
    int insertProblemAnswerList(@Param("answerList") List<ProblemAnswer> answerList);

    List<ProblemAnswer> selectProblemAnswerList(ProblemAnswer answer);
}
