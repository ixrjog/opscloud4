package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcKeybox;
import com.baiyi.opscloud.domain.param.keybox.KeyboxParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcKeyboxMapper extends Mapper<OcKeybox> {

   List<OcKeybox> queryOcKeyboxByParam(KeyboxParam.PageQuery pageQuery);
}