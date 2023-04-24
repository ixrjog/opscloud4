package com.baiyi.opscloud.datasource.nexus;

import com.baiyi.opscloud.datasource.nexus.base.BaseNexusTest;
import com.baiyi.opscloud.datasource.nexus.driver.NexusAssetDriver;
import com.baiyi.opscloud.datasource.nexus.driver.NexusRepositoryDriver;
import com.baiyi.opscloud.datasource.nexus.driver.NexusSearchDriver;
import com.baiyi.opscloud.datasource.nexus.entity.NexusAsset;
import com.baiyi.opscloud.datasource.nexus.entity.NexusComponent;
import com.baiyi.opscloud.datasource.nexus.entity.NexusRepository;
import com.baiyi.opscloud.datasource.nexus.param.SearchParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/5 6:12 下午
 * @Version 1.0
 */
public class NexusTest extends BaseNexusTest {

    @Resource
    private NexusRepositoryDriver nexusRepositoryHandler;

    @Resource
    private NexusAssetDriver nexusAssetDriver;

    @Resource
    private NexusSearchDriver nexusSearchDriver;

    @Test
    void listRepositoriesTest() {
        List<NexusRepository.Repository> repositories = nexusRepositoryHandler.list(getConfig().getNexus());
        repositories.forEach(this::print);
    }

    @Test
    void listAssetsTest() {
        NexusAsset.Assets assets = nexusAssetDriver.list(getConfig().getNexus(), "maven-snapshots", "");
        assets.getItems().forEach(this::print);
    }

    @Test
    void searchComponentsTest() {
        SearchParam.SearchComponentsQuery query = SearchParam.SearchComponentsQuery.builder()
                .repository("maven-releases")
                .group("com.transsnet.palmpay")
                .name("common-util")
                .version("0.0.1.230410-RELEASE")
                .build();
        NexusComponent.Components components = nexusSearchDriver.search(getConfig().getNexus(), query);
        components.getItems().forEach(this::print
        );
    }

}
