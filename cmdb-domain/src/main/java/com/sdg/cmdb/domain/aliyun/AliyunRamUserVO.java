package com.sdg.cmdb.domain.aliyun;

import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.sdg.cmdb.domain.auth.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class AliyunRamUserVO extends AliyunRamUserDO implements Serializable {

    private List<ListPoliciesForUserResponse.Policy> policyList;
    private List<ListAccessKeysResponse.AccessKey> accessKeyList;
    private UserVO userVO;


}
