package com.baiyi.opscloud.datasource.kubernetes.client.provider.eks;

import com.amazonaws.DefaultRequest;
import com.amazonaws.auth.*;
import com.amazonaws.auth.presign.PresignerFacade;
import com.amazonaws.auth.presign.PresignerParams;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.internal.auth.DefaultSignerProvider;
import com.amazonaws.internal.auth.SignerProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/3/3 2:22 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class AmazonEksGenerator {

    /**
     * https://docs.aws.amazon.com/zh_cn/zh_cn/sdk-for-java/v1/developer-guide/prog-services-sts.html
     * 对于 IAM 用户，临时凭证的有效期范围是 900 秒 (15 分钟) 到 129600 秒 (36 小时)。如果不指定有效期，则默认使用 43200 秒（12 小时）
     * <p>
     * https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_enable-regions.html
     * STS可用区
     * <p>
     * https://medium.com/@rschoening/eks-client-authentication-f17f39228dc
     * STS签名Token
     *
     * @param amazonEks
     * @return
     * @throws URISyntaxException
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10M, key = "'eks_url_'+ #amazonEks.url", unless = "#result == null")
    public String generateEksToken(KubernetesConfig.AmazonEks amazonEks) throws URISyntaxException {
        DefaultRequest<GetCallerIdentityRequest> defaultRequest = new DefaultRequest<>(new GetCallerIdentityRequest(), "sts");
        URI uri = new URI("https", "sts.amazonaws.com", null, null);
        defaultRequest.setResourcePath("/");
        defaultRequest.setEndpoint(uri);
        defaultRequest.setHttpMethod(HttpMethodName.GET);
        defaultRequest.addParameter("Action", "GetCallerIdentity");
        defaultRequest.addParameter("Version", "2011-06-15");
        defaultRequest.addHeader("x-k8s-aws-id", amazonEks.getClusterName());
        BasicAWSCredentials basicCredentials = new BasicAWSCredentials(amazonEks.getAccessKeyId(), amazonEks.getSecretKey());
        AWSStaticCredentialsProvider credentials = new AWSStaticCredentialsProvider(basicCredentials);
        Signer signer = SignerFactory.createSigner(SignerFactory.VERSION_FOUR_SIGNER, new SignerParams("sts", amazonEks.getRegion()));
        AWSSecurityTokenServiceClient stsClient = (AWSSecurityTokenServiceClient) AWSSecurityTokenServiceClientBuilder
                .standard()
                .withRegion(amazonEks.getRegion())
                .withCredentials(credentials)
                .build();
        SignerProvider signerProvider = new DefaultSignerProvider(stsClient, signer);
        PresignerParams presignerParams = new PresignerParams(uri, credentials, signerProvider, SdkClock.STANDARD);
        PresignerFacade presignerFacade = new PresignerFacade(presignerParams);
        URL url = presignerFacade.presign(defaultRequest, new Date(System.currentTimeMillis() + 60000));
        String encodedUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(url.toString().getBytes());
        log.debug("Generate EKS Token: clusterName={}, url={}", amazonEks.getClusterName(),amazonEks.getUrl());
        return Joiner.on(".").join("k8s-aws-v1", encodedUrl);
    }

}