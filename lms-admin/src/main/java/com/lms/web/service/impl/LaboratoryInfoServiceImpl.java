package com.lms.web.service.impl;

import com.lms.common.core.domain.entity.LaboratoryInfo;
import com.lms.common.core.domain.entity.LaboratoryInfoVo;
import com.lms.web.mapper.LaboratoryInfoMapper;
import com.lms.web.service.ILaboratoryInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whn
 */
@Service
public class LaboratoryInfoServiceImpl implements ILaboratoryInfoService {

    @Resource
    private LaboratoryInfoMapper infoMapper;


    @Override
    public int updateInfo(LaboratoryInfoVo infoVo) {
        String name = infoVo.getName();
        String description = infoVo.getDescription();

        if (name != null && !name.isEmpty()) {
            infoMapper.updateLaboratoryName(name);
        }
        if (description != null && !description.isEmpty()) {
            infoMapper.updateLaboratoryDesc(description);
        }
        return 1;
    }

    @Override
    public int addImage(LaboratoryInfo info) {
        info.setType('3');
        infoMapper.insertInfo(info);
        return info.getId();
    }

    @Override
    public int deleteImageByIds(Integer[] ids) {
        return infoMapper.deleteInfoByIds(ids);
    }

    @Override
    public LaboratoryInfoVo getLaboratoryInfo() {
        String name = infoMapper.selectLaboratoryName();
        String desc = infoMapper.selectLaboratoryDesc();
        List<LaboratoryInfo> imageList = infoMapper.selectInfoList('3');
        LaboratoryInfoVo infoVo = new LaboratoryInfoVo();
        infoVo.setName(name);
        infoVo.setDescription(desc);
        infoVo.setImageList(imageList);
        return infoVo;
    }
}
