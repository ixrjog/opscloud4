package com.baiyi.opscloud.datasource.ansible.builder.args;

/**
 * @Author baiyi
 * @Date 2023/3/22 09:50
 * @Version 1.0
 */
public interface IAnsibleArgs {

    String getInventory();

    String getKeyFile();

    Integer getForks();

    String getBecomeUser();

    boolean isVersion();

}