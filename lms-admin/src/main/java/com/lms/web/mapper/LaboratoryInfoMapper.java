package com.lms.web.mapper;

import com.lms.common.core.domain.entity.LaboratoryInfo;

import java.util.List;

public interface LaboratoryInfoMapper {
    int insertInfo(LaboratoryInfo info);

    int updateLaboratoryName(String name);

    int updateLaboratoryDesc(String desc);

    LaboratoryInfo selectInfoById(Integer id);


    List<LaboratoryInfo> selectInfoList(char type);


    int deleteInfoByIds(Integer[] ids);

    String selectLaboratoryName();

    String selectLaboratoryDesc();
}
