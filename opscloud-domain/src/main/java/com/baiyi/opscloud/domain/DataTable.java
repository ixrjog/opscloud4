package com.baiyi.opscloud.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;


@ApiModel
@Data
public class DataTable<T> {

    public final static DataTable EMPTY = new DataTable<>();

    @ApiModelProperty(value = "分页数据")
    private List<T> data;

    @ApiModelProperty(value = "当前页码")
    private int nowPage;

    @ApiModelProperty(value = "总记录数")
    private long totalNum;

    public DataTable(List<T> data, long totalNum) {
        this.data = data;
        this.totalNum = totalNum;
    }

    public DataTable() {
        this.data = Collections.EMPTY_LIST;
        this.totalNum = 0;
    }
}
