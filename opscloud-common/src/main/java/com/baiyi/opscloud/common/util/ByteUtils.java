package com.baiyi.opscloud.common.util;

import lombok.Builder;
import lombok.Getter;

import java.text.DecimalFormat;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/22 10:20 上午
 * @Since 1.0
 */
public class ByteUtils {

    private static final Integer STEP = 1024;
    private static final String[] UNITS = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};

    @Getter
    public enum Unit {
        B(0),
        KB(1),
        MB(2),
        GB(3),
        TB(4),
        PB(5),
        EB(6);
        private int level;

        Unit(int level) {
            this.level = level;
        }
    }

    @Getter
    @Builder
    public static class ByteResult {
        private String size;
        private String unit;

    }

    public static ByteResult byteFormat(long size, Unit unit) {
        if (size <= 0) return null;
        int digitGroups = (int) (Math.log10(size) / Math.log10(STEP));
        while (digitGroups + unit.getLevel() >= UNITS.length) {
            digitGroups--;
        }
        return ByteResult.builder()
                .size(new DecimalFormat("#,##0.#").format(size / Math.pow(STEP, digitGroups)))
                .unit(UNITS[digitGroups + unit.level])
                .build();
    }
}
