package com.sdg.cmdb.domain.server;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnvType implements Serializable{

    private static final long serialVersionUID = 313667218140772942L;

    private int envType;
    private String name;
    private String color;

    public EnvType(){}

    public EnvType(int envType){
        this.envType = envType;
        this.name = EnvTypeEnum.getEnvTypeName(envType);
        this.color = EnvColorEnum.getEnvColorName(envType);
    }

    public enum EnvTypeEnum {
        //0 保留／在组中代表的是所有权限
        keep(0, "default"),
        dev(1, "dev"),
        daily(2, "daily"),
        gray(3, "gray"),
        prod(4, "prod"),
        test(5, "test"),
        back(6, "back"),
        pre(7, "pre");
        private int code;
        private String desc;

        EnvTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvTypeName(int code) {
            for (EnvTypeEnum envTypeEnum : EnvTypeEnum.values()) {
                if (envTypeEnum.getCode() == code) {
                    return envTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public enum EnvColorEnum {

        keep(0, "#777"),
        dev(1, "#5bc0de"),
        daily(2, "#449d44"),
        gray(3, "#ec971f"),
        prod(4, "#d9534f"),
        test(5, "#5e5e5e"),
        back(6, "#286090"),
        pre(7, "#ff5f87");
        private int code;
        private String desc;

        EnvColorEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvColorName(int code) {
            for (EnvColorEnum envColorEnum : EnvColorEnum.values()) {
                if (envColorEnum.getCode() == code) {
                    return envColorEnum.getDesc();
                }
            }
            return "#777";
        }
    }


}
