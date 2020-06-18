package com.baiyi.opscloud.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.aliyun.ecs.base.BaseAliyunECS;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/17 4:07 下午
 * @Version 1.0
 */
@Component
public class AliyunImageHandler extends BaseAliyunECS {

    public static final String SELF = "self";

    /**
     * 查询镜像
     *
     * @param regionId
     * @param isSelf
     * @return
     */
    public List<DescribeImagesResponse.Image> getImageList(String regionId, boolean isSelf) {
        List<DescribeImagesResponse.Image> images = Lists.newArrayList();
        try {
            DescribeImagesRequest describe = new DescribeImagesRequest();
            describe.setSysRegionId(regionId);
            // self：您创建的自定义镜像。
            if (isSelf)
                describe.setImageOwnerAlias(SELF);
            describe.setPageSize(QUERY_PAGE_SIZE);
            int size = QUERY_PAGE_SIZE;
            int pageNumber = 1;
            // 循环取值
            while (QUERY_PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                List<DescribeImagesResponse.Image> imageList = getDescribeImagesResponse(describe, regionId).getImages();
                images.addAll(imageList);
                size = imageList.size();
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    private DescribeImagesResponse getDescribeImagesResponse(DescribeImagesRequest describe, String regionId) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

}
