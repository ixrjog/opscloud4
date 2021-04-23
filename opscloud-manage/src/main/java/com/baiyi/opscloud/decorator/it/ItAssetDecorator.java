package com.baiyi.opscloud.decorator.it;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.service.it.*;
import com.baiyi.opscloud.service.org.OcOrgDepartmentService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 3:50 下午
 * @Since 1.0
 */

@Component("ItAssetDecorator")
public class ItAssetDecorator {

    @Resource
    private OcItAssetCompanyService ocItAssetCompanyService;

    @Resource
    private OcItAssetTypeService ocItAssetTypeService;

    @Resource
    private OcItAssetNameService ocItAssetNameService;

    @Resource
    private OcItAssetService ocItAssetService;

    @Resource
    private OcItAssetApplyService ocItAssetApplyService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcOrgDepartmentService ocOrgDepartmentService;

    @Resource
    private OcItAssetDisposeService ocItAssetDisposeService;

    public ItAssetVO.Asset decoratorVO(OcItAsset ocItAsset) {
        ItAssetVO.Asset asset = BeanCopierUtils.copyProperties(ocItAsset, ItAssetVO.Asset.class);
        OcItAssetCompany company = ocItAssetCompanyService.queryOcItAssetCompany(ocItAsset.getAssetCompany());
        OcItAssetName ocItAssetName = ocItAssetNameService.queryOcItAssetNameById(ocItAsset.getAssetNameId());
        OcItAssetType ocItAssetType = ocItAssetTypeService.queryOcItAssetTypeById(ocItAssetName.getAssetTypeId());
        asset.setAssetName(ocItAssetName.getAssetName());
        asset.setAssetType(ocItAssetType.getAssetType());
        List<Integer> ids = Lists.newArrayList();
        ids.add(ocItAssetType.getId());
        ids.add(ocItAssetName.getId());
        asset.setAssetNameIds(ids);
        asset.setAssetAddTimestamp(ocItAsset.getAssetAddTime().getTime());
        if (company != null) {
            asset.setAssetCompanyName(company.getAssetCompanyName());
            asset.setAssetCompanyType(company.getAssetCompanyType());
        }
        OcItAssetApply ocItAssetApply = ocItAssetApplyService.queryOcItAssetApplyByAssetId(ocItAsset.getId());
        if (ocItAssetApply != null) {
            OcUser ocUser = ocUserService.queryOcUserById(ocItAssetApply.getUserId());
            asset.setUser(ocUser);
            asset.setDisplayName(ocUser.getDisplayName());
        }
        if (asset.getAssetStatus().equals(4)) {
            OcItAssetDispose ocItAssetDispose = ocItAssetDisposeService.queryOcItAssetDisposeByAssetId(asset.getId());
            if (ocItAssetDispose != null) {
                asset.setDisposeType(ocItAssetDispose.getDisposeType());
                asset.setDisposeRemark(ocItAssetDispose.getRemark());
            }
        }
        return asset;
    }

    public List<ItAssetVO.Asset> decoratorVOList(List<OcItAsset> ocItAssetList) {
        List<ItAssetVO.Asset> assetList = Lists.newArrayListWithCapacity(ocItAssetList.size());
        ocItAssetList.forEach(ocItAsset -> assetList.add(decoratorVO(ocItAsset)));
        return assetList;
    }

    private ItAssetVO.AssetApply decoratorApplyVO(OcItAssetApply ocItAssetApply) {
        ItAssetVO.AssetApply assetApply = BeanCopierUtils.copyProperties(ocItAssetApply, ItAssetVO.AssetApply.class);
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(ocItAssetApply.getAssetId());
        assetApply.setAssetCode(ocItAsset.getAssetCode());
        if (ocItAssetApply.getUserOrgDeptId() != null) {
            OcOrgDepartment orgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(ocItAssetApply.getUserOrgDeptId());
            if (orgDepartment != null)
                assetApply.setUserOrgDeptName(orgDepartment.getName());
        }
        OcUser user = ocUserService.queryOcUserById(ocItAssetApply.getUserId());
        if (user != null) {
            assetApply.setUsername(user.getUsername());
            assetApply.setDisplayName(user.getDisplayName());
        }
        return assetApply;
    }

    public List<ItAssetVO.AssetApply> decoratorApplyVOList(List<OcItAssetApply> ocItAssetApplyList) {
        List<ItAssetVO.AssetApply> assetApplyList = Lists.newArrayListWithCapacity(ocItAssetApplyList.size());
        ocItAssetApplyList.forEach(ocItAssetApply -> assetApplyList.add(decoratorApplyVO(ocItAssetApply)));
        return assetApplyList;
    }

    private ItAssetVO.AssetDispose decoratorDisposeVO(OcItAssetDispose ocItAssetDispose) {
        ItAssetVO.AssetDispose assetApply = BeanCopierUtils.copyProperties(ocItAssetDispose, ItAssetVO.AssetDispose.class);
        OcItAsset ocItAsset = ocItAssetService.queryOcItAssetById(ocItAssetDispose.getAssetId());
        assetApply.setAssetCode(ocItAsset.getAssetCode());
        return assetApply;
    }

    public List<ItAssetVO.AssetDispose> decoratorDisposeVOList(List<OcItAssetDispose> ocItAssetDisposeList) {
        List<ItAssetVO.AssetDispose> assetDisposeList = Lists.newArrayListWithCapacity(ocItAssetDisposeList.size());
        ocItAssetDisposeList.forEach(ocItAssetDispose -> assetDisposeList.add(decoratorDisposeVO(ocItAssetDispose)));
        return assetDisposeList;
    }
}
