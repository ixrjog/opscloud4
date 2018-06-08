package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.SystemDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.systems.SystemDO;
import com.sdg.cmdb.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 16/10/8.
 */
@Service
public class SystemServiceImpl implements SystemService {

    private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);

    @Resource
    private SystemDao systemDao;

    @Override
    public BusinessWrapper<Boolean> saveSystem(SystemDO systemDO) {
        try {
            if (systemDO.getId() == 0) {
                systemDao.addSystem(systemDO);
            } else {
                systemDao.updateSystem(systemDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delSystem(long systemId) {
        try {
            systemDao.delSystemById(systemId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public TableVO<List<SystemDO>> querySystems(String systemName, int page, int length) {
        long size = systemDao.querySystemsByNameSize(systemName);
        List<SystemDO> list = systemDao.querySystemByNamePage(systemName, page * length, length);
        return new TableVO<>(size, list);
    }
}
