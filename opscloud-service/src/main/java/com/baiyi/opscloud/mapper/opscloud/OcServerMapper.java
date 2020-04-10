package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerMapper extends Mapper<OcServer> {

    List<OcServer> queryOcServerByParam(ServerParam.PageQuery pageQuery);

    List<OcServer> fuzzyQueryOcServerByParam(ServerParam.PageQuery pageQuery);

    OcServer queryOcServerMaxSerialNumber(@Param("serverGroupId") int serverGroupId);

    OcServer queryOcServerMaxSerialNumberByEnvType(@Param("serverGroupId") int serverGroupId,@Param("envType") int envType);


}