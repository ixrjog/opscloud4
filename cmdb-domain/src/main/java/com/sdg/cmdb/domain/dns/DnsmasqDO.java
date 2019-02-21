package com.sdg.cmdb.domain.dns;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/7/11.
 */
@Data
@EqualsAndHashCode
public class DnsmasqDO implements Serializable {

    private static final long serialVersionUID = 7562425153421096263L;
    private long id;
    // 行记录
    private String dnsItem;
    private String content;
    private String gmtCreate;
    private String gmtModify;

}
