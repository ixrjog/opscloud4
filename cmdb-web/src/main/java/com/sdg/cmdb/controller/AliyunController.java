package com.sdg.cmdb.controller;

import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.aliyun.*;
import com.sdg.cmdb.domain.aliyunMQ.AliyunMqGroupUserDO;
import com.sdg.cmdb.domain.aliyunMQ.AliyunMqGroupVO;
import com.sdg.cmdb.domain.aliyunMQ.CreateOnsGroup;
import com.sdg.cmdb.domain.aliyunMQ.CreateTopic;
import com.sdg.cmdb.domain.server.EcsTemplateDO;
import com.sdg.cmdb.service.AliyunMQService;
import com.sdg.cmdb.service.AliyunRAMService;
import com.sdg.cmdb.service.AliyunService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private AliyunRAMService aliyunRamService;


    @Resource
    private AliyunMQService mqService;

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


    /**
     * 保存EcsTemplate
     *
     * @return
     */
    @RequestMapping(value = "/template/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveTemplate(@RequestBody EcsTemplateDO ecsTemplateDO) {
        return new HttpResult(aliyunService.saveTemplate(ecsTemplateDO));
    }

    @RequestMapping(value = "/template/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult saveTemplate(@RequestParam long id) {
        return new HttpResult(aliyunService.delTemplate(id));
    }

    // TODO MQ
    @RequestMapping(value = "/mq/topic/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryTopic(@RequestParam String topic) {
        return new HttpResult(mqService.topicList(topic, null));
    }

    @RequestMapping(value = "/mq/topic/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult createTopic(@RequestBody CreateTopic createTopic) {
        return new HttpResult(mqService.createTopic(createTopic));
    }

    @RequestMapping(value = "/mq/group/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult careteSubscribe(@RequestBody CreateOnsGroup createOnsGroup) {
        return new HttpResult(mqService.createGroup(createOnsGroup));
    }

    @RequestMapping(value = "/mq/group/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveGroupSubscribe(@RequestBody AliyunMqGroupVO aliyunMqGroupVO) {
        return new HttpResult(mqService.saveGroup(aliyunMqGroupVO));
    }

    @RequestMapping(value = "/mq/group/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryGroupSubscribe() {
        return new HttpResult(mqService.groupList(null));
    }

    @RequestMapping(value = "/mq/group/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryGroupSubscribe(@RequestParam long id) {
        return new HttpResult(mqService.getGroup(id));
    }

    @RequestMapping(value = "/mq/group/user/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult addGroupUser(@RequestBody AliyunMqGroupUserDO aliyunMqGroupUserDO) {
        return new HttpResult(mqService.addGroupUser(aliyunMqGroupUserDO));
    }

    @RequestMapping(value = "/mq/group/user/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delGroupUser(@RequestParam long id) {
        return new HttpResult(mqService.delGroupUser(id));
    }


    @RequestMapping(value = "/mq/consumer/status", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getSubscribeStatus(@RequestParam String cid) {
        return new HttpResult(mqService.consumerStatus(cid, null));
    }
    // TODO MQ


    /**
     * 获取指定条件的RAM策略分页数据
     *
     * @param policyName
     * @param description
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/ram/policy/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryPolicyPage(@RequestParam String policyName, @RequestParam String description,
                                      @RequestParam int page, @RequestParam int length) {
        return new HttpResult(aliyunRamService.getPolicyPage(policyName, description, page, length));
    }

    @RequestMapping(value = "/ram/policy/set", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult setPolicyAllowsPage(
            @RequestParam long id) {
        return new HttpResult(aliyunRamService.setRamPolicyAllows(id));
    }

    @RequestMapping(value = "/ram/policy/update", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updatePolicy() {
        return new HttpResult(aliyunRamService.updateRamPolicies());
    }


    /**
     * 工作流查询阿里云RAM策略
     *
     * @param queryName
     * @return
     */
    @RequestMapping(value = "/ram/policy/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryPolicyPage(@RequestParam String queryName) {
        return new HttpResult(aliyunRamService.queryPolicy(queryName));
    }

    /**
     * 获取指定条件的RAM用户分页数据
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    @RequestMapping(value = "/ram/user/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryRamUserPage(@RequestParam String username, @RequestParam String userTag,
                                       @RequestParam int page, @RequestParam int length) {
        return new HttpResult(aliyunRamService.getRamUserPage(username, userTag, page, length));
    }

    @RequestMapping(value = "/ram/user/update", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult updateRamUser() {
        return new HttpResult(aliyunRamService.updateRamUsers());
    }


    @RequestMapping(value = "/ram/user/get", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getRamUser( @RequestParam long userId) {
        return new HttpResult(aliyunRamService.getRamUser(userId));
    }

    @RequestMapping(value = "/ram/user/import", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult importRamUserPolicy(@RequestParam long id) {
        return new HttpResult(aliyunRamService.importRamUserPolicy(id));
    }

    @RequestMapping(value = "/ram/user/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult getRamUser(@RequestBody AliyunRamUserDO aliyunRamUserDO) {
        return new HttpResult(aliyunRamService.saveRamUser(aliyunRamUserDO));
    }

}
