package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/7/9 12:01
 * @Version 1.0
 */
public class KubernetesPodTest extends BaseKubernetesTest {

    @Test
    void getLogTest(){
       String l = KubernetesPodDriver.getLog(getConfig().getKubernetes(),"prod","posp-7878b5d9ff-cf95v");
       print(l);
    }
}
