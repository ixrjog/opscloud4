package com.sdg.cmdb.service.jms.service;

import com.sdg.cmdb.service.jms.base.ApiConstants;
import com.sdg.cmdb.service.jms.model.AssetsPermission;

import java.util.Map;

public class JmsPermissionService extends JmsBaseService {

    public JmsPermissionService(String url, String token) {
        this.URL = url;
        this.TOKEN = token;
    }

    public JmsPermissionService(String url, String username, String password) {
        super(url, username, password);
    }

    //资产授权规则
    public Map<String, String> addAssetpermission(AssetsPermission assetsPermission) {
        return super.add(assetsPermission, ApiConstants.ASSET_PERMISSIONS);
    }

    public Map<String, String> updateAssetpermission(AssetsPermission assetsPermission) {
        return super.update(assetsPermission, ApiConstants.ASSET_PERMISSIONS, assetsPermission.getId());
    }

    public Map<String, String> deleteAssetpermission(AssetsPermission assetsPermission) {
        return super.delete(assetsPermission, ApiConstants.ASSET_PERMISSIONS, assetsPermission.getId());
    }

    public Map<String, String> queryAssetpermission(String id) {
        return super.query(id, ApiConstants.ASSET_PERMISSIONS);
    }
}
