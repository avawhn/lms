package com.lms.hustoj.service;

import com.lms.hustoj.domain.ProblemAnswer;

import java.util.List;

/**
 * @author whn
 */
public interface ProblemAnswerService {
    List<ProblemAnswer> selectProblemAnswer(ProblemAnswer answer);
}
