package com.baiyi.opscloud.sshcore.table;

import com.baiyi.opscloud.sshcore.table.converter.PlainConsoleConverter;
import com.baiyi.opscloud.sshcore.table.parser.JsonParser;

import java.io.IOException;
import java.util.*;

/**
 * PrettyTable class.
 */
public class PrettyTable {

    public List<String> fieldNames = new ArrayList<>();

    public List<Object[]> rows = new ArrayList<>();

    public boolean comma = false;

    public boolean border = true;

    public boolean color = false;

    public String fontColor = "DEFAULT";

    public String borderColor = "DEFAULT";

    private Parser parser = new JsonParser();

    private Converter converter = new PlainConsoleConverter();

    /**
     * @param fieldNames
     * @return
     */
    public static PrettyTable fieldNames(final String... fieldNames) {
        PrettyTable pt = new PrettyTable();
        pt.fieldNames.addAll(Arrays.asList(fieldNames));
        return pt;
    }

    public static PrettyTable fieldNames(final Iterator<String> fieldNames) {
        PrettyTable pt = new PrettyTable();
        fieldNames.forEachRemaining(pt.fieldNames::add);
        return pt;
    }

    public PrettyTable addRow(final Object... row) {
        this.rows.add(row);
        return this;
    }

    public PrettyTable deleteRow(final int num) {
        this.rows.remove(num - 1);
        return this;
    }

    public PrettyTable addField(final String fieldName) {
        this.fieldNames.add(fieldName);
        return this;
    }

    public PrettyTable comma(final boolean comma) {
        this.comma = comma;
        return this;
    }

    public PrettyTable border(final boolean border) {
        this.border = border;
        return this;
    }

    public PrettyTable color(final boolean color) {
        this.color = color;
        return this;
    }

    public PrettyTable fontColor(final String colorName) {
        this.fontColor = colorName;
        return this;
    }

    public PrettyTable borderColor(final String colorName) {
        this.borderColor = colorName;
        return this;
    }

    public static PrettyTable parser(final Parser parser) {
        PrettyTable pt = new PrettyTable();
        pt.parser = parser;
        return pt;
    }

    public PrettyTable fromString(String text) throws IOException {
        return parser.parse(text);
    }

    public PrettyTable converter(final Converter converter) {
        this.converter = converter;
        return this;
    }

    public PrettyTable sortTable(final String fieldName) {
        return sortTable(fieldName, false);
    }

    public PrettyTable sortTable(final String fieldName, final boolean reverse) {
        int idx = Collections.binarySearch(fieldNames, fieldName);
        rows.sort((o1, o2) -> {
            if (o1[idx] instanceof Comparable && o2[idx] instanceof Comparable) {
                int c = ((Comparable) o1[idx]).compareTo(o2[idx]);
                return c * (reverse ? -1 : 1);
            }
            return 0;
        });

        return this;
    }

    public PrettyTable clearTable() {
        rows.clear();
        return this;
    }

    public PrettyTable deleteTable() {
        rows.clear();
        fieldNames.clear();
        return this;
    }

    @Override
    public String toString() {
        if (converter instanceof Bordered) {
            ((Bordered) converter).border(border);
        }
        if (converter instanceof Colored) {
            ((Colored) converter)
                    .fontColor(fontColor)
                    .borderColor(borderColor);
        }
        return converter.convert(this);
    }

}