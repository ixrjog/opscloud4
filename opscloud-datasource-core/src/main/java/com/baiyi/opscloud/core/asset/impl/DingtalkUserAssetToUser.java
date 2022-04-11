package com.baiyi.opscloud.core.asset.impl;

import com.baiyi.opscloud.common.util.EmailUtil;
import com.baiyi.opscloud.core.asset.impl.base.AbstractAssetToBO;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/30 6:23 下午
 * @Version 1.0
 */
@Component
public class DingtalkUserAssetToUser extends AbstractAssetToBO {

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.DINGTALK_USER.name();
    }

    @Override
    protected BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        Pair<String, String> pair = cutString(asset.getName());
        return UserVO.User.builder()
                .username(EmailUtil.toUsername(asset.getAssetKey2()))
                .name(StringUtils.isNotBlank(pair.getLeft()) ? pair.getLeft() : pair.getRight())
                .displayName(StringUtils.isNotBlank(pair.getRight()) ? pair.getRight() : pair.getLeft())
                .email(asset.getAssetKey2())
                .phone(asset.getProperties().get("mobile"))
                .build();
    }

    private Pair<String, String> cutString(String s) {
        List<Character> chineseArray = Lists.newArrayList();
        List<Character> englishArray = Lists.newArrayList();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isChinese(c)) {
                chineseArray.add(c);
            } else {
                englishArray.add(c);
            }
        }
        String chinese = Joiner.on("")
                .join(chineseArray)
                .trim();
        String english = Joiner.on("")
                .join(englishArray)
                .replace("_", " ")
                .replaceAll(" {2,}", " ")
                .trim();
        return new ImmutablePair<>(chinese, english);
    }

    private boolean isChinese(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN)
            return true;
        return false;
    }

    @Override
    public List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.USER);
    }

}
