package com.sdg.cmdb.util;

/**
 * Content IP工具类.
 * User: chenxinghu
 * Email: chenxinghu@51xianqu.net
 * Date: 16/4/18
 * Time: 11:16
 */

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class IPUtils {
    /**
     * 把long类型的Ip转为一般Ip类型：xx.xx.xx.xx
     *
     * @param ip
     * @return
     */
    public static String getIpFromLong(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * 把xx.xx.xx.xx类型的转为long类型的
     *
     * @param ip
     * @return
     */
    public static Long getIpFromString(String ip) {
        Long ipLong = 0L;
        String ipTemp = ip;
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp);
        return ipLong;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param maskBit 掩码位数，如"28"、"30"
     * @return
     */
    public static String getMaskByMaskBit(String maskBit) {
        return StringUtils.isEmpty(maskBit) ? "error, maskBit is null !"
                : maskBitMap().get(maskBit);
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的字符串表示
     */
    public static String getBeginIpStr(String ip, String maskBit) {
        return getIpFromLong(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的长整型表示
     */
    public static Long getBeginIpLong(String ip, String maskBit) {
        return getIpFromString(ip) & getIpFromString(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的字符串表示
     */
    public static String getEndIpStr(String ip, String maskBit) {
        return getIpFromLong(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的长整型表示
     */
    public static Long getEndIpLong(String ip, String maskBit) {
        return getBeginIpLong(ip, maskBit)
                + ~getIpFromString(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据子网掩码转换为掩码位 如 255.255.255.252转换为掩码位 为 30
     *
     * @param netmarks
     * @return
     */
    public static int getNetMask(String netmarks) {
        StringBuffer sbf;
        String str;
        int inetmask = 0, count = 0;
        String[] ipList = netmarks.split("\\.");
        for (int n = 0; n < ipList.length; n++) {
            sbf = toBin(Integer.parseInt(ipList[n]));
            str = sbf.reverse().toString();
            count = 0;
            for (int i = 0; i < str.length(); i++) {
                i = str.indexOf('1', i);
                if (i == -1) {
                    break;
                }
                count++;
            }
            inetmask += count;
        }
        return inetmask;
    }

    /**
     * 计算子网大小
     *
     * @param maskBit 掩码位
     * @return
     */
    public static int getPoolMax(int maskBit) {
        if (maskBit <= 0 || maskBit >= 32) {
            return 0;
        }
        return (int) Math.pow(2, 32 - maskBit) - 2;
    }

    private static StringBuffer toBin(int x) {
        StringBuffer result = new StringBuffer();
        result.append(x % 2);
        x /= 2;
        while (x > 0) {
            result.append(x % 2);
            x /= 2;
        }
        return result;
    }

    /*
     * 存储着所有的掩码位及对应的掩码 key:掩码位 value:掩码（x.x.x.x）
    */
    private static Map<String, String> maskBitMap() {
        Map<String, String> maskBit = new HashMap<String, String>();
        maskBit.put("0", "0.0.0.0");
        maskBit.put("1", "128.0.0.0");
        maskBit.put("2", "192.0.0.0");
        maskBit.put("3", "224.0.0.0");
        maskBit.put("4", "240.0.0.0");
        maskBit.put("5", "248.0.0.0");
        maskBit.put("6", "252.0.0.0");
        maskBit.put("7", "254.0.0.0");
        maskBit.put("8", "255.0.0.0");
        maskBit.put("9", "255.128.0.0");
        maskBit.put("10", "255.192.0.0");
        maskBit.put("11", "255.224.0.0");
        maskBit.put("12", "255.240.0.0");
        maskBit.put("13", "255.248.0.0");
        maskBit.put("14", "255.252.0.0");
        maskBit.put("15", "255.254.0.0");
        maskBit.put("16", "255.255.0.0");
        maskBit.put("17", "255.255.128.0");
        maskBit.put("18", "255.255.192.0");
        maskBit.put("19", "255.255.224.0");
        maskBit.put("20", "255.255.240.0");
        maskBit.put("21", "255.255.248.0");
        maskBit.put("22", "255.255.252.0");
        maskBit.put("23", "255.255.254.0");
        maskBit.put("24", "255.255.255.0");
        maskBit.put("25", "255.255.255.128");
        maskBit.put("26", "255.255.255.192");
        maskBit.put("27", "255.255.255.224");
        maskBit.put("28", "255.255.255.240");
        maskBit.put("29", "255.255.255.248");
        maskBit.put("30", "255.255.255.252");
        maskBit.put("31", "255.255.255.254");
        maskBit.put("32", "255.255.255.255");
        return maskBit;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param masks
     * @return
     */
    @Deprecated
    public static String getMaskByMaskBit(int masks) {
        String ret = "";
        if (masks == 1)
            ret = "128.0.0.0";
        else if (masks == 2)
            ret = "192.0.0.0";
        else if (masks == 3)
            ret = "224.0.0.0";
        else if (masks == 4)
            ret = "240.0.0.0";
        else if (masks == 5)
            ret = "248.0.0.0";
        else if (masks == 6)
            ret = "252.0.0.0";
        else if (masks == 7)
            ret = "254.0.0.0";
        else if (masks == 8)
            ret = "255.0.0.0";
        else if (masks == 9)
            ret = "255.128.0.0";
        else if (masks == 10)
            ret = "255.192.0.0";
        else if (masks == 11)
            ret = "255.224.0.0";
        else if (masks == 12)
            ret = "255.240.0.0";
        else if (masks == 13)
            ret = "255.248.0.0";
        else if (masks == 14)
            ret = "255.252.0.0";
        else if (masks == 15)
            ret = "255.254.0.0";
        else if (masks == 16)
            ret = "255.255.0.0";
        else if (masks == 17)
            ret = "255.255.128.0";
        else if (masks == 18)
            ret = "255.255.192.0";
        else if (masks == 19)
            ret = "255.255.224.0";
        else if (masks == 20)
            ret = "255.255.240.0";
        else if (masks == 21)
            ret = "255.255.248.0";
        else if (masks == 22)
            ret = "255.255.252.0";
        else if (masks == 23)
            ret = "255.255.254.0";
        else if (masks == 24)
            ret = "255.255.255.0";
        else if (masks == 25)
            ret = "255.255.255.128";
        else if (masks == 26)
            ret = "255.255.255.192";
        else if (masks == 27)
            ret = "255.255.255.224";
        else if (masks == 28)
            ret = "255.255.255.240";
        else if (masks == 29)
            ret = "255.255.255.248";
        else if (masks == 30)
            ret = "255.255.255.252";
        else if (masks == 31)
            ret = "255.255.255.254";
        else if (masks == 32)
            ret = "255.255.255.255";
        return ret;
    }

    /** 校验Ip格式 这是模仿js校验ip格式，使用java做的判断
     * @param ip
     * @return
     */
    public static boolean checkIp(String ip)
    {
        String[] ipValue = ip.split("\\.");
        if (ipValue.length != 4) {return false;}
        for (int i = 0; i < ipValue.length; i++)
        {
            String temp = ipValue[i];
            try
            {
                // java判断字串是否整数可以用此类型转换异常捕获方法，也可以用正则 var regu = /^\d+$/;
                Integer q = Integer.valueOf(ipValue[i]);
                if (q > 255)
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return true;
    }

}
