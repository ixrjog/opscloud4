package com.baiyi.opscloud.common.util;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2022/3/28 09:33
 * @Version 1.0
 */

public class IPAddressUtil {

    private static final int INADDR4SZ = 4;
    private static final int INADDR16SZ = 16;
    private static final int INT16SZ = 2;
    private static final long L_IPV6_DELIMS = 0L;
    private static final long H_IPV6_DELIMS = 671088640L;
    private static final long L_GEN_DELIMS = -8935000888854970368L;
    private static final long H_GEN_DELIMS = 671088641L;
    private static final long L_AUTH_DELIMS = 288230376151711744L;
    private static final long H_AUTH_DELIMS = 671088641L;
    private static final long L_COLON = 288230376151711744L;
    private static final long H_COLON = 0L;
    private static final long L_SLASH = 140737488355328L;
    private static final long H_SLASH = 0L;
    private static final long L_BACKSLASH = 0L;
    private static final long H_BACKSLASH = 268435456L;
    private static final long L_NON_PRINTABLE = 4294967295L;
    private static final long H_NON_PRINTABLE = -9223372036854775808L;
    private static final long L_EXCLUDE = -8935000884560003073L;
    private static final long H_EXCLUDE = -9223372035915251711L;
    private static final char[] OTHERS = new char[]{'⁇', '⁈', '⁉', '℀', '℁', '℅', '℆', '⩴', '﹕', '﹖', '﹟', '﹫', '＃', '／', '：', '？', '＠'};

    public IPAddressUtil() {
    }

    public static byte[] textToNumericFormatV4(String var0) {
        byte[] var1 = new byte[4];
        long var2 = 0L;
        int var4 = 0;
        boolean var5 = true;
        int var6 = var0.length();
        if (var6 != 0 && var6 <= 15) {
            for (int var7 = 0; var7 < var6; ++var7) {
                char var8 = var0.charAt(var7);
                if (var8 == '.') {
                    if (var5 || var2 < 0L || var2 > 255L || var4 == 3) {
                        return null;
                    }

                    var1[var4++] = (byte) ((int) (var2 & 255L));
                    var2 = 0L;
                    var5 = true;
                } else {
                    int var9 = Character.digit(var8, 10);
                    if (var9 < 0) {
                        return null;
                    }

                    var2 *= 10L;
                    var2 += (long) var9;
                    var5 = false;
                }
            }

            if (!var5 && var2 >= 0L && var2 < 1L << (4 - var4) * 8) {
                switch (var4) {
                    case 0:
                        var1[0] = (byte) ((int) (var2 >> 24 & 255L));
                    case 1:
                        var1[1] = (byte) ((int) (var2 >> 16 & 255L));
                    case 2:
                        var1[2] = (byte) ((int) (var2 >> 8 & 255L));
                    case 3:
                        var1[3] = (byte) ((int) (var2 & 255L));
                    default:
                        return var1;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static byte[] textToNumericFormatV6(String var0) {
        if (var0.length() < 2) {
            return null;
        } else {
            char[] var5 = var0.toCharArray();
            byte[] var6 = new byte[16];
            int var7 = var5.length;
            int var8 = var0.indexOf("%");
            if (var8 == var7 - 1) {
                return null;
            } else {
                if (var8 != -1) {
                    var7 = var8;
                }

                int var1 = -1;
                int var9 = 0;
                int var10 = 0;
                if (var5[var9] == ':') {
                    ++var9;
                    if (var5[var9] != ':') {
                        return null;
                    }
                }

                int var11 = var9;
                boolean var3 = false;
                int var4 = 0;

                while (true) {
                    int var12;
                    while (var9 < var7) {
                        char var2 = var5[var9++];
                        var12 = Character.digit(var2, 16);
                        if (var12 != -1) {
                            var4 <<= 4;
                            var4 |= var12;
                            if (var4 > 65535) {
                                return null;
                            }

                            var3 = true;
                        } else {
                            if (var2 != ':') {
                                if (var2 == '.' && var10 + 4 <= 16) {
                                    String var13 = var0.substring(var11, var7);
                                    int var14 = 0;

                                    for (int var15 = 0; (var15 = var13.indexOf(46, var15)) != -1; ++var15) {
                                        ++var14;
                                    }

                                    if (var14 != 3) {
                                        return null;
                                    }

                                    byte[] var16 = textToNumericFormatV4(var13);
                                    if (var16 == null) {
                                        return null;
                                    }

                                    for (int var17 = 0; var17 < 4; ++var17) {
                                        var6[var10++] = var16[var17];
                                    }

                                    var3 = false;
                                    break;
                                }

                                return null;
                            }

                            var11 = var9;
                            if (!var3) {
                                if (var1 != -1) {
                                    return null;
                                }

                                var1 = var10;
                            } else {
                                if (var9 == var7) {
                                    return null;
                                }

                                if (var10 + 2 > 16) {
                                    return null;
                                }

                                var6[var10++] = (byte) (var4 >> 8 & 255);
                                var6[var10++] = (byte) (var4 & 255);
                                var3 = false;
                                var4 = 0;
                            }
                        }
                    }

                    if (var3) {
                        if (var10 + 2 > 16) {
                            return null;
                        }

                        var6[var10++] = (byte) (var4 >> 8 & 255);
                        var6[var10++] = (byte) (var4 & 255);
                    }

                    if (var1 != -1) {
                        var12 = var10 - var1;
                        if (var10 == 16) {
                            return null;
                        }

                        for (var9 = 1; var9 <= var12; ++var9) {
                            var6[16 - var9] = var6[var1 + var12 - var9];
                            var6[var1 + var12 - var9] = 0;
                        }

                        var10 = 16;
                    }

                    if (var10 != 16) {
                        return null;
                    }

                    byte[] var18 = convertFromIPv4MappedAddress(var6);
                    return Objects.requireNonNullElse(var18, var6);

                }
            }
        }
    }

    public static boolean isIPv4LiteralAddress(String var0) {
        return textToNumericFormatV4(var0) != null;
    }

    public static boolean isIPv6LiteralAddress(String var0) {
        return textToNumericFormatV6(var0) != null;
    }

    public static byte[] convertFromIPv4MappedAddress(byte[] var0) {
        if (isIPv4MappedAddress(var0)) {
            byte[] var1 = new byte[4];
            System.arraycopy(var0, 12, var1, 0, 4);
            return var1;
        } else {
            return null;
        }
    }

    private static boolean isIPv4MappedAddress(byte[] var0) {
        if (var0.length < 16) {
            return false;
        } else {
            return var0[0] == 0 && var0[1] == 0 && var0[2] == 0 && var0[3] == 0 && var0[4] == 0 && var0[5] == 0 && var0[6] == 0 && var0[7] == 0 && var0[8] == 0 && var0[9] == 0 && var0[10] == -1 && var0[11] == -1;
        }
    }

    public static boolean match(char var0, long var1, long var3) {
        if (var0 < '@') {
            return (1L << var0 & var1) != 0L;
        } else if (var0 < 128) {
            return (1L << var0 - 64 & var3) != 0L;
        } else {
            return false;
        }
    }

    public static int scan(String var0, long var1, long var3) {
        int var5 = -1;
        int var6;
        if (var0 != null && (var6 = var0.length()) != 0) {
            boolean var7 = false;

            do {
                ++var5;
            } while (var5 < var6 && !(var7 = match(var0.charAt(var5), var1, var3)));

            return var7 ? var5 : -1;
        } else {
            return -1;
        }
    }

    public static int scan(String var0, long var1, long var3, char[] var5) {
        int var6 = -1;
        int var7;
        if (var0 != null && (var7 = var0.length()) != 0) {
            boolean var8 = false;
            char var10 = var5[0];

            while (true) {
                ++var6;
                char var9;
                if (var6 >= var7 || (var8 = match(var9 = var0.charAt(var6), var1, var3))) {
                    break;
                }

                if (var9 >= var10 && Arrays.binarySearch(var5, var9) > -1) {
                    var8 = true;
                    break;
                }
            }

            return var8 ? var6 : -1;
        } else {
            return -1;
        }
    }

    private static String describeChar(char var0) {
        if (var0 >= ' ' && var0 != 127) {
            return var0 == '\\' ? "'\\'" : "'" + var0 + "'";
        } else if (var0 == '\n') {
            return "LF";
        } else {
            return var0 == '\r' ? "CR" : "control char (code=" + var0 + ")";
        }
    }

    private static String checkUserInfo(String var0) {
        int var1 = scan(var0, -9223231260711714817L, -9223372035915251711L);
        return var1 >= 0 ? "Illegal character found in user-info: " + describeChar(var0.charAt(var1)) : null;
    }

    private static String checkHost(String var0) {
        int var1;
        if (var0.startsWith("[") && var0.endsWith("]")) {
            var0 = var0.substring(1, var0.length() - 1);
            if (isIPv6LiteralAddress(var0)) {
                var1 = var0.indexOf(37);
                if (var1 >= 0) {
                    var1 = scan(var0 = var0.substring(var1), 4294967295L, -9223372036183687168L);
                    if (var1 >= 0) {
                        return "Illegal character found in IPv6 scoped address: " + describeChar(var0.charAt(var1));
                    }
                }

                return null;
            } else {
                return "Unrecognized IPv6 address format";
            }
        } else {
            var1 = scan(var0, -8935000884560003073L, -9223372035915251711L);
            return var1 >= 0 ? "Illegal character found in host: " + describeChar(var0.charAt(var1)) : null;
        }
    }

    private static String checkAuth(String var0) {
        int var1 = scan(var0, -9223231260711714817L, -9223372036586340352L);
        return var1 >= 0 ? "Illegal character found in authority: " + describeChar(var0.charAt(var1)) : null;
    }

    public static String checkAuthority(URL var0) {
        if (var0 == null) {
            return null;
        } else {
            String var1;
            String var2;
            if ((var1 = checkUserInfo(var2 = var0.getUserInfo())) != null) {
                return var1;
            } else {
                String var3;
                if ((var1 = checkHost(var3 = var0.getHost())) != null) {
                    return var1;
                } else {
                    return var3 == null && var2 == null ? checkAuth(var0.getAuthority()) : null;
                }
            }
        }
    }

    public static String checkExternalForm(URL var0) {
        if (var0 == null) {
            return null;
        } else {
            String var1;
            int var2 = scan(var1 = var0.getUserInfo(), 140741783322623L, -9223372036854775808L);
            if (var2 >= 0) {
                return "Illegal character found in authority: " + describeChar(var1.charAt(var2));
            } else {
                return (var1 = checkHostString(var0.getHost())) != null ? var1 : null;
            }
        }
    }

    public static String checkHostString(String var0) {
        if (var0 == null) {
            return null;
        } else {
            int var1 = scan(var0, 140741783322623L, -9223372036854775808L, OTHERS);
            return var1 >= 0 ? "Illegal character found in host: " + describeChar(var0.charAt(var1)) : null;
        }
    }

}