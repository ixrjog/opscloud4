package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbSc;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:38 下午
 * @Since 1.0
 */
public interface OcAliyunSlbSCService {

    OcAliyunSlbSc queryOcAliyunSlbScById(Integer id);

    void addOcAliyunSlbSc(OcAliyunSlbSc ocAliyunSlbSc);

    void updateOcAliyunSlbSc(OcAliyunSlbSc ocAliyunSlbSc);

    void deleteOcAliyunSlbSc(int id);

    OcAliyunSlbSc queryOcAliyunSlbScBySCId(String scId);

    List<OcAliyunSlbSc> queryOcAliyunSlbScAll();

    DataTable<OcAliyunSlbSc> queryOcAliyunSlbScPage(AliyunSLBParam.SCPageQuery pageQuery);
}
