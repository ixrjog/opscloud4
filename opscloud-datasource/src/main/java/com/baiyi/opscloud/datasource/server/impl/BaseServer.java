package com.baiyi.opscloud.datasource.server.impl;

import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.provider.base.common.IInstanceType;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.facade.business.BusinessFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.BaseTagService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 1:47 下午
 * @Since 1.0
 */
public abstract class BaseServer extends SimpleDsInstanceProvider implements InitializingBean, IInstanceType {

    private List<SimpleAssetProvider> simpleAssetProviderList;

    @Resource
    private BusinessFacade businessFacade;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private BaseTagService baseTagService;

//    @Resource
//    protected DsConfigFactory dsFactory;
//
//    @Resource
//    private DsInstanceAssetService dsInstanceAssetService;
//
//    @Resource
//    private BaseDsAssetFacade baseDsAssetFacade;


    public void create(Server server) {
        Map<String, String> serverProperties = getServerProperties(server);
        create(server, serverProperties);
    }

    public void create(Server server, Map<String, String> serverProperties) {
        List<DatasourceInstance> instanceList = filterDsInstanceByBusinessTag();
        instanceList.forEach(instance -> {
            DatasourceInstanceAsset asset = create(buildDsInstanceContext(instance.getId()), server, serverProperties);
            buildBusinessRelation(server, asset);
        });
    }

    /**
     * 创建资产
     *
     * @param dsInstanceContext
     * @param server
     * @param serverProperties
     * @return
     */
    protected abstract DatasourceInstanceAsset create(DsInstanceContext dsInstanceContext, Server server, Map<String, String> serverProperties);


    public void update(Server server) {
        Map<String, String> serverProperties = getServerProperties(server);
        update(server, serverProperties);
    }

    public void update(Server server, Map<String, String> serverProperties) {
        List<DatasourceInstance> instanceList = filterDsInstanceByBusinessTag();
        instanceList.forEach(instance -> {
            DatasourceInstanceAsset asset = update(buildDsInstanceContext(instance.getId()), server, serverProperties);
            buildBusinessRelation(server, asset);
        });
    }

    /**
     * 更新资产
     *
     * @param dsInstanceContext
     * @param server
     * @param serverProperties
     * @return
     */
    protected abstract DatasourceInstanceAsset update(DsInstanceContext dsInstanceContext, Server server, Map<String, String> serverProperties);

//    @Override
//    public void destroy(Integer id) {
//        List<DatasourceInstance> instanceList = filterDsInstanceByBusinessTag();
//        instanceList.forEach(instance -> destroy(buildDsInstanceContext(instance.getId()), id));
//    }

//    private void destroy(DsInstanceContext dsInstanceContext, Integer id) {
//        DatasourceInstanceAsset asset = getBindAsset(id);
//        if (asset != null) {
//            destroy(dsInstanceContext, asset);
//            baseDsAssetFacade.deleteAssetById(asset.getId());
//        }
//    }

    /**
     * 销毁资产
     *
     * @param dsInstanceContext
     * @param asset
     */
    protected abstract void destroy(DsInstanceContext dsInstanceContext, DatasourceInstanceAsset asset);

    protected void buildBusinessRelation(Server server, DatasourceInstanceAsset asset) {
        BusinessRelation businessRelation = BusinessRelation.builder()
                .sourceBusinessType(BusinessTypeEnum.SERVER.getType())
                .sourceBusinessId(server.getId())
                .targetBusinessType(BusinessTypeEnum.ASSET.getType())
                .targetBusinessId(asset.getId())
                .relationType(asset.getAssetType())
                .build();
     //   businessFacade.saveBusinessRelation(businessRelation);
    }

//    private void setSimpleAssetProviderList() {
//        this.simpleAssetProviderList = AssetProviderFactory.getProviders(getInstanceType(), getAssetType());
//    }

//    public List<SimpleAssetProvider> getSimpleAssetProviderList() {
//        if (CollectionUtils.isEmpty(simpleAssetProviderList)) {
//            setSimpleAssetProviderList();
//        }
//        return simpleAssetProviderList;
//    }

    protected String getFilterDsInstanceTagKey() {
        return Strings.EMPTY;
    }

    private List<DatasourceInstance> filterDsInstanceByBusinessTag() {
        List<DatasourceInstance> instanceList = dsInstanceService.listByInstanceType(getInstanceType());
        if (StringUtils.isNotBlank(getFilterDsInstanceTagKey())) {
            return instanceList.stream()
                    .filter(instance ->
                            baseTagService.hasBusinessTag(getFilterDsInstanceTagKey(), BusinessTypeEnum.DATASOURCE_INSTANCE.getType(), instance.getId(), true)
                    ).collect(Collectors.toList());
        }
        return instanceList;
    }

    protected Map<String, String> getServerGroupProperties(Server server) {
        Map<String, String> map = businessFacade.getDefaultServerGroupProperty();
        Map<String, String> serverGroupProperties = businessFacade.getBusinessProperty(BusinessTypeEnum.SERVERGROUP.getType(), server.getServerGroupId());
        map.putAll(serverGroupProperties);
        return map;
    }

    protected Map<String, String> getServerProperties(Server server) {
        Map<String, String> map = getServerGroupProperties(server);
        Map<String, String> serverProperties = businessFacade.getBusinessProperty(BusinessTypeEnum.SERVER.getType(), server.getServerGroupId());
        map.putAll(serverProperties);
        return map;
    }



}
