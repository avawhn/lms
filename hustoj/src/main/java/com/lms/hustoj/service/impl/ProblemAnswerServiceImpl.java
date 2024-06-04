package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.ProblemAnswer;
import com.lms.hustoj.mapper.ProblemAnswerMapper;
import com.lms.hustoj.service.ProblemAnswerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whn
 */
@Service
public class ProblemAnswerServiceImpl implements ProblemAnswerService {
    @Resource
    private ProblemAnswerMapper answerMapper;

    @Override
    public List<ProblemAnswer> selectProblemAnswer(ProblemAnswer answer) {
        return answerMapper.selectProblemAnswerList(answer);
    }
}
