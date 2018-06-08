package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ExplainDao;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.explain.ExplainDTO;
import com.sdg.cmdb.domain.explain.ExplainInfo;
import com.sdg.cmdb.domain.explain.ExplainJob;
import com.sdg.cmdb.processor.GitProcessor;
import com.sdg.cmdb.service.ExplainService;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by zxxiao on 2017/3/21.
 */
@Service
public class ExplainServiceImpl implements ExplainService {

    private static final Logger logger = LoggerFactory.getLogger(ExplainServiceImpl.class);

    @Value("#{cmdb['explain.localPath']}")
    private String localPath;

    @Value("#{cmdb['explain.git.username']}")
    private String username;

    @Value("#{cmdb['explain.git.pwd']}")
    private String pwd;

    @Resource
    private ExplainDao explainDao;

    @Override
    public void addRepoExplainSub(ExplainDTO explainDTO) {
        ExplainInfo explainInfo = new ExplainInfo(explainDTO);

        if (explainInfo.getId() == 0) {
            explainDao.addRepoExplainSub(explainInfo);
        } else {
            explainDao.updateRepoExplainSub(explainInfo);
        }
    }

    @Override
    public void delRepoExplainSub(long id) {
        explainDao.delRepoExplainSub(id);
    }

    @Override
    public TableVO<List<ExplainDTO>> queryRepoExplainSubList(String repo, int page, int length) {
        long size = explainDao.queryRepoExplainSubListSize(repo);
        List<ExplainInfo> explainInfoList = explainDao.queryRepoExplainSubListPage(repo, page * length, length);

        List<ExplainDTO> explainDTOList = new ArrayList<>();
        for(ExplainInfo explainInfo : explainInfoList) {
            ExplainDTO explainDTO = new ExplainDTO(explainInfo);
            explainDTOList.add(explainDTO);
        }
        return new TableVO<>(size, explainDTOList);
    }

    @Override
    public Set<String> doScanRepo(ExplainDTO explainDTO) {
        try {
            Git git = GitProcessor.cloneRepository(localPath + "/explain/" + explainDTO.getId() + "/", explainDTO.getRepo(), username, pwd, Collections.EMPTY_LIST);

            Set<String> refSet = GitProcessor.getRefList(git.getRepository());
            refSet.remove("HEAD");
            refSet.remove("head");

            return refSet;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.EMPTY_SET;
    }

    @Override
    public TableVO<List<String>> queryRepoList(String repo, int page, int length) {
        return new TableVO<>(0, explainDao.queryRepoList(repo, page, length));
    }

    @Override
    public ExplainDTO getRepoSubById(long id) {
        ExplainInfo explainInfo = explainDao.getRepoSubById(id);
        return new ExplainDTO(explainInfo);
    }

    @Override
    public TableVO<List<ExplainJob>> queryJobs(ExplainJob explainJob, int page, int length) {
        long size = explainDao.queryExplainJobSize(explainJob);
        List<ExplainJob> jobList = explainDao.queryExplainJobPage(explainJob, page * length, length);

        return new TableVO<>(size, jobList);
    }

    @Override
    public boolean subJob(ExplainJob explainJob) {
        return false;
    }

    @Override
    public boolean unsubJob(ExplainJob explainJob) {
        return false;
    }
}
