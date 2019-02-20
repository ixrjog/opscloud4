package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CiTemplateVO extends CiTemplateDO implements Serializable {
    private static final long serialVersionUID = 7582941739817836712L;

    private List<CiJobVO> jobList;

    private List<CiJobVO> deployJobList;

//    public CiTemplateVO(CiTemplateDO ciTemplateDO){
//        setId(ciTemplateDO.getId());
//        setName(ciTemplateDO.getName());
//        setVersion(ciTemplateDO.getVersion());
//        setAppType(ciTemplateDO.getAppType());
//        setCiType(ciTemplateDO.getCiType());
//        setRollbackType(ciTemplateDO.getRollbackType());
//        setEnvType(ciTemplateDO.getEnvType());
//        setMd5(ciTemplateDO.getMd5());
//        setContent(ciTemplateDO.getContent());
//        setGmtCreate(ciTemplateDO.getGmtCreate());
//        setGmtModify(ciTemplateDO.getGmtModify());
//
//    }
//
//    public CiTemplateVO(){
//
//    }

    @Override
    public String toString(){
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }


}
