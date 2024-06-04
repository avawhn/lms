package com.lms.hustoj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.common.annotation.Excel;
import com.lms.common.annotation.Excels;
import com.lms.common.core.domain.BaseEntity;
import com.lms.common.core.domain.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author whn
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestUser extends BaseEntity {
    private Integer id;
    private Integer contestId;
    private Integer userId;
    private Character status;


    @Excels({
            @Excel(name = "姓名", targetAttr = "nickName", type = Excel.Type.EXPORT),
            @Excel(name = "分组", targetAttr = "group.groupName", type = Excel.Type.EXPORT)
    })
    private SysUser user;

    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @Excel(name = "状态")
    private String statusStr;

    @Excel(name = "分数")
    private Integer score;


    private Contest contest;


    public void setStatus(Character status) {
        String str = null;
        if (status == '0') {
            str = "未提交";
        } else if (status == '1') {
            str = "已提交";
        }
        this.setStatusStr(str);
        this.status = status;
    }
}
