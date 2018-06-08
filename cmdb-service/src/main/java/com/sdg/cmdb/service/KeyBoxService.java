package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.keybox.*;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxStatusVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;

import java.util.List;

/**
 * Created by zxxiao on 2016/11/20.
 */
public interface KeyBoxService {

    /**
     * 获取指定条件下的可访问列表
     * @param userServerDO
     * @param page
     * @param length
     * @return
     */
    TableVO<List<KeyboxUserServerVO>> getUserServerPage(KeyboxUserServerDO userServerDO, int page, int length);


    BusinessWrapper<String> launchUserGetway(String username);

    /**
     * 获取满足条件的数目
     * @param userServerDO
     * @return
     */
    long getUserServerSize(KeyboxUserServerDO userServerDO);

    /**
     * 授权用户
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> authUserKeybox(String username);

    /**
     * 终止用户授权
     * @param username
     * @return
     */
    BusinessWrapper<Boolean>  delUserKeybox(String username);

    /**
     * 新增 or 更新用户服务器组关系
     * @param userServerVO
     * @return
     */
    BusinessWrapper<Boolean> saveUserGroup(KeyboxUserServerVO userServerVO);

    /**
     * 删除用户服务器组
     * @param userServerDO
     * @return
     */
    BusinessWrapper<Boolean> delUserGroup(KeyboxUserServerDO userServerDO);

    /**
     * 创建指定用户的配置文件
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> createUserGroupConfigFile(String username);

    /**
     * 创建所有用户的配置文件
     * @return
     */
    BusinessWrapper<Boolean> createAllUserGroupConfigFile();

    /**
     * 查询被授权的堡垒机服务器列表
     * @param serverGroupId
     * @param envType
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ServerVO>> getServerList(long serverGroupId, int envType, int page, int length);

    void invokeServerInfo(ServerVO serverVO);

    /**
     * 校验用户（删除离职用户的关联数据）
     * @return
     */
    BusinessWrapper<Boolean> checkUser();

    /**
     * 堡垒机服务器组用户管理分页数据查询
     * @param serverGorupId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<KeyboxUserServerVO>> getBoxGroupUserPage(long serverGorupId, int page, int length);

    /**
     * 查询密钥
     * @return
     */
    ApplicationKeyVO getApplicationKey();

    /**
     * 保存key
     * @param applicationKeyVO
     * @return
     */
    BusinessWrapper<Boolean> saveApplicationKey(ApplicationKeyVO applicationKeyVO) ;


    /**
     * 堡垒机统计
     * @return
     */
    KeyboxStatusVO keyboxStatus();

    BusinessWrapper<Boolean> getwayStatus(String username ,String ip);

}
