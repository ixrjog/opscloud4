package com.sdg.cmdb.template.format;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 16/10/11.
 */
public class BashLine {
    public final static int bash_format_list = 1;
    public final static int bash_format_value = 0;

    private int type = this.bash_format_value;
    private List<BashLine> bashLines = new ArrayList<BashLine>();


    public void put(BashLine line) {
        if (line == null) return;
        if (this.bashLines == null) {
            this.bashLines = new ArrayList<BashLine>();
        }
        this.bashLines.add(line);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    private String key;
    private String value;

    public static BashLine builder(String key, String value, int type) {
        if (key == null) return null;
        if (value == null) return null;
        return new BashLine(key, value, type);
    }

    public static BashLine builder(String key, String value) {
        if (key == null) return null;
        if (value == null) return null;
        return new BashLine(key, value);
    }


    public  BashLine() {
    }


    public BashLine(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public BashLine(String key, String value, int type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }


    public String toLine() {
        String str;
        if (this.getType() == 0) {
            str = this.key + "='" + this.value + "' \n\n";
        } else {
            str = this.key + "=( '" + this.value + "' ) \n\n";
        }
        return str;
    }

    public String getLines() {
        StringBuffer buffer = new StringBuffer();
        for (BashLine bl : this.bashLines) {
            buffer.append(bl.toLine());
        }
        return buffer.toString();
    }



}
