package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.copy.CopyDO;
import com.sdg.cmdb.domain.copy.CopyLogVO;
import com.sdg.cmdb.domain.copy.CopyVO;

import java.util.List;

public interface CopyService {

    TableVO<List<CopyVO>> getCopyPage(String businessKey, int page, int length);

    TableVO<List<CopyLogVO>> getCopyLogPage(String serverName, int envType, int page, int length);

    CopyVO getCopy(long id);

    BusinessWrapper<Boolean> delCopy(long id);

    BusinessWrapper<Boolean> saveCopy(CopyDO copyDO);

    BusinessWrapper<Boolean> addCopyServer(long copyId,long serverId);

    BusinessWrapper<Boolean> delCopyServer(long id);

    /**
     * 执行远程同步
     * @param copyId
     * @return
     */
    BusinessWrapper<Boolean> doCopy(long copyId);

    BusinessWrapper<Boolean> delCopyLog(long id);

}
