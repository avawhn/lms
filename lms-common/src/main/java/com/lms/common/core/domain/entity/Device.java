package com.lms.common.core.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lms.common.annotation.Excel;
import com.lms.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @Excel(name = "所属组id")
    private Long groupId;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long deviceNumber;

    /**
     * 图片url
     */
    @Excel(name = "图片url")
    private String imageUrl;

}
