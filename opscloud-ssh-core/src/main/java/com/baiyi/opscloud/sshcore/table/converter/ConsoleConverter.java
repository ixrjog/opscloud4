package com.baiyi.opscloud.sshcore.table.converter;

import com.baiyi.opscloud.sshcore.table.Bordered;
import com.baiyi.opscloud.sshcore.table.Converter;
import com.baiyi.opscloud.sshcore.table.PrettyTable;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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

        for (int i = 0; i < pt.fieldNames.size(); i++) {
            af(StringUtils.rightPad(pt.fieldNames.get(i), maxWidth[i]));

            if (i < pt.fieldNames.size() - 1) {
                centerBorder();
            } else {
                rightBorder();
            }
        }

        bottomBorderLine(maxWidth);

        // Convert rows to table
        for (Object[] r : pt.rows) {
            ab("\n");
            leftBorder();

            int rL = colLength(String.valueOf(r));
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
                    int width = maxWidth[c];
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

    /*
     * Adjust for max width of the column
     */
    public int[] adjustMaxWidth(PrettyTable pt) {

        // Adjust comma
        List<List<String>> converted = pt.rows.stream()
                .map(r -> Stream.of(r).map(o -> {
                    if (pt.comma && o instanceof Number) {
                        return NumberFormat
                                .getNumberInstance(Locale.US)
                                .format(o);
                    } else {
                        return o.toString();
                    }
                }).collect(Collectors.toList()))
                .collect(Collectors.toList());
        return IntStream.range(0, pt.fieldNames.size())
                .map(i -> {
                    int n = converted.stream()
                            .map(f -> colLength(f.get(i)))
                            .max(Comparator.naturalOrder())
                            .orElse(0);
                    return Math.max(pt.fieldNames.get(i).length(), n);
                }).toArray();
    }

    private int colLength(String colStr) {
        final String searchChar = "[0m";
        int origialLength = colStr.length();
        // "\u001B[0m"
        colStr = colStr.replace(searchChar, "");
        int newLength = colStr.length();

        int count = (origialLength - newLength) / 3;
        int length = origialLength - 8 * count;
        return length;
    }

    private int colorAnisSize(String str) {
        String searchChar = "[0m";
        int origialLength = str.length();
        // "\u001B[0m"
        str = str.replace(searchChar, "");
        int newLength = str.length();

        return (origialLength - newLength) / 3;
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
        for (int i = 0; i < maxWidth.length; i++) {
            sb.append(StringUtils.rightPad("", maxWidth[i] + 2, '-'));
            sb.append("+");
        }
        return sb.toString();
    }
}
