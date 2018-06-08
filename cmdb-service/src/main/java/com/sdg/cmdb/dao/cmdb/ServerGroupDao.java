package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupUseTypeDO;
import com.sdg.cmdb.domain.server.ServerGroupVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/1.
 */
@Component
public interface ServerGroupDao {

    /**
     * 查询一定条件下服务器组数量
     *
     * @param groups
     * @param name
     * @param useType
     * @return
     */
    long queryServerGroupSize(@Param("list") List<String> groups, @Param("name") String name, @Param("useType") int useType);

    /**
     * 查询一定条件下分页服务器组信息
     *
     * @param groups
     * @param start
     * @param length
     * @param name
     * @param useType
     * @return
     */
    List<ServerGroupDO> queryServerGroupPage(
            @Param("list") List<String> groups, @Param("start") long start, @Param("length") int length,
            @Param("name") String name, @Param("useType") int useType);


    long queryProjectServerGroupSize( @Param("name") String name, @Param("useType") int useType);

    List<ServerGroupDO> queryLogServiceServerGroupPage(@Param("start") long start,
                                                       @Param("length") int length,
                                                       @Param("name") String name,
                                                       @Param("username") String username,
                                                       @Param("useType") int useType);


    List<ServerGroupDO> queryProjectServerGroupPage(
            @Param("start") long start, @Param("length") int length,
            @Param("name") String name, @Param("useType") int useType);




    long queryLogServiceServerGroupSize(@Param("name") String name,
                                        @Param("username") String username,
                                        @Param("useType") int useType);


    /**
     * 查询服务器组授权用户信息
     *
     * @param name
     * @param start
     * @param length
     * @return
     */
    List<ServerGroupVO> queryKeyboxServerGroupPage(
            @Param("name") String name, @Param("start") long start, @Param("length") int length
    );

    /**
     * 更新服务器组信息
     *
     * @param serverGroupDO
     * @return
     */
    int updateServerGroupInfo(ServerGroupDO serverGroupDO);

    /**
     * 新增服务器组信息
     *
     * @param serverGroupDO
     * @return
     */
    int addServerGroupInfo(ServerGroupDO serverGroupDO);

    /**
     * 删除服务器信息
     *
     * @param id
     * @return
     */
    int delServerGroupInfo(@Param("id") long id);

    /**
     * 服务器组绑定指定的ip组
     *
     * @param serverGroupId
     * @param ipNetworkId
     * @param ipType
     * @return
     */
    int addServerIPGroup(
            @Param("serverGroupId") long serverGroupId,
            @Param("ipNetworkId") long ipNetworkId, @Param("ipType") int ipType);

    /**
     * 服务器组解绑指定的ip组
     *
     * @param serverGroupId
     * @param ipNetworkId
     * @return
     */
    int delServerIPGroup(@Param("serverGroupId") long serverGroupId, @Param("ipNetworkId") long ipNetworkId);

    /**
     * 获取指定id的group
     *
     * @param groupId
     * @return
     */
    ServerGroupDO queryServerGroupById(@Param("groupId") long groupId);

    /**
     * 获取指定name的group
     *
     * @param name
     * @return
     */
    ServerGroupDO queryServerGroupByName(@Param("name") String name);

    /**
     * 查询所有的group
     *
     * @return
     */
    List<ServerGroupDO> queryServerGroup();

    /**
     * 按使用类型查询
     * @param useType
     * @return
     */
    List<ServerGroupDO> queryServerGroupByUseType(@Param("useType") int useType);

    /**
     * 获取指定用户对应的服务器组集合
     *
     * @param username
     * @return
     */
    List<ServerGroupDO> getGroupsByName(@Param("username") String username);


    /**
     * 查询一定条件下分页服务器组使用类型数量
     * @param typeName
     * @return
     */
    long queryUseTypeSize(@Param("typeName") String typeName);

    /**
     * 查询一定条件下分页服务器组使用类型信息
     * @param typeName
     * @param start
     * @param length
     * @return
     */
    List<ServerGroupUseTypeDO> queryUseTypePage(
            @Param("typeName") String typeName, @Param("start") long start, @Param("length") int length);

    /**
     * 更新服务器组使用类型信息
     *
     * @param serverGroupUseTypeDO
     * @return
     */
    int updateServerGroupUseType(ServerGroupUseTypeDO serverGroupUseTypeDO);

    /**
     * 新增服务器组使用类型信息
     *
     * @param serverGroupUseTypeDO
     * @return
     */
    int addServerGroupUseType(ServerGroupUseTypeDO serverGroupUseTypeDO);

    int delServerGroupUseType(@Param("id") long id);

    ServerGroupUseTypeDO getServerGroupUseTypeByUseType(@Param("useType") int useType);

}
