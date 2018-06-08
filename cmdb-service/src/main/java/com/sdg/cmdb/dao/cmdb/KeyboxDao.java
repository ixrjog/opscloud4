package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.keybox.ApplicationKeyDO;
import com.sdg.cmdb.domain.keybox.KeyboxLoginStatusDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxLoginChartsVO;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxServerVO;
import com.sdg.cmdb.domain.keybox.keyboxStatus.KeyboxUserVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 2016/11/2.
 */
@Component
public interface KeyboxDao {

    /**
     * 获取应用密钥
     *
     * @return
     */
    ApplicationKeyDO getApplicationKey();

    /**
     * 更新堡垒机公共密钥
     *
     * @param applicationKeyDO
     * @return
     */
    int updateApplicationKey(ApplicationKeyDO applicationKeyDO);


    /**
     * 添加用户可访问堡垒机
     *
     * @param userServerDO
     * @return
     */
    int addUserServer(KeyboxUserServerDO userServerDO);

    /**
     * 更新用户服务器组关系
     *
     * @param userServerDO
     * @return
     */
    int updateUserServer(KeyboxUserServerDO userServerDO);

    /**
     * 删除用户可访问的堡垒机
     *
     * @param userServerDO
     * @return
     */
    int delUserServer(KeyboxUserServerDO userServerDO);

    /**
     * 获取指定条件的堡垒机列表数目
     *
     * @param userServerDO
     * @return
     */
    long getUserServerSize(KeyboxUserServerDO userServerDO);

    /**
     * 获取指定服务器组的堡垒机授权用户数量
     *
     * @param serverGroupId
     * @return
     */
    int getServerGroupSize(long serverGroupId);

    /**
     * 获取指定条件的堡垒机列表
     *
     * @param userServerDO
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<KeyboxUserServerDO> getUserServerPage(
            @Param("userServer") KeyboxUserServerDO userServerDO,
            @Param("pageStart") int pageStart,
            @Param("pageLength") int pageLength);

    /**
     * 堡垒机服务器组用户管理分页数据查询
     *
     * @param serverGroupId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<KeyboxUserServerDO> getBoxGroupUserPage(
            @Param("serverGroupId") long serverGroupId,
            @Param("pageStart") int pageStart,
            @Param("pageLength") int pageLength);

    /**
     * 获取用户所有的服务器组集合
     *
     * @param username
     * @return
     */
    List<ServerGroupDO> getGroupListByUsername(@Param("username") String username);

    /**
     * 获取堡垒机服务器数目
     *
     * @param username
     * @param serverGroupId
     * @param envType
     * @return
     */
    long getBoxServerSize(
            @Param("username") String username, @Param("serverGroupId") long serverGroupId, @Param("envType") int envType);

    /**
     * 获取堡垒机服务器分页数据
     *
     * @param username
     * @param serverGroupId
     * @param envType
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ServerDO> getBoxServerPage(
            @Param("username") String username, @Param("serverGroupId") long serverGroupId, @Param("envType") int envType,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    List<KeyboxUserServerDO> getUserServerByGroupId(@Param("serverGroupId") long serverGroupId);


    /**
     * 查询所有数据KeyboxUserServer
     *
     * @return
     */
    List<KeyboxUserServerDO> getKeyboxUserServerAll();

    int addKeyboxStatus(KeyboxLoginStatusDO keyboxStatusDO);


    /**
     * 统计堡垒机登陆次数最多的服务器Top10
     *
     * @return
     */
    List<KeyboxServerVO> statusKeyboxServer();

    /**
     * 统计使用堡垒机最多的用户Top10
     *
     * @return
     */
    List<KeyboxUserVO> statusKeyboxUser();

    /**
     * 统计堡垒机授权用户数
     *
     * @return
     */
    int getKeyboxAuthedUserCnt();

    /**
     * 统计堡垒总登陆次数
     *
     * @return
     */
    int getKeyboxLoginCnt();


    /**
     * 查询最后登陆记录
     *
     * @param loginType
     * @return
     */
    List<KeyboxLoginStatusDO> getLastLogin(int loginType);

    /**
     * 统计最近cnt天的登陆次数
     * @return
     */
    List<KeyboxLoginChartsVO> statusKeyboxLoginByCnt(int cnt);

}
