package com.baiyi.opscloud.sshcore.table.converter;

import com.baiyi.opscloud.sshcore.table.Bordered;
import com.baiyi.opscloud.sshcore.table.Converter;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class ConsoleConverter implements Converter, Bordered {

    private boolean border;

    public ConsoleConverter border(final boolean border) {
        this.border = border;
        return this;
    }

    abstract ConsoleConverter clear();

    abstract ConsoleConverter af(String text);

    abstract ConsoleConverter ab(String text);

    @Override
    public String convert(PrettyTable pt) {

        clear();

        // Check empty
        if (pt.fieldNames.isEmpty()) {
            return "";
        }

        int[] maxWidth = adjustMaxWidth(pt);

        topBorderLine(maxWidth);

        leftBorder();

        IntStream.range(0, pt.fieldNames.size()).forEach(i -> {
            af(StringUtils.rightPad(pt.fieldNames.get(i), maxWidth[i]));
            if (i < pt.fieldNames.size() - 1) {
                centerBorder();
            } else {
                rightBorder();
            }
        });

        bottomBorderLine(maxWidth);

        // Convert rows to table
        for (Object[] r : pt.rows) {
            ab("\n");
            leftBorder();

            int rL = colLength(Arrays.toString(r));
            // for (int c = 0; c < r.length; c++) {
            for (int c = 0; c < r.length; c++) {

                String nc;
                if (r[c] instanceof Number) {
                    String n = pt.comma
                            ? NumberFormat
                            .getNumberInstance(Locale.US)
                            .format(r[c])
                            : r[c].toString();
                    nc = StringUtils.leftPad(n, maxWidth[c]);
                } else {

                    String colStr = String.valueOf(r[c]);
                    int colorAnisSize = colorAnisSize(colStr);
                    // 修正中文补偿
                    int width = maxWidth[c] - fixLength(colStr);
                    int rLength = colStr.length();
                    if (colorAnisSize == 0) {
                        nc = StringUtils.rightPad(colStr, width);
                    } else {
                        int colL = colLength(colStr);
                        nc = StringUtils.rightPad(colStr, rLength + (width - colL) + colorAnisSize);
                    }
                }

                af(nc);

                if (c < rL - 1) {
                    centerBorder();
                } else {
                    rightBorder();
                }
            }
        }

        bottomBorderLine(maxWidth);

        return toString();
    }

    /**
     * Adjust for max width of the column
     * @param pt
     * @return
     */
    public int[] adjustMaxWidth(PrettyTable pt) {

        // Adjust comma
        List<List<String>> converted = new ArrayList<>();
        for (Object[] r : pt.rows) {
            List<String> collect = Stream.of(r).map(o -> {
                if (pt.comma && o instanceof Number) {
                    return NumberFormat
                            .getNumberInstance(Locale.US)
                            .format(o);
                } else {
                    return o.toString();
                }
            }).collect(Collectors.toList());
            converted.add(collect);
        }
        return IntStream.range(0, pt.fieldNames.size())
                .map(i -> {
                    int n = converted.stream()
                            .map(f ->
                                    colLength(f.get(i)))
                            .max(Comparator.naturalOrder())
                            .orElse(0);
                    return Math.max(pt.fieldNames.get(i).length(), n);
                }).toArray();
    }

    private int colLength(String colStr) {
        final String searchChar = "[0m";
        int originalLength = colStr.length();
        // "\u001B[0m"
        colStr = colStr.replace(searchChar, "");
        int newLength = colStr.length();
        // int newLength = getChineseLength(colStr);
        int count = (originalLength - newLength) / 3;
        int length = originalLength - 8 * count;
        // 补偿中文
        return length + fixLength(colStr);
    }

    private int fixLength(String colStr) {
        int length1 = colStr.length();
        int length2 = getChineseLength(colStr);
        return length2 - length1;
    }

    private int colorAnisSize(String str) {
        String searchChar = "[0m";
        int originalLength = str.length();
        // "\u001B[0m"
        str = str.replace(searchChar, "");
        int newLength = str.length();

        return (originalLength - newLength) / 3;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param validateStr 指定的字符串
     * @return 字符串的长度
     */
    public static int getChineseLength(String validateStr) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < validateStr.length(); i++) {
            /* 获取一个字符 */
            String temp = validateStr.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private ConsoleConverter topBorderLine(final int[] maxWidth) {
        ab(border ? line(maxWidth) + "\n" : "");
        return this;
    }

    private ConsoleConverter bottomBorderLine(final int[] maxWidth) {
        ab(border ? "\n" + line(maxWidth) : "");
        return this;
    }

    private ConsoleConverter leftBorder() {
        ab(border ? "| " : "");
        return this;
    }

    private ConsoleConverter rightBorder() {
        ab(border ? " |" : "");
        return this;
    }

    private ConsoleConverter centerBorder() {
        ab(border ? " | " : " ");
        return this;
    }

    private static String line(final int[] maxWidth) {

        final StringBuilder sb = new StringBuilder();

        sb.append("+");
        int i = 0;
        while (i < maxWidth.length) {
            sb.append(StringUtils.rightPad("", maxWidth[i] + 2, '-'));
            sb.append("+");
            i++;
        }
        return sb.toString();
    }

}