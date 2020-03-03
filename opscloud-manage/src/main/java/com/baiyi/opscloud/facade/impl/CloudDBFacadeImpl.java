package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.cloud.db.ICloudDB;
import com.baiyi.opscloud.cloud.db.factory.CloudDBFactory;
import com.baiyi.opscloud.common.base.CloudDBType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.CloudDBDatabaseDecorator;
import com.baiyi.opscloud.decorator.CloudDBDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcCloudDb;
import com.baiyi.opscloud.domain.generator.OcCloudDbAttribute;
import com.baiyi.opscloud.domain.generator.OcCloudDbDatabase;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.param.cloud.CloudDBParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBVO;
import com.baiyi.opscloud.facade.CloudDBFacade;
import com.baiyi.opscloud.service.cloud.OcCloudDBAttributeService;
import com.baiyi.opscloud.service.cloud.OcCloudDBDatabaseService;
import com.baiyi.opscloud.service.cloud.OcCloudDBService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/1 12:20 下午
 * @Version 1.0
 */
@Service
public class CloudDBFacadeImpl implements CloudDBFacade {

    @Resource
    private OcCloudDBService ocCloudDBService;

    @Resource
    private OcCloudDBDatabaseService ocCloudDBDatabaseService;

    @Resource
    private OcCloudDBAttributeService ocCloudDBAttributeService;

    @Resource
    private CloudDBDecorator cloudDBDecorator;

    @Resource
    private CloudDBDatabaseDecorator cloudDBDatabaseDecorator;

    @Override
    public DataTable<OcCloudDBVO.CloudDB> fuzzyQueryCloudDBPage(CloudDBParam.PageQuery pageQuery) {
        DataTable<OcCloudDb> table = ocCloudDBService.fuzzyQueryOcCloudDBByParam(pageQuery);
        // return toUserPage(table, pageQuery.getExtend());
        List<OcCloudDBVO.CloudDB> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudDBVO.CloudDB.class);
        //DataTable<OcCloudDBVO.CloudDB> dataTable = new DataTable<>(page, table.getTotalNum());
        // return dataTable;
        return new DataTable<>(page.stream().map(e -> cloudDBDecorator.decorator(e, 1)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Transactional
    public BusinessWrapper<Boolean> deleteCloudDBById(int id) {
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbById(id);
        if (ocCloudDb == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_DB_NOT_EXIST);
        ocCloudDBService.deleteOcCloudDbById(id);
        // 删除实例属性
        List<OcCloudDbAttribute> ocCloudDbAttributeList = ocCloudDBAttributeService.queryOcCloudDbAttributeByCloudDbId(id);
        for (OcCloudDbAttribute ocCloudDbAttribute : ocCloudDbAttributeList)
            ocCloudDBAttributeService.delOcCloudDbAttributeById(ocCloudDbAttribute.getId());

        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudDB() {
        Map<String, ICloudDB> context = CloudDBFactory.getCloudDBContainer();
        for (String key : context.keySet()) {
            try {
                context.get(key).syncDBInstance();
            } catch (Exception e) {
            }
        }
        return new BusinessWrapper<>(true);
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudDatabase(int id) {
        //OcCloudDbDatabase ocCloudDbDatabase = ocCloudDBDatabaseService.queryOcCloudDbDatabaseById(id);
        //if (ocCloudDbDatabase == null) return new BusinessWrapper(ErrorEnum.CLOUD_DB_DATABASE_NOT_EXIST);
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbById(id);
        if (ocCloudDb == null) return new BusinessWrapper(ErrorEnum.CLOUD_DB_NOT_EXIST);
        String key = CloudDBType.getName(ocCloudDb.getCloudDbType());
        CloudDBFactory.getCloudDBByKey(key).syncDatabase(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OcCloudDBDatabaseVO.CloudDBDatabase> fuzzyQueryCloudDBDatabasePage(CloudDBDatabaseParam.PageQuery pageQuery) {
        DataTable<OcCloudDbDatabase> table = ocCloudDBDatabaseService.fuzzyQueryOcCloudDBDatabaseByParam(pageQuery);
        List<OcCloudDBDatabaseVO.CloudDBDatabase> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudDBDatabaseVO.CloudDBDatabase.class);
        return new DataTable<>(page.stream().map(e -> cloudDBDatabaseDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> updateBaseCloudDBDatabase(OcCloudDBDatabaseVO.CloudDBDatabase cloudDBDatabase) {
        if (ocCloudDBDatabaseService.queryOcCloudDbDatabaseById(cloudDBDatabase.getId()) == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_DB_DATABASE_NOT_EXIST);
        OcCloudDbDatabase ocCloudDbDatabase = BeanCopierUtils.copyProperties(cloudDBDatabase, OcCloudDbDatabase.class);
        ocCloudDBDatabaseService.updateBaseOcCloudDbDatabase(ocCloudDbDatabase);
        return BusinessWrapper.SUCCESS;
    }
}
