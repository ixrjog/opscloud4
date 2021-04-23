package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAliyunOnsInstanceMapper extends Mapper<OcAliyunOnsInstance> {

    List<OcAliyunOnsInstance> queryOcAliyunOnsInstanceByInstanceIdList(@Param("instanceIdList") List<String> instanceIdList);
}