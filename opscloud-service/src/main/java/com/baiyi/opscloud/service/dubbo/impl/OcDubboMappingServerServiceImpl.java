package com.baiyi.opscloud.service.dubbo.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcDubboMappingServer;
import com.baiyi.opscloud.mapper.opscloud.OcDubboMappingServerMapper;
import com.baiyi.opscloud.service.dubbo.OcDubboMappingServerService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/12 10:44 上午
 * @Version 1.0
 */
@Service
public class OcDubboMappingServerServiceImpl implements OcDubboMappingServerService {

    @Resource
    private OcDubboMappingServerMapper ocDubboMappingServerMapper;

    @Override
    public List<OcDubboMappingServer> queryOcDubboMappingServerByIpAndIsMapping(String ip, boolean isMapping) {
        Example example = new Example(OcDubboMappingServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ip", ip);
        if (isMapping) {
            criteria.andNotEqualTo("tcpMappingId", 0);
        } else {
            criteria.andEqualTo("tcpMappingId", 0);
        }
        return ocDubboMappingServerMapper.selectByExample(example);
    }

    @Override
    public List<OcDubboMappingServer> queryOcDubboMappingServerUnmapped() {
        Example example = new Example(OcDubboMappingServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tcpMappingId", 0);
        return ocDubboMappingServerMapper.selectByExample(example);
    }

    @Override
    public OcDubboMappingServer queryOneBindOcDubboMappingServer(String ip){
        Example example = new Example(OcDubboMappingServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ip", ip);
        criteria.andNotEqualTo("tcpMappingId", 0);
        PageHelper.startPage(1, 1);
        return ocDubboMappingServerMapper.selectOneByExample(example);
    }

    @Override
    public List<OcDubboMappingServer> queryOcDubboMappingServerByMappingId(int mappingId) {
        Example example = new Example(OcDubboMappingServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mappingId", mappingId);
        return ocDubboMappingServerMapper.selectByExample(example);
    }

    @Override
    public List<OcDubboMappingServer> queryOcDubboMappingServerByTcpMappingId(int tcpMappingId){
        Example example = new Example(OcDubboMappingServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tcpMappingId",tcpMappingId);
        return ocDubboMappingServerMapper.selectByExample(example);
    }

    @Override
    public void addOcDubboMappingServer(OcDubboMappingServer ocDubboMappingServer) {
        ocDubboMappingServerMapper.insert(ocDubboMappingServer);
    }

    @Override
    public void updateOcDubboMappingServer(OcDubboMappingServer ocDubboMappingServer) {
        ocDubboMappingServerMapper.updateByPrimaryKey(ocDubboMappingServer);
    }

    @Override
    public OcDubboMappingServer queryOcDubboMappingServerByUniqueKey(OcDubboMappingServer ocDubboMappingServer) {
        Example example = new Example(OcDubboMappingServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mappingId", ocDubboMappingServer.getMappingId());
        criteria.andEqualTo("ip", ocDubboMappingServer.getIp());
        criteria.andEqualTo("dubboPort", ocDubboMappingServer.getDubboPort());
        return ocDubboMappingServerMapper.selectOneByExample(example);
    }
}
