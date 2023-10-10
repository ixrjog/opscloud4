package com.baiyi.opscloud.datasource.aws.transfer.driver;

import com.amazonaws.services.transfer.model.CreateUserRequest;
import com.amazonaws.services.transfer.model.CreateUserResult;
import com.amazonaws.services.transfer.model.HomeDirectoryMapEntry;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.transfer.service.AwsTransferService;
import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * @Author baiyi
 * @Date 2023/10/9 17:18
 * @Version 1.0
 */
public class AwsTransferDriver {

    public static final String HOME_DIRECTORY_TYPE_LOGICAL = "LOGICAL";

    public static CreateUserResult createUser(String regionId, AwsConfig.Aws config, Collection<HomeDirectoryMapEntry> homeDirectoryMappings, String userName, String role, String serverId, String sshPublicKey) {
        return createUser(regionId, config, HOME_DIRECTORY_TYPE_LOGICAL, homeDirectoryMappings, userName, role, serverId, sshPublicKey);
    }

    public static CreateUserResult createUser(String regionId, AwsConfig.Aws config, String homeDirectoryType, Collection<HomeDirectoryMapEntry> homeDirectoryMappings, String userName, String role, String serverId, String sshPublicKey) {
        CreateUserRequest request = new CreateUserRequest();
        request.setHomeDirectoryType(homeDirectoryType);
        request.setHomeDirectoryMappings(homeDirectoryMappings);
        request.setUserName(userName);
        request.setRole(role);
        request.setServerId(serverId);
        request.setSshPublicKeyBody(sshPublicKey);
        return AwsTransferService.buildAwsTransfer(regionId, config).createUser(request);
    }

    public static Collection<HomeDirectoryMapEntry> buildHomeDirectoryMappings(String target) {
        return buildHomeDirectoryMappings("/", target);
    }

    public static Collection<HomeDirectoryMapEntry> buildHomeDirectoryMappings(String entry, String target) {
        HomeDirectoryMapEntry homeDirectoryMapEntry = new HomeDirectoryMapEntry().withEntry(entry)
                .withTarget(target);
        return Lists.newArrayList(homeDirectoryMapEntry);
    }

}
