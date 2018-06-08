package com.sdg.cmdb.dao.stash;


import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import com.sdg.cmdb.domain.todo.todoProperty.StashRepositoryDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface StashDao {

    /**
     * 查询指定条件的Stash项目数
     *
     * @param name
     * @return
     */
    long getStashProjectSize(
            @Param("name") String name
    );

    /**
     * 查询指定条件的Stash项目分页数据
     *
     * @param name
     * @param pageStart
     * @param length
     * @return
     */
    List<StashProjectDO> getStashProjectPage(
            @Param("name") String name,
            @Param("pageStart") long pageStart, @Param("length") int length);

    StashProjectDO getStashProjectById(
            @Param("id") long id
    );

    /**
     * 查询指定条件的StashRepository数
     *
     * @param project_id
     * @return
     */
    long getStashRepositorySize(
            @Param("name") String name,
            @Param("project_id") long project_id
    );

    /**
     * 查询指定条件的StashRepository分页数据
     *
     * @param project_id
     * @param pageStart
     * @param length
     * @return
     */
    List<StashRepositoryDO> getStashRepositoryPage(
            @Param("name") String name,
            @Param("project_id") long project_id,
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 查询所有的Stash项目分页数据
     *
     * @return
     */
    List<StashProjectDO> getStashProjectAll();


    /**
     * 查询指定条件的Stash Project
     * @param name
     * @return
     */
    StashProjectDO getStashProjectByName(
            @Param("name") String name
    );
}
