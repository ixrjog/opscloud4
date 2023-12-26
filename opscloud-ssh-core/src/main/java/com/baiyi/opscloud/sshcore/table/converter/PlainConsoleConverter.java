package com.baiyi.opscloud.sshcore.table.converter;

public class PlainConsoleConverter extends ConsoleConverter {

    private final StringBuilder sb = new StringBuilder();

    @Override
    ConsoleConverter clear() {
        sb.setLength(0); // set length of buffer to 0
        sb.trimToSize(); // trim the underlying buffer
        return this;
    }

    @Override
    ConsoleConverter af(String text) {
        sb.append(text);
        return this;
    }

    @Override
    ConsoleConverter ab(String text) {
        sb.append(text);
        return null;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}