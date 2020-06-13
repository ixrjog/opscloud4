package com.baiyi.opscloud.domain.vo.cloud;

import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/2 11:16 上午
 * @Version 1.0
 */
public class CloudDBDatabaseVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudDBDatabase {

        private List<TagVO.Tag> tags;

        private EnvVO.Env env;

        @ApiModelProperty(value = "云数据库类型")
        private Integer cloudDbType;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "云数据库id")
        private Integer cloudDbId;

        @ApiModelProperty(value = "云数据库实例id")
        private String dbInstanceId;

        @ApiModelProperty(value = "数据库名称")
        private String dbName;

        @ApiModelProperty(value = "数据库实例类型")
        private String engine;

        @ApiModelProperty(value = "描述")
        private String dbDescription;

        /**
         * 数据库状态，取值：
         * <p>
         * Creating：创建中
         * Running：使用中
         * Deleting：删除中
         */
        @ApiModelProperty(value = "数据库状态")
        private String dbStatus;

        @ApiModelProperty(value = "字符集")
        private String characterSetName;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "create_time")
        private Date createTime;

        @ApiModelProperty(value = "update_time")
        private Date updateTime;

        @ApiModelProperty(value = "备注")
        private String comment;

    }


}
