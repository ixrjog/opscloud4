package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.mapper.opscloud.OcServerMapper;
import com.baiyi.opscloud.service.server.OcServerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:25 下午
 * @Version 1.0
 */
@Service
public class OcServerServiceImpl implements OcServerService {

    @Resource
    private OcServerMapper ocServerMapper;

    @Override
    public OcServer queryOcServerByPrivateIp(String privateIp) {
        Example example = new Example(OcServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("privateIp", privateIp);
        return ocServerMapper.selectOneByExample(example);
    }

    @Override
    public OcServer queryOcServerByIp(String ip) {
        // select * from oc_server WHERE ( private_ip = #{ip} ) or( public_ip = #{ip} )
        Example example = new Example(OcServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("privateIp", ip);
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("publicIp", ip);
        example.or(criteria2);
        PageHelper.startPage(1, 1);
        return ocServerMapper.selectOneByExample(example);
    }

    @Override
    public OcServer queryOcServerById(int id) {
        return ocServerMapper.selectByPrimaryKey(id);
    }

    @Override
    public int countByServerGroupId(int id) {
        Example example = new Example(OcServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", id);
        return ocServerMapper.selectCountByExample(example);
    }

    @Override
    public int countByEnvType(int envType) {
        Example example = new Example(OcServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envType", envType);
        return ocServerMapper.selectCountByExample(example);
    }

    @Override
    public DataTable<OcServer> queryOcServerByParam(ServerParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcServer> ocServerList = ocServerMapper.queryOcServerByParam(pageQuery);
        return new DataTable<>(ocServerList, page.getTotal());
    }

    @Override
    public DataTable<OcServer> fuzzyQueryOcServerByParam(ServerParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcServer> ocServerList = ocServerMapper.fuzzyQueryOcServerByParam(pageQuery);
        return new DataTable<>(ocServerList, page.getTotal());
    }

    @Override
    public int countOcServerByServerGroupId(int serverGroupId) {
        Example example = new Example(OcServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return ocServerMapper.selectCountByExample(example);
    }

    @Override
    public List<OcServer> queryOcServerByServerGroupId(int serverGroupId) {
        Example example = new Example(OcServer.class);
        example.setOrderByClause("serial_number");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return ocServerMapper.selectByExample(example);
    }

    @Override
    public List<OcServer> queryAllOcServer() {
        return ocServerMapper.selectAll();
    }

    @Override
    public void addOcServer(OcServer ocServer) {
        ocServerMapper.insert(ocServer);
    }

    @Override
    public void updateOcServer(OcServer ocServer) {
        ocServerMapper.updateByPrimaryKey(ocServer);
    }

    @Override
    public void deleteOcServerById(int id) {
        ocServerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int queryOcServerMaxSerialNumber(int serverGroupId) {
        OcServer ocServer = ocServerMapper.queryOcServerMaxSerialNumber(serverGroupId);
        if (ocServer == null)
            return 0;
        return ocServer.getSerialNumber();
    }

    @Override
    public int queryOcServerMaxSerialNumber(int serverGroupId, int envType) {
        OcServer ocServer = ocServerMapper.queryOcServerMaxSerialNumberByEnvType(serverGroupId, envType);
        if (ocServer == null)
            return 0;
        return ocServer.getSerialNumber();
    }


}
