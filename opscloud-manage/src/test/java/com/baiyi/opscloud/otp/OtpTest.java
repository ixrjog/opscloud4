package com.baiyi.opscloud.otp;

import com.baiyi.opscloud.BaseUnit;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

/**
 * @Author baiyi
 * @Date 2022/2/24 5:46 PM
 * @Version 1.0
 */
@Slf4j
public class OtpTest extends BaseUnit {

    // otpauth://totp/[客户端显示的账户信息]?secret=[secretBase32]

    // otpauth://totp/[客户端显示的账户信息]?secret=[secretBase32]

    @Test
    void test() {
        try {
            // 60S
            final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(60L));

            final Key key;
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());

            // Key length should match the length of the HMAC output (160 bits for SHA-1, 256 bits
            // for SHA-256, and 512 bits for SHA-512). Note that while Mac#getMacLength() returns a
            // length in _bytes,_ KeyGenerator#init(int) takes a key length in _bits._
            final int macLengthInBytes = Mac.getInstance(totp.getAlgorithm()).getMacLength();


            print("macLengthInBytes=" + macLengthInBytes * 8);
            keyGenerator.init(macLengthInBytes * 8);
            // LACYXBI6WBQ4O5WW273USF5S7QMVMYBT
            key = keyGenerator.generateKey();

            log.info("Algorithm={}, Encoded={}, Format={}", key.getAlgorithm(), Base32StringUtil.encode(key.getEncoded()), key.getFormat());

//            print("getAlgorithm() = " ,  key.getAlgorithm(),key.getEncoded(),key.getFormat());
//            print("Encoded = " + key.getEncoded());
//            print("Format = " + key.getFormat());

            final Instant now = Instant.now();
            final Instant before = now.plus(totp.getTimeStep());
            final Instant later = now.plus(totp.getTimeStep());
            System.out.println("Current password: " + totp.generateOneTimePasswordString(key, now));
            System.out.println("Future password:  " + totp.generateOneTimePasswordString(key, later));
        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeyException e2) {

        }
    }

    /**
     * Current password: 862708
     */
    @Test
    void test2() {
        try {
            Key sk = OtpUtil.generateOtpSK();
            print(Base32StringUtil.encode(sk.getEncoded()));
            while (true) {
                // SecretKey sk = OtpUtil.toKey("LACYXBI6WBQ4O5WW273USF5S7QMVMYBT");
                log.info(OtpUtil.generateOtp(sk));
                Thread.sleep(2000);
            }
        } catch (Exception e) {
        }
    }

    @Test
    void test3() {
        try {
            Key sk = OtpUtil.toKey("");
            print(Base32StringUtil.encode(sk.getEncoded()));
            while (true) {
                // SecretKey sk = OtpUtil.toKey("LACYXBI6WBQ4O5WW273USF5S7QMVMYBT");
                log.info(OtpUtil.generateOtp(sk));
                Thread.sleep(2000);
            }
        } catch (Exception e) {
        }
    }

}
