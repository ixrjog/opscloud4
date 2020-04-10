package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.google.common.base.Joiner;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

import static com.baiyi.opscloud.ansible.config.AnsibleConfig.ANSIBLE_HOSTS;

/**
 * @Author baiyi
 * @Date 2020/4/6 5:27 下午
 * @Version 1.0
 */
public class AnsibleArgsBuilder {

    /**
     * https://docs.ansible.com/ansible/latest/reference_appendices/config.html
     * ansible.cfg
     * force_valid_group_names = ignore
     *
     *
     * @param config
     * @param args
     * @return
     */


    public static CommandLine build(AnsibleConfig config, AnsibleArgsBO args) {
        CommandLine commandLine = new CommandLine("bin");

        // --private-key
        // privateKey

        commandLine.addArgument("--key-file");
        if(!StringUtils.isEmpty(args.getKeyFile())){
            commandLine.addArgument(args.getKeyFile());
        }else{
            commandLine.addArgument(config.acqPrivateKey());
        }

        // 指定主机文件，如果不指定则用默认主机文件
        commandLine.addArgument("-i");
        if(!StringUtils.isEmpty(args.getInventory())){
            commandLine.addArgument(args.getInventory());
        }else{
            commandLine.addArgument(Joiner.on("/").join(config.acqInventoryPath() , ANSIBLE_HOSTS));
        }

        if(!StringUtils.isEmpty(args.getModuleName())){
            commandLine.addArgument("-m");
            commandLine.addArgument(args.getModuleName());
        }

        if(!StringUtils.isEmpty(args.getModuleArguments())){
            commandLine.addArgument("-a");
            commandLine.addArgument(args.getModuleArguments());
        }

        if(!StringUtils.isEmpty(args.getBecomeUser()) && !args.getBecomeUser().equalsIgnoreCase("root")){
            commandLine.addArgument("--become-user");
            commandLine.addArgument(args.getBecomeUser());
        }

        if(args.getForks() != null && args.getForks() != 5){
            commandLine.addArgument("-f");
            commandLine.addArgument(args.getForks().toString());
        }
        return commandLine;
    }
}
