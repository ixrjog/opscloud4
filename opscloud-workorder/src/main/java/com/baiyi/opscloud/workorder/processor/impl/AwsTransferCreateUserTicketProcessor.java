package com.baiyi.opscloud.workorder.processor.impl;

import com.amazonaws.services.transfer.model.HomeDirectoryMapEntry;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.aws.transfer.driver.AwsTransferDriver;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.AwsTransferCreateUserEntry;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author baiyi
 * @Date 2023/10/9 19:25
 * @Version 1.0
 */
@Slf4j
@Component
public class AwsTransferCreateUserTicketProcessor extends BaseTicketProcessor<AwsTransferCreateUserEntry.TransferUser> {

    private final static String REGION_ID = "eu-west-1";

    private final static String ROLE = "arn:aws:iam::502076313352:role/transfer-to-s3";

    private final static String SERVER_ID = "s-6d384561f0da4b148";

    private final static String INSTANCE_UUID = "9877af2fa97f48faa34608531df354d2";

    private final static String TARGET = "/transsnet-download-production/{}";

    @Resource
    private DsConfigManager dsConfigManager;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.AWS_TRANSFER_CREATE_USER.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, AwsTransferCreateUserEntry.TransferUser entry) throws TicketProcessException {
        ticketEntry.setInstanceUuid(INSTANCE_UUID);
        AwsConfig.Aws config = getDsConfig(ticketEntry).getAws();
        final AwsTransferCreateUserEntry.TransferUser transferUser = toEntry(ticketEntry.getContent());
        final String target = StringFormatter.format(TARGET, transferUser.getUserName());
        Collection<HomeDirectoryMapEntry> homeDirectoryMappings = AwsTransferDriver.buildHomeDirectoryMappings(target);
        try {
            AwsTransferDriver.createUser(REGION_ID, config, homeDirectoryMappings, transferUser.getUserName(), ROLE, SERVER_ID, transferUser.getSshPublicKey());
        } catch (Exception e) {
            throw new TicketProcessException("创建Transfer账户失败: {}", e.getMessage());
        }
    }

    private AwsConfig getDsConfig(WorkOrderTicketEntry ticketEntry) {
        DatasourceConfig datasourceConfig = dsConfigManager.getConfigByInstanceUuid(ticketEntry.getInstanceUuid());
        return dsConfigManager.build(datasourceConfig, AwsConfig.class);
    }

    @Override
    protected Class<AwsTransferCreateUserEntry.TransferUser> getEntryClassT() {
        return AwsTransferCreateUserEntry.TransferUser.class;
    }

    @Override
    protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        AwsTransferCreateUserEntry.TransferUser transferUser = toEntry(ticketEntry.getContent());
        verifyUserName(transferUser.getUserName());
        if (StringUtils.isBlank(transferUser.getSshPublicKey())) {
            throw new TicketVerifyException("SshPublicKey cannot be empty");
        }
        verifySshPublicKey(transferUser.getSshPublicKey());
    }

    public static void verifyUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            throw new TicketVerifyException("UserName cannot be empty");
        }
        if (!userName.matches("^\\w[\\w@.-]{2,99}$")) {
            throw new TicketVerifyException("The UserName does not meet the specifications ! Length Constraints: Minimum length of 3. Maximum length of 100. Pattern: ^[\\w][\\w@.-]{2,99}$");
        }
    }

    public static void verifySshPublicKey(String sshPublicKey) {
        if (StringUtils.isBlank(sshPublicKey)) {
            throw new TicketVerifyException("SshPublicKey cannot be empty");
        }
        String fingerprint = SSHUtil.getFingerprint(sshPublicKey);
        if (StringUtils.isBlank(fingerprint)) {
            throw new TicketVerifyException("Incorrect format of SshPublicKey");
        }
    }

}