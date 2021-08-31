package com.baiyi.opscloud.ansible.args.base;

/**
 * @Author baiyi
 * @Date 2021/8/31 10:50 上午
 * @Version 1.0
 */
public interface IAnsbileArgs {


    String getInventory();

    String getKeyFile();

    Integer getForks();

    String getBecomeUser();

    boolean isVersion();


}
