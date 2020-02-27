package com.baiyi.opscloud.sshKey;

import com.baiyi.opscloud.BaseUnit;
import org.junit.jupiter.api.Test;
import sun.security.pkcs.PKCS7;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

/**
 * @Author baiyi
 * @Date 2020/2/27 9:44 上午
 * @Version 1.0
 */
public class SSHKeyTest extends BaseUnit {


    public static X509Certificate readSignatureBlock(InputStream in) throws IOException, GeneralSecurityException {
        PKCS7 pkcs7 = new PKCS7(in);
        return pkcs7.getCertificates()[0];
    }

    @Test
    void testSSHKey() {
        String pubKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDtVLKSMBSDxFBApa+1fmcGG0OHizL6kPfrFY7KMScoILNrhf5y2GoV5WxSSSd73c56YYd5HbfK3CFIjwZ54swDhKEiGkSDA7FOlriv1TTyvhknkDSsnsABibPKtRkP9XT3EzznolwikqWCbANTu1XiIR6EaX5r+rL54mtwE2xqOEKdkbU9wkkd41dEIMcwqcgazzTb3hrUunVFF5JrZXukCkLRRDGtYcXKA4vFOILpqLZiTMW7hPto3F9NGdBIy7ZphD2QUEuVmFgnwpCUb2ps0Ud3uLqdIF+folEHC4rqDBB2Nqgx/vbIB94U3bIJED9zkfOtubC5eUq7IFPrZvPb liangjian@51xianqu.net";
        InputStream in = new ByteArrayInputStream(pubKey.getBytes());

        try{
            PKCS7 pkcs7 = new PKCS7(in);
            X509Certificate publicKey = pkcs7.getCertificates()[0];
            System.out.println("issuer:" + publicKey.getIssuerDN());
            System.out.println("subject:" + publicKey.getSubjectDN());
            System.out.println(publicKey.getPublicKey());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
