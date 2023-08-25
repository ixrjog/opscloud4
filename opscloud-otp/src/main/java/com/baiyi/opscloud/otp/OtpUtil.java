package com.baiyi.opscloud.otp;

import com.baiyi.opscloud.otp.exception.OtpException;
import com.baiyi.opscloud.otp.model.OTPAccessCode;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.slf4j.helpers.MessageFormatter;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

/**
 * OTP 工具类
 *
 * @Author baiyi
 * @Date 2022/2/25 9:31 AM
 * @Version 1.0
 */
public class OtpUtil {

    private OtpUtil() {
    }

    private static final Duration DURATION = Duration.ofSeconds(30L);

    /**
     * otpauth://totp/OPSCLOUD@${ACCOUNT}?secret=${OTP_SK}?&issuer=OPSCLOUD
     */
    private static final String QR_CODE = "otpauth://totp/OPSCLOUD@{}?secret={}";

    /**
     * 构建一个OTP SecretKey
     * 30s + SHA1(160bit)
     *
     * @return OXUX4SRUEYZ3FCS4N3YSII23DISP3DI6
     * @throws NoSuchAlgorithmException
     */
    public static Key generateOtpSK() throws NoSuchAlgorithmException {
        /*
         * 30S
         */
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(DURATION);
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
        /*
         *  Key length should match the length of the HMAC output (160 bits for SHA-1, 256 bits
         *  for SHA-256, and 512 bits for SHA-512). Note that while Mac#getMacLength() returns a
         *  length in _bytes,_ KeyGenerator#init(int) takes a key length in _bits._
         */
        final int macLengthInBytes = Mac.getInstance(totp.getAlgorithm()).getMacLength();
        keyGenerator.init(macLengthInBytes * 8);
        return keyGenerator.generateKey();
    }

    public static String generateOtp(Key key) throws NoSuchAlgorithmException, InvalidKeyException {
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(DURATION);
        final Instant now = Instant.now();
        return totp.generateOneTimePasswordString(key, now);
    }

    /**
     * 绑定MFA专用，生成2组AccessCode
     *
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static OTPAccessCode.AccessCode generateOtpAccessCode(Key key) throws NoSuchAlgorithmException, InvalidKeyException {
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(DURATION);
        final Instant now = Instant.now();
        final Instant later = now.plus(totp.getTimeStep());
        return OTPAccessCode.AccessCode.builder()
                .currentPassword(totp.generateOneTimePasswordString(key, now))
                .futurePassword(totp.generateOneTimePasswordString(key, later))
                .build();
    }

    /**
     * 转换Key
     *
     * @param otpSecretKeyStr
     * @return
     * @throws OtpException.DecodingException
     */
    public static SecretKey toKey(String otpSecretKeyStr) throws OtpException.DecodingException {
        return new SecretKeySpec(Base32StringUtil.decode(otpSecretKeyStr), "HmacSHA1");
    }

    public static SecretKey toKey(byte[] var1) throws OtpException.DecodingException {
        return new SecretKeySpec(var1, "HmacSHA1");
    }

    /**
     * 转换二维码
     *
     * @param account
     * @param otpSk
     * @return otpauth://totp/客户端显示的账户信息?secret=secretBase32
     */
    public static String toQRCode(String account, String otpSk) {
        return MessageFormatter.format(QR_CODE, account, otpSk).getMessage();
    }

}
