package com.lms.web.controller.hustoj;

import com.lms.hustoj.domain.Solution;
import com.lms.hustoj.service.SolutionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {

    @Resource
    private SolutionService solutionService;

    @GetMapping("/hello")
    public Solution hello() {
        Solution solution = solutionService.selectSolutionBySolutionId(1001);
        System.out.println(solution);
        return solution;
    }
}
