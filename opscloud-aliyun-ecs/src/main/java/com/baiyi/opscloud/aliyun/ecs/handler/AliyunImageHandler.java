package com.baiyi.opscloud.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/17 4:07 下午
 * @Version 1.0
 */
@Component
public class AliyunImageHandler {

    @Resource
    private AliyunCore aliyunCore;

    public static final int QUERY_PAGE_SIZE = 50;

    public static final String SELF = "self";

    /**
     * 查询镜像
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
                DescribeImagesResponse response = getDescribeImagesResponse(describe, regionId);
                images.addAll(response.getImages());
                size = response.getImages().size();
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
            DescribeImagesResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private IAcsClient acqAcsClient(String regionId) {
        return aliyunCore.getAcsClient(regionId);
    }
}
