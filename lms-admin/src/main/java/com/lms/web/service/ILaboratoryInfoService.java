package com.lms.web.service;

import com.lms.common.core.domain.entity.LaboratoryInfo;
import com.lms.common.core.domain.entity.LaboratoryInfoVo;

/**
 * @author whn
 */
public interface ILaboratoryInfoService {

    int updateInfo(LaboratoryInfoVo infoVo);

    int addImage(LaboratoryInfo info);
    int deleteImageByIds(Integer[] ids);

    LaboratoryInfoVo getLaboratoryInfo();
}
