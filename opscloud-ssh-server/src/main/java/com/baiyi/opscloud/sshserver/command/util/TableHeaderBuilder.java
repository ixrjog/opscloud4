package com.baiyi.opscloud.sshserver.command.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/8 10:40 上午
 * @Version 1.0
 */
public class TableHeaderBuilder {

    private final List<TableHeader> headers = Lists.newArrayList();

    static public TableHeaderBuilder newBuilder() {
        return new TableHeaderBuilder();
    }

    public TableHeaderBuilder addHeader(String name, int length) {
        headers.add(new TableHeader(name, length));
        return this;
    }

    public String build() {
        String headers = Joiner.on("").join(this.headers.stream().map(TableHeader::toName).collect(Collectors.toList()));
        headers = headers.substring(0, headers.length() - 1);
        String divdingLines = Joiner.on("+").join(this.headers.stream().map(TableHeader::toLine).collect(Collectors.toList()));
        return Joiner.on("\n").join(headers, divdingLines);
    }

    @Data
    public static class TableHeader {
        public TableHeader(String name, int length) {
            this.name = name;
            this.length = length;
        }
        private String name;
        private int length;
        public String toName() {
            return String.format(" %-" + length + "s|", name);
        }
        public String toLine() {
            return String.join("", Collections.nCopies(length + 1, "-"));
        }
    }
}
