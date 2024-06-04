package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lms.common.annotation.Excel;
import com.lms.common.annotation.Excels;
import com.lms.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备对象 t_device
 *
 * @author whn
 * @date 2024-02-21
 */
@Setter
@Getter
@ToString
public class Device extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     * Long 64 位，JavaScript 最大 53 位，直接返回会导致溢出，序列化时转为字符串
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 所属组id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    private String userIds;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long deviceNumber;

    /**
     * 图片url
     */
    private String imageUrl;

    private Integer deviceTypeId;

    private String deviceTypeName;

    private String brand;


    @Excel(name = "出厂日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;

    @Excel(name = "报废日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date retirementDate;

    private String status;


    @Excels({
            @Excel(name = "所属分组", targetAttr = "groupName", type = Excel.Type.EXPORT),
    })
    private Group group;

    // @Excels({
    //         @Excel(name = "负责人", targetAttr = "nickName", type = Excel.Type.EXPORT),
    // })
    private List<SysUser> userList;


    @Excel(name = "负责人")
    private String userNames;


    public void setUserList(List<SysUser> userList) {
        this.userList = userList;
        this.userNames = userList.stream().map(SysUser::getNickName).collect(Collectors.joining(","));
    }
}
