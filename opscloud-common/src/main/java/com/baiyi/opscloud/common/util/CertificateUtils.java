package com.baiyi.opscloud.common.util;

import lombok.Builder;
import lombok.Getter;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/24 4:33 下午
 * @Since 1.0
 */
public class CertificateUtils {

    private interface SSLStatus {
        Integer SAFE = 1;
        Integer UNSAFE = 2;
    }

    @Getter
    @Builder
    public static class SSLResult {
        private String dn;
        private Integer status;
        private Date startTime;
        private Date endTime;
    }

    public static SSLResult checkCertificate(String url) {
        if (!url.startsWith("https://")) {
            url = "https://" + url;
        }
        try {
            URL httpsUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) httpsUrl.openConnection();
            connection.connect();
            X509Certificate x509Certificate = (X509Certificate) connection.getServerCertificates()[0];
            x509Certificate.checkValidity(); // 校验有效性
            connection.disconnect();
            return SSLResult.builder()
                    .status(SSLStatus.SAFE)
                    .dn(x509Certificate.getSubjectDN().getName())
                    .startTime(x509Certificate.getNotBefore())
                    .endTime(x509Certificate.getNotAfter())
                    .build();
        } catch (Exception e) {
            return SSLResult.builder()
                    .status(SSLStatus.UNSAFE)
                    .build();
        }
    }

}
