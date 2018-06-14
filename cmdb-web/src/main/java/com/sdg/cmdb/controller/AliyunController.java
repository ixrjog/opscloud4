package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.service.AliyunService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2017/6/12.
 */
@Controller
@RequestMapping("/aliyun")
public class AliyunController {

    @Resource
    private AliyunService aliyunService;

    /**
     * 获取EcsImage列表
     *
     * @return
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryImage(@RequestParam String queryName) {
        List<AliyunEcsImageVO> listVO = aliyunService.queryAliyunImage(queryName);
        return new HttpResult(listVO);
    }

    /**
     * 保存EcsImage
     *
     * @return
     */
    @RequestMapping(value = "/image/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult queryImage(@RequestBody AliyunEcsImageDO aliyunEcsImageDO) {
        return new HttpResult(aliyunService.saveAliyunImage(aliyunEcsImageDO));
    }

    /**
     * 删除EcsImage
     *
     * @return
     */
    @RequestMapping(value = "/image/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult queryImage(@RequestParam long id) {
        return new HttpResult(aliyunService.delAliyunImage(id));
    }


    /**
     * 获取网络类型
     *
     * @return
     */
    @RequestMapping(value = "/network", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryNetwork() {
        List<AliyunNetworkDO> list = aliyunService.getAliyunNetwork();
        return new HttpResult(list);
    }

    /**
     * 获取vpc列表
     *
     * @return
     */
    @RequestMapping(value = "/vpc", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVpc(@RequestParam String networkType, @RequestParam String queryName) {
        List<AliyunVpcVO> list = aliyunService.queryAliyunVpc(networkType, queryName);
        return new HttpResult(list);
    }

    /**
     * 获取vpc网络详情
     *
     * @return
     */
    @RequestMapping(value = "/vpc/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVpc() {
        List<AliyunVpcVO> list = aliyunService.getAliyunVpc();
        return new HttpResult(list);
    }

    @RequestMapping(value = "/vpc/rsync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getVpcRsync() {
        return new HttpResult(aliyunService.rsyncAliyunNetwork());
    }

    @RequestMapping(value = "/vpc/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delVpc(@RequestParam long id) {
        return new HttpResult(aliyunService.delAliyunVpc(id));
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @RequestMapping(value = "/vswitch", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVswitch(@RequestParam long vpcId, @RequestParam String queryName) {
        List<AliyunVswitchVO> list = aliyunService.queryAliyunVswitch(vpcId, queryName);
        return new HttpResult(list);
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @RequestMapping(value = "/securityGroup", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryVpcSecurityGroup(@RequestParam long vpcId, @RequestParam String queryName) {
        List<AliyunVpcSecurityGroupVO> list = aliyunService.querySecurityGroup(vpcId, queryName);
        return new HttpResult(list);
    }

    /**
     * 获取地域列表
     *
     * @return
     */
    @RequestMapping(value = "/api/describeRegions", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDescribeRegions() {
        return new HttpResult(aliyunService.getDescribeRegions());
    }

    /**
     * 获取ECS镜像列表
     *
     * @return
     */
    @RequestMapping(value = "/api/describeImages", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDescribeImages() {
        return new HttpResult(aliyunService.getImages());
    }

    /**
     * 查询实例类型
     *
     * @return
     */
    @RequestMapping(value = "/api/describeInstanceTypes", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDescribeInstanceTypes(@RequestParam String regionId) {
        return new HttpResult(aliyunService.getInstanceTypes(regionId));
    }

    /**
     * 查询实例类型
     *
     * @return
     */
    @RequestMapping(value = "/api/describeZones", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getZones(@RequestParam String regionId) {
        return new HttpResult(aliyunService.getZones(regionId));
    }

}
