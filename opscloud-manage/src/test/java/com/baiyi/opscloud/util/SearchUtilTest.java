package com.baiyi.opscloud.util;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.MatchingUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2023/12/21 10:23
 * @Version 1.0
 */
public class SearchUtilTest extends BaseUnit {

    @Test
    void test() {
        print("Unit 1: true");
        print(MatchingUtil.fuzzyMatching("AAAAa", "AAAAa"));

        print("Unit 2: false");
        print(MatchingUtil.fuzzyMatching("AAAAa", "AAAAb"));

        print("Unit 3: true");
        print(MatchingUtil.fuzzyMatching("AAAAaCDEF", "*aCDEF"));

        print("Unit 4: false");
        print(MatchingUtil.fuzzyMatching("AAAAaCDEF", "*aCADEF"));

        print("Unit 5: true");
        print(MatchingUtil.fuzzyMatching("AAAAaCDEF", "AAAAaC*"));

        print("Unit 6: false");
        print(MatchingUtil.fuzzyMatching("AAAAaCDEF", "AAAAaC1*"));

    }

}