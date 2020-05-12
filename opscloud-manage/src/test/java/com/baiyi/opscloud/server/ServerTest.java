package com.baiyi.opscloud.server;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.service.server.OcServerService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/10 4:28 下午
 * @Version 1.0
 */
public class ServerTest extends BaseUnit {

    @Resource
    private OcServerService ocServerService;

    @Test
    void test() {
        Integer serialNumber = 0;

//        serialNumber = Integer.valueOf("x");
//        System.err.println(serialNumber);

        // 序号错误
        serialNumber = ocServerService.queryOcServerMaxSerialNumber(44);
        System.err.println(serialNumber);

    }


    @Test
    void test1() {
      List<OcServer> list =  ocServerService.queryAllOcServer();
      list.forEach( e ->{
              e.setLoginUser("gegejia");
          ocServerService.updateOcServer(e);

      });

    }
}
