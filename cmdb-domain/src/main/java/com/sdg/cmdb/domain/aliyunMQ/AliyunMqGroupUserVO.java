package com.sdg.cmdb.domain.aliyunMQ;

import com.sdg.cmdb.domain.auth.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class AliyunMqGroupUserVO extends AliyunMqGroupUserDO implements Serializable {
    private static final long serialVersionUID = 6779120195112554864L;
    private UserVO userVO;
}
