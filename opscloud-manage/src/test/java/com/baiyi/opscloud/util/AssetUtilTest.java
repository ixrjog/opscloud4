package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/17 11:26 上午
 * @Version 1.0
 */
public class AssetUtilTest extends BaseUnit {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Test
    void dd() {
        System.out.println(AssetUtil.equals(null, "A"));
        System.out.println(AssetUtil.equals("A", null));

        System.out.println(AssetUtil.equals("B", "B"));
    }

    @Test
    void toDate() {
        // 2021-08-27T03:14:04Z

        String time = "2021-08-27T03:14:04Z";

        //    String UTC = "yyyy-MM-dd'T'HH:mm'Z'";

        Date date = TimeUtil.toDate(time, TimeZoneEnum.UTC);

        String d = NewTimeUtil.parse(date);
        System.err.println(d);
    }


    // Emmanuel A  Imoh
    @Test
    void xx() {
        List<DatasourceInstanceAsset> assetList =
                dsInstanceAssetService.listByInstanceAssetType("e9f2acfe1d2945dd91262ba49df26984", "DINGTALK_USER");
        assetList.forEach(e -> cutString(e.getName()));
//        cutString("Emmanuel A. Imoh");
    }

    private void cutString(String s) {
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
        Pair<String, String> pair = new ImmutablePair<>(chinese, english);
        print(pair.getLeft() + "----" + pair.getRight());
    }

    private boolean isChinese(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN)
            return true;
        return false;
    }
}
