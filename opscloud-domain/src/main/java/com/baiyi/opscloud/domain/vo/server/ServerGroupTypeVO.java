package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/5/24 10:34 上午
 * @Version 1.0
 */
public class ServerGroupTypeVO {

    public interface IServerGroupType {
        void setServerGroupType(ServerGroupTypeVO.ServerGroupType serverGroupType);
        Integer getServerGroupTypeId();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupType extends BaseVO implements Serializable {

        private static final long serialVersionUID = -6926530768540517199L;
        private Integer id;

        @NotNull(message = "组类型名称不能为空")
        private String name;

        private String color;

        private String comment;

        @ApiModelProperty(value = "使用类型的服务器组数量",example = "1")
        private Integer serverGroupSize;
    }
}
