package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.ReadmeMDDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.readmeMD.ReadmeMDDO;
import com.sdg.cmdb.domain.readmeMD.ReadmeMDVO;
import com.sdg.cmdb.util.BeanCopierUtils;
import com.sdg.cmdb.util.SessionUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReadmeMDService {

    @Autowired
    private ReadmeMDDao mdDao;

    @Autowired
    private AuthService authService;

    @Autowired
    private NginxService nginxService;

    public static final String ANDROID_CI_README = "ANDROID_CI_README";

    public static final String JMS_README = "JMS_README";

    @Value("#{cmdb['coco.host']}")
    private String cocoHost;


    /**
     * 查询
     *
     * @param mdKey
     * @return
     */
    public ReadmeMDVO getMD(String mdKey) {
        ReadmeMDDO md = mdDao.getReadmeMDByKey(mdKey);
        ReadmeMDVO mdVO= BeanCopierUtils.copyProperties(md,ReadmeMDVO.class);
        invokePreview(mdVO);
        return mdVO;
    }

    /**
     * 更新
     *
     * @param readmeMDDO
     * @return
     */
    public BusinessWrapper<Boolean> saveMD(ReadmeMDDO readmeMDDO) {
        if (!checkAuth(readmeMDDO.getMdKey())) return new BusinessWrapper<Boolean>(ErrorCode.checkAuthFailure);
        try {
            if (readmeMDDO.getId() == 0) {
                mdDao.addReadmeMD(readmeMDDO);
            } else {
                mdDao.updateReadmeMD(readmeMDDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private boolean checkAuth(String mdKey) {
        String username = SessionUtils.getUsername();
        if (authService.isRole(username, RoleDO.roleAdmin)) return true;

        if(mdKey.equals( ANDROID_CI_README )){
            if (authService.isRole(username, RoleDO.roleDevelopAndroid)) return true;
        }

        return false;
    }

    private void invokePreview(ReadmeMDVO readmeMDVO){
        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("cocoHost", cocoHost);
        valuesMap.put("username", SessionUtils.getUsername());
        invokeValuesMap(readmeMDVO,valuesMap);
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        readmeMDVO.setPreview(sub.replace(readmeMDVO.getMdBody()));
    }

    private void invokeValuesMap(ReadmeMDVO readmeMDVO,Map<String, String> valuesMap){
        if(readmeMDVO.getMdKey().equals("NGINX_TCP_DUBBO_README")){
            // 注入配置
            valuesMap.put("dubbo-resolve", nginxService.scanNginxTcpDubbo());
        }
    }


}
