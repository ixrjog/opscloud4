package com.baiyi.opscloud.service.leo.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.LeoMonitorParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoBuildRequestParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.mapper.LeoBuildMapper;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:03
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoBuildServiceImpl implements LeoBuildService {

    private final LeoBuildMapper buildMapper;

    @Override
    public void add(LeoBuild leoBuild) {
        buildMapper.insert(leoBuild);
    }

    @Override
    public LeoBuild getById(Integer id) {
        return buildMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateByPrimaryKeySelective(LeoBuild leoBuild) {
        buildMapper.updateByPrimaryKeySelective(leoBuild);
    }

    @Override
    public int getMaxBuildNumberWithJobId(Integer jobId) {
        Integer maxBuildNumber = buildMapper.getMaxBuildNumberWithJobId(jobId);
        return maxBuildNumber == null ? 0 : maxBuildNumber;
    }

    @Override
    public List<LeoBuild> queryTheHistoricalBuildToBeDeleted(Integer jobId) {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId)
                .andEqualTo("isFinish", true)
                .andEqualTo("isDeletedBuildJob", false);
        example.setOrderByClause("id desc");
        return buildMapper.selectByExample(example);
    }

    @Override
    public List<LeoBuild> queryWithJobId(Integer jobId) {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId);
        return buildMapper.selectByExample(example);
    }

    @Override
    public DataTable<LeoBuild> queryBuildPage(SubscribeLeoBuildRequestParam pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("jobId", pageQuery.getJobIds());
        example.setOrderByClause("id desc");
        List<LeoBuild> data = buildMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<LeoBuild> queryBuildPage(LeoJobParam.JobBuildPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
//        Example example = new Example(LeoBuild.class);
//        Example.Criteria criteria = example.createCriteria();
//        if (IdUtil.isNotEmpty(pageQuery.getJobId())) {
//            criteria.andEqualTo("jobId", pageQuery.getJobId());
//        }
//        if (IdUtil.isNotEmpty(pageQuery.getApplicationId())) {
//            criteria.andEqualTo("applicationId", pageQuery.getApplicationId());
//        }
//        if (IdUtil.isNotEmpty(pageQuery.getEnvType())) {
//            criteria.andEqualTo("envType", pageQuery.getEnvType());
//        }
//        if (pageQuery.getIsActive() != null) {
//            criteria.andEqualTo("isActive", pageQuery.getIsActive());
//        }
//        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
//            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
//            Example.Criteria criteria2 = example.createCriteria();
//            criteria2.orLike("username", likeName)
//                    .orLike("versionName", likeName)
//                    .orLike("versionDesc", likeName);
//            example.and(criteria2);
//        }
//        example.setOrderByClause("id desc");
//        List<LeoBuild> data = leoBuildMapper.selectByExample(example);
        List<LeoBuild> data = buildMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<LeoBuild> queryBuildVersion(LeoDeployParam.QueryDeployVersion queryBuildVersion) {
        Page<?> page = PageHelper.startPage(1, 10);
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", queryBuildVersion.getJobId())
                .andEqualTo("isFinish", true)
                .andEqualTo("isActive", true);
        if (StringUtils.isNotBlank(queryBuildVersion.getQueryName())) {
            // 用户输入数字，搜索构建编号
            if (StringUtils.isNumeric(queryBuildVersion.getQueryName())) {
                criteria.andLike("buildNumber", queryBuildVersion.getQueryName());
            } else {
                String likeName = SQLUtil.toLike(queryBuildVersion.getQueryName());
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.orLike("versionName", likeName)
                        .orLike("versionDesc", likeName);
                example.and(criteria2);
            }
        }
        example.setOrderByClause("id desc");
        return buildMapper.selectByExample(example);
    }

    @Override
    public int countWithJobId(Integer jobId) {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId);
        return buildMapper.selectCountByExample(example);
    }

    @Override
    public List<LeoBuild> queryLatestBuildWithJobId(Integer jobId, int size) {
        Page<?> page = PageHelper.startPage(1, size);
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId);
        example.setOrderByClause("id desc");
        return buildMapper.selectByExample(example);
    }

    @Override
    public List<LeoBuild> queryNotFinishBuildWithOcInstance(String ocInstance) {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isFinish", false)
                //.andEqualTo("isActive", false)
                .andEqualTo("ocInstance", ocInstance);
        example.setOrderByClause("id desc");
        return buildMapper.selectByExample(example);
    }

    @Override
    public List<LeoBuild> queryNotFinishBuild() {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isFinish", false);
        example.setOrderByClause("id desc");
        return buildMapper.selectByExample(example);
    }

    @Override
    public int countRunningWithJobId(int jobId) {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobId", jobId)
                .andEqualTo("isActive", true)
                .andEqualTo("isFinish", false);
        return buildMapper.selectCountByExample(example);
    }

    @Override
    public List<ReportVO.Report> statByMonth() {
        return buildMapper.statByMonth();
    }

    @Override
    public List<ReportVO.Report> queryMonth() {
        return buildMapper.queryMonth();
    }

    @Override
    public List<ReportVO.Report> statByEnvName() {
        return buildMapper.statByEnvName();
    }

    @Override
    public int countWithReport() {
        Example example = new Example(LeoBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true)
                .andEqualTo("isFinish", true);
        return buildMapper.selectCountByExample(example);
    }

    @Override
    public int statUserTotal() {
        return buildMapper.statUserTotal();
    }

    @Override
    public void deleteById(Integer id) {
        buildMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<LeoBuild> queryLatestLeoBuild(LeoMonitorParam.QueryLatestBuild queryLatestBuild) {
        PageHelper.startPage(1, queryLatestBuild.getSize());
        Example example = new Example(LeoBuild.class);
        if (queryLatestBuild.getIsFinish() != null) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("isFinish", queryLatestBuild.getIsFinish());
        }
        example.setOrderByClause("id desc");
        return buildMapper.selectByExample(example);
    }

}