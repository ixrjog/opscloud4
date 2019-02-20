package com.sdg.cmdb.domain.aliyunMQ;

import com.sdg.cmdb.domain.auth.UserVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AliyunMqGroupUserVO extends AliyunMqGroupUserDO implements Serializable {
    private static final long serialVersionUID = 6779120195112554864L;
    private UserVO userVO;
}
