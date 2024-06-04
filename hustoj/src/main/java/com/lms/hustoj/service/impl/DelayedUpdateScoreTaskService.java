package com.lms.hustoj.service.impl;

import com.lms.hustoj.domain.Solution;
import com.lms.hustoj.domain.SubmitInfo;
import com.lms.hustoj.mapper.ContestUserMapper;
import com.lms.hustoj.mapper.SolutionMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author whn
 */
@Service
public class DelayedUpdateScoreTaskService {

    @Resource
    private SolutionMapper solutionMapper;

    @Resource
    private ContestUserMapper contestUserMapper;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    @Getter
    private final ConcurrentLinkedQueue<SubmitInfo> queue;

    private static final Logger log = LoggerFactory.getLogger(DelayedUpdateScoreTaskService.class);

    public DelayedUpdateScoreTaskService() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void callDelayedUpdateScore() {
        // 延时5秒后执行任务
        scheduler.schedule(() -> {
            SubmitInfo info = queue.poll();
            if (info == null) {
                return;
            }
            Solution solution = solutionMapper.selectSolutionBySolutionId(info.getSolutionId());
            // result < 4 表示没有完成判题，需要等待
            if (solution.getResult() < 4) {
                this.queue.offer(info);
                this.callDelayedUpdateScore();
                return;
            }
            int score = (int) (solution.getPassRate() * info.getScore());
            log.info("计算算法题分数：{}", score);
            contestUserMapper.addScore(info.getContestId(), info.getUserId(), score);
        }, 3, TimeUnit.SECONDS);
    }
}
