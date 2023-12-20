package com.baiyi.opscloud.sshcore.table.converter;

import com.baiyi.opscloud.sshcore.table.Colored;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.valueOf;

public class ColoredConsoleConverter extends ConsoleConverter implements Colored {

    private final Ansi ansi = new Ansi();

    private Ansi.Color fontColor = Ansi.Color.DEFAULT;

    private Ansi.Color borderColor = Ansi.Color.DEFAULT;

    @Override
    public ColoredConsoleConverter fontColor(final String colorName) {
        return fontColor(valueOf(colorName));
    }

    private ColoredConsoleConverter fontColor(final Ansi.Color font) {
        this.fontColor = font;
        return this;
    }

    @Override
    public ColoredConsoleConverter borderColor(String colorName) {
        return borderColor(valueOf(colorName));
    }

    public ColoredConsoleConverter borderColor(final Ansi.Color border) {
        this.borderColor = border;
        return this;
    }

    @Override
    ConsoleConverter clear() {
        ansi.reset();
        return this;
    }

    @Override
    ConsoleConverter af(String text) {
        ansi.fg(fontColor);
        ansi.a(text);
        ansi.reset();
        return this;
    }

    @Override
    ConsoleConverter ab(String text) {
        ansi.fg(borderColor);
        ansi.a(text);
        ansi.reset();
        return this;
    }

    @Override
    public String toString() {
        return ansi.toString();
    }

}