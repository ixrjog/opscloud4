package com.sdg.cmdb.domain;


public class PublicType {

    public enum CiAppTypeEnum {
        java(0, "java"),
        python(1, "python"),
        ios(2, "ios"),
        android(3, "android"),
        test(4, "test");
        private int code;
        private String desc;

        CiAppTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getCiAppTypeName(int code) {
            for (CiAppTypeEnum ciAppTypeEnum : CiAppTypeEnum.values()) {
                if (ciAppTypeEnum.getCode() == code) {
                    return ciAppTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public enum CiTypeEnum {
        cicd(0, "cicd"),
        cd(1, "cd");
        private int code;
        private String desc;

        CiTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getCiTypeName(int code) {
            for (CiTypeEnum ciTypeEnum : CiTypeEnum.values()) {
                if (ciTypeEnum.getCode() == code) {
                    return ciTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public enum HookEventTypeEnum {
        push(0, "push"),
        tag_push(1, "tag_push"),
        merge_request(2, "merge_request"),
        repository_update(3, "repository_update"),
        project_create(4, "project_create"),
        project_destroy(5, "project_destroy"),
        project_rename(6, "project_rename"),
        project_transfer(7, "project_transfer"),
        project_update(8, "project_update"),
        user_add_to_team(9, "user_add_to_team"),
        user_remove_from_team(10, "user_remove_from_team"),
        user_create(10, "user_create"),
        user_destroy(11, "user_destroy"),
        user_failed_login(12, "user_failed_login"),
        user_rename(13, "user_rename"),
        key_create(14, "key_create"),
        key_destroy(15, "key_destroy"),
        group_create(16, "group_create"),
        group_destroy(17, "group_destroy"),
        group_rename(18, "group_rename"),
        user_add_to_group(19, "user_add_to_group"),
        user_remove_from_group(20, "user_remove_from_group");
        private int code;
        private String desc;

        HookEventTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getHookEventTypeName(int code) {
            for (HookEventTypeEnum hookEventTypeEnum : HookEventTypeEnum.values()) {
                if (hookEventTypeEnum.getCode() == code) {
                    return hookEventTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    /**
     * Android渠道
     */
    public enum CiAndroidChannelTypeEnum {
        yyb("yyb", "腾讯应用宝"),
        taobao("taobao", "豌豆荚"),
        huawei("huawei", "华为"),
        xiaomi("xiaomi", "小米"),
        meizu("meizu", "魅族商店"),
        lianxiang("lianxiang", "联想乐商店"),
        oppo("oppo", "oppo手机"),
        vivo("vivo", "vivo"),
        chuizi("chuizi", "锤子"),
        sanxing("sanxing", "三星"),
        sougou("sougou", "搜狗"),
        qihoo360("qihoo360", "360应用平台"),
        baidu("baidu", "百度"),
        bmhy("bmhy", "斑马会员"),
        hqbs("hqbs", "环球捕手");

        private String code;
        private String desc;

        CiAndroidChannelTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }

        public static String getCiAndroidChannelTypeName(String code) {
            for (CiAndroidChannelTypeEnum ciAndroidChannelTypeEnum : CiAndroidChannelTypeEnum.values()) {
                if (ciAndroidChannelTypeEnum.getCode().equals(code)) {
                    return ciAndroidChannelTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


}
