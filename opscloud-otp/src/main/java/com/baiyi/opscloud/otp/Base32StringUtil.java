package com.baiyi.opscloud.otp;

import com.baiyi.opscloud.otp.exception.OtpException;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2022/2/24 7:22 PM
 * @Version 1.0
 */
public class Base32StringUtil {
    // singleton

    /**
     * RFC 4648/3548
     */
    private static final Base32StringUtil INSTANCE = new Base32StringUtil("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567");

    static Base32StringUtil getInstance() {
        return INSTANCE;
    }

    private final char[] DIGITS;
    private final int MASK;
    private final int SHIFT;
    private final HashMap<Character, Integer> CHAR_MAP;

    private static final String SEPARATOR = "-";

    protected Base32StringUtil(String alphabet) {
        // 32 alpha-numeric characters.
        DIGITS = alphabet.toCharArray();
        MASK = DIGITS.length - 1;
        SHIFT = Integer.numberOfTrailingZeros(DIGITS.length);
        CHAR_MAP = Maps.newHashMap();
        IntStream.range(0, DIGITS.length).forEach(i -> CHAR_MAP.put(DIGITS[i], i));
    }

    public static byte[] decode(String encoded) throws OtpException.DecodingException {
        return getInstance().decodeInternal(encoded);
    }

    protected byte[] decodeInternal(String encoded) throws OtpException.DecodingException {
        // Remove whitespace and separators
        encoded = encoded.trim()
                .replaceAll(SEPARATOR, "")
                .replaceAll(" ", "");

        // Remove padding. Note: the padding is used as hint to determine how many
        // bits to decode from the last incomplete chunk (which is commented out
        // below, so this may have been wrong to start with).
        encoded = encoded.replaceFirst("[=]*$", "");

        // Canonicalize to all upper case
        encoded = encoded.toUpperCase(Locale.US);
        if (encoded.isEmpty()) {
            return new byte[0];
        }
        int encodedLength = encoded.length();
        int outLength = encodedLength * SHIFT / 8;
        byte[] result = new byte[outLength];
        int buffer = 0;
        int next = 0;
        int bitsLeft = 0;
        for (char c : encoded.toCharArray()) {
            if (!CHAR_MAP.containsKey(c)) {
                throw new OtpException.DecodingException("Illegal character: " + c);
            }
            buffer <<= SHIFT;
            buffer |= CHAR_MAP.get(c) & MASK;
            bitsLeft += SHIFT;
            if (bitsLeft >= 8) {
                result[next++] = (byte) (buffer >> (bitsLeft - 8));
                bitsLeft -= 8;
            }
        }
        return result;
    }

    public static String encode(byte[] data) {
        return getInstance().encodeInternal(data);
    }

    protected String encodeInternal(byte[] data) {
        if (data.length == 0) {
            return "";
        }

        // SHIFT is the number of bits per output character, so the length of the
        // output is the length of the input multiplied by 8/SHIFT, rounded up.
        if (data.length >= (1 << 28)) {
            // The computation below will fail, so don't do it.
            throw new IllegalArgumentException();
        }

        int outputLength = (data.length * 8 + SHIFT - 1) / SHIFT;
        StringBuilder result = new StringBuilder(outputLength);

        int buffer = data[0];
        int next = 1;
        int bitsLeft = 8;
        while (bitsLeft > 0 || next < data.length) {
            if (bitsLeft < SHIFT) {
                if (next < data.length) {
                    buffer <<= 8;
                    buffer |= (data[next++] & 0xff);
                    bitsLeft += 8;
                } else {
                    int pad = SHIFT - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }
            int index = MASK & (buffer >> (bitsLeft - SHIFT));
            bitsLeft -= SHIFT;
            result.append(DIGITS[index]);
        }
        return result.toString();
    }

    /**
     * enforce that this class is a singleton
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}