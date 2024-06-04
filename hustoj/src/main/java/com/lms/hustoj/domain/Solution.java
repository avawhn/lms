package com.lms.hustoj.domain;

import lombok.Data;

import java.sql.Timestamp;


/**
 * @author whn
 */
@Data
public class Solution {
    private static final String[] MSG_CONST =
            {
                    "挂起中",
                    "挂起并重新判定",
                    "编译中",
                    "运行并判定",
                    "正确",
                    "格式错误",
                    "答案错误",
                    "超出时间限制",
                    "超出内存限制",
                    "输出超出限制",
                    "运行错误",
                    "编译错误",
                    "编译成功",
                    "测试运行",
                    "运行完成",
                    "提交中",
                    "MSG_REMOTE_PENDING",
                    "MSG_REMOTE_JUDGING"
            };
    private Integer solutionId;
    private Integer problemId;
    private String userId;
    private String nick;
    private Integer time;
    private Integer memory;
    private Timestamp inDate;
    private Integer result;
    private Integer language;
    private String ip;
    private Integer contestId;
    private Integer valid;
    private Integer num;
    private Integer codeLength;
    private Timestamp judgetime;
    private Double passRate;
    private Integer lintError;
    private String judger;
    private String remoteOj;
    private String remoteId;

    private String msg;


    private SourceCode sourceCode;

    public void setResult(Integer result) {
        this.setMsg(MSG_CONST[result]);
        this.result = result;
    }

    private Problem problem;
}
