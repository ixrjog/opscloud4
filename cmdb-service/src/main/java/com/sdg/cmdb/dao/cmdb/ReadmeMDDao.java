package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.readmeMD.ReadmeMDDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ReadmeMDDao {

    ReadmeMDDO getReadmeMD(@Param("id") long id);

    ReadmeMDDO getReadmeMDByKey(@Param("mdKey") String mdKey);

    int addReadmeMD(ReadmeMDDO readmeMDDO);

    int updateReadmeMD(ReadmeMDDO readmeMDDO);

    int delReadmeMD(@Param("id") long id);

}
