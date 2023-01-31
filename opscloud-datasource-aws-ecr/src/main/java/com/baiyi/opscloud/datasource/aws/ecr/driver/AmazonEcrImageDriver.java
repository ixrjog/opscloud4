package com.baiyi.opscloud.datasource.aws.ecr.driver;

import com.amazonaws.services.ecr.model.DescribeImagesRequest;
import com.amazonaws.services.ecr.model.DescribeImagesResult;
import com.amazonaws.services.ecr.model.ImageDetail;
import com.amazonaws.services.ecr.model.ImageIdentifier;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ecr.service.AmazonEcrService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/17 14:42
 * @Version 1.0
 */
@Component
public class AmazonEcrImageDriver {

    private static final int MAX_RESULTS = 1000;

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param config
     * @param registryId
     * @param repositoryName
     * @return
     */
    public List<ImageDetail> describeImages(String regionId, AwsConfig.Aws config, String registryId, String repositoryName) {
        DescribeImagesRequest request = new DescribeImagesRequest();
        request.setMaxResults(MAX_RESULTS);
        request.setRegistryId(registryId);
        request.setRepositoryName(repositoryName);
        List<ImageDetail> imageDetails = Lists.newArrayList();
        while (true) {
            DescribeImagesResult result = AmazonEcrService.buildAmazonECR(regionId, config).describeImages(request);
            imageDetails.addAll(result.getImageDetails());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return imageDetails;
    }

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param config
     * @param registryId
     * @param repositoryName
     * @return
     */
    public List<ImageDetail> describeImages(String regionId, AwsConfig.Aws config, String registryId, String repositoryName, int size) {
        DescribeImagesRequest request = new DescribeImagesRequest();
        request.setMaxResults(Math.min(size, MAX_RESULTS));
        request.setRegistryId(registryId);
        request.setRepositoryName(repositoryName);
        DescribeImagesResult result = AmazonEcrService.buildAmazonECR(regionId, config).describeImages(request);
        return result.getImageDetails();
    }

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param config
     * @param registryId
     * @param repositoryName
     * @return
     */
    public List<ImageDetail> describeImages(String regionId, AwsConfig.Aws config, String registryId, String repositoryName, String imageTag) {
        DescribeImagesRequest request = new DescribeImagesRequest();
        //request.setMaxResults(Math.min(size, MAX_RESULTS));
        request.setRegistryId(registryId);
        request.setRepositoryName(repositoryName);
        ImageIdentifier imageIdentifier = new ImageIdentifier().withImageTag(imageTag);
        request.setImageIds(Lists.newArrayList(imageIdentifier));
        DescribeImagesResult result = AmazonEcrService.buildAmazonECR(regionId, config).describeImages(request);
        return result.getImageDetails();
    }

}
