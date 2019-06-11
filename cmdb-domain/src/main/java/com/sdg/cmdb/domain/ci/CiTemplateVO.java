package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdg.cmdb.domain.server.EnvType;
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
    private EnvType env;

    @Override
    public String toString(){
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }


}
