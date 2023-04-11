package org.gitlab4j.api.utils;

import java.util.Arrays;

/**
 * This class implements a CharSequence that can be cleared of it's contained characters.
 * This class is utilized to pass around secrets (passwords) instead of a String instance.
 */
public class SecretString implements CharSequence, AutoCloseable {

    private final char[] chars;

    public SecretString(CharSequence charSequence) {

        int length = charSequence.length();
        chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = charSequence.charAt(i);
        }
    }

    public SecretString(char[] chars) {
        this(chars, 0, chars.length);
    }

    public SecretString(char[] chars, int start, int end) {
        this.chars = new char[end - start];
        System.arraycopy(chars, start, this.chars, 0, this.chars.length);
    }

    @Override
    public char charAt(int index) {
        return chars[index];
    }

    @Override
    public void close() {
        clear();
    }

    @Override
    public int length() {
        return chars.length;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new SecretString(this.chars, start, end);
    }

    /**
     * Clear the contents of this SecretString instance by setting each character to 0.
     * This is automatically done in the finalize() method.
     */
    public void clear() {
        Arrays.fill(chars, '\0');
    }

    @Override
    public void finalize() throws Throwable {
        clear();
        super.finalize();
    }
}