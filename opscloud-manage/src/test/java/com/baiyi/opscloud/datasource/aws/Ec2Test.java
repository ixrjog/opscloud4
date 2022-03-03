package com.baiyi.opscloud.datasource.aws;

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
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.ec2.drive.AmazonEc2Drive;
import com.baiyi.opscloud.datasource.aws.ec2.entity.Ec2Instance;
import com.baiyi.opscloud.datasource.aws.ec2.helper.AmazonEc2InstanceTypeHelper;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/21 11:15 AM
 * @Version 1.0
 */
public class Ec2Test extends BaseAwsTest {

    @Resource
    private AmazonEc2Drive amazonEc2Drive;

    @Resource
    private AmazonEc2InstanceTypeHelper amazonEc2InstanceTypeHelper;

    @Test
    void pullAssetTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.AWS.getName(), DsAssetTypeConstants.EC2.name());
        assert assetProvider != null;
        assetProvider.pullAsset(20);
    }


    @Test
    void getAmazonEc2InstanceTypeMapTest() {
        try {
            Map<String, InstanceModel.EC2InstanceType> map = amazonEc2InstanceTypeHelper.getAmazonEc2InstanceTypeMap(getConfig().getAws());
            print(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void listInstancesTest() {
        List<Ec2Instance.Instance> instances = amazonEc2Drive.listInstances("eu-west-2", getConfig().getAws());
        print(instances);
    }

    @Test
    void urlTest() {
        try {
            URL url = new URL(getConfig().getAws().getEc2().getInstances());
            print("url = " + getConfig().getAws().getEc2().getInstances());
            print("host = " + url.getHost());
            print("path = " + url.getPath());
            print("protocol = " + url.getProtocol());
            print("port = " + url.getPort());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateEksToken() throws URISyntaxException {
        String region = "us-east-1";
        String clusterName = "eksworkshop-eksctl";
        String url = "https://D1E85F6BCC9BC11111D2C1126ADA7970.yl4.eu-west-1.eks.amazonaws.com";
        AwsConfig.Account account = getConfig().getAws().getAccount();
        String token = generateEksToken(clusterName, region, account.getAccessKeyId(), account.getSecretKey());
       // NodeList nodeList = KubeClient.build(url, token).nodes().list();
       // print(nodeList.getItems());
    }

    private String generateEksToken(String clusterName, String region, String accessKey, String secretKey) throws URISyntaxException {
        DefaultRequest defaultRequest = new DefaultRequest<>(new GetCallerIdentityRequest(), "sts");
        URI uri = new URI("https", "sts.amazonaws.com", null, null);
        defaultRequest.setResourcePath("/");
        defaultRequest.setEndpoint(uri);
        defaultRequest.setHttpMethod(HttpMethodName.GET);
        defaultRequest.addParameter("Action", "GetCallerIdentity");
        defaultRequest.addParameter("Version", "2011-06-15");
        defaultRequest.addHeader("x-k8s-aws-id", clusterName);
        BasicAWSCredentials basicCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider credentials = new AWSStaticCredentialsProvider(basicCredentials);
        Signer signer = SignerFactory.createSigner(SignerFactory.VERSION_FOUR_SIGNER, new SignerParams("sts", region));
        AWSSecurityTokenServiceClient stsClient = (AWSSecurityTokenServiceClient) AWSSecurityTokenServiceClientBuilder
                .standard().withRegion(region).withCredentials(credentials).build();
        SignerProvider signerProvider = new DefaultSignerProvider(stsClient, signer);
        PresignerParams presignerParams = new PresignerParams(uri, credentials, signerProvider, SdkClock.STANDARD);
        PresignerFacade presignerFacade = new PresignerFacade(presignerParams);
        URL url = presignerFacade.presign(defaultRequest, new Date(System.currentTimeMillis() + 60000));
        byte[] b = url.toString().getBytes();
        String encodedUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
        return "k8s-aws-v1." + encodedUrl;
    }

}
