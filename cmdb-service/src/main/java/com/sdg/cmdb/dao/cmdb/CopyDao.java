package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.copy.CopyDO;
import com.sdg.cmdb.domain.copy.CopyLogDO;
import com.sdg.cmdb.domain.copy.CopyServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CopyDao {

    /**
     * 分页条目数量
     *
     * @param businessKey
     * @return
     */
    long getCopySize(@Param("businessKey") String businessKey);

    /**
     * 分页详情
     *
     * @param businessKey
     * @param pageStart
     * @param length
     * @return
     */
    List<CopyDO> getCopyPage(
            @Param("businessKey") String businessKey,
            @Param("pageStart") long pageStart,
            @Param("length") int length);

    CopyDO getCopy(@Param("id") long id);

    List<CopyDO> queryCopyByBusinessKey(@Param("businessKey") String businessKey);

    CopyDO queryCopyByBusinessId(@Param("businessId") long businessId);

    int addCopy(CopyDO copyDO);

    int updateCopy(CopyDO copyDO);

    int delCopy(@Param("id") long id);

    List<CopyServerDO> queryCopyServerByCopyId(@Param("copyId") long copyId);

    int addCopyServer(CopyServerDO copyServerDO);

    int delCopyServer(@Param("id") long id);

    int addCopyLog(CopyLogDO copyLogDO);

    int delCopyLog(@Param("id") long id);

    int updateCopyLog(CopyLogDO copyLogDO);

    long getCopyLogSize(@Param("serverName") String serverName, @Param("envType") int envType);

    List<CopyLogDO> getCopyLogPage(
            @Param("serverName") String serverName,
            @Param("envType") int envType,
            @Param("pageStart") long pageStart,
            @Param("length") int length);

    List<CopyLogDO> queryCopyLogByCopyId(@Param("copyId") long copyId);

}
