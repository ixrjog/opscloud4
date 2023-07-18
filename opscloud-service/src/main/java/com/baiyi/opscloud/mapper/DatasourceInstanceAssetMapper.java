package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.datasource.DsCustomAssetParam;
import com.baiyi.opscloud.domain.param.report.ApolloReportParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DatasourceInstanceAssetMapper extends Mapper<DatasourceInstanceAsset> {

    List<String> queryInstanceAssetTypes(String instanceUuid);

    List<DatasourceInstanceAsset> queryUserPermissionAssetByParam(DsAssetParam.UserPermissionAssetPageQuery pageQuery);

    List<DatasourceInstanceAsset> queryApolloAssetPageByParam(DsCustomAssetParam.ApolloReleaseAssetPageQuery pageQuery);

    List<ReportVO.Report> statApolloReleaseLast30Days(ApolloReportParam.ApolloReleaseReport apolloReleaseReport);

}