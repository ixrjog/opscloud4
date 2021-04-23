package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;
import tk.mybatis.mapper.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OcSubmenuMapper extends Mapper<OcSubmenu> {

    List<OcSubmenu> queryOcSubmenuByIdList(@Param("idList") List<Integer> idList);
}