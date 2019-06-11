package com.sdg.cmdb.util;


import com.sdg.cmdb.domain.server.EnvType;
import org.gitlab.api.models.GitlabBranch;


import java.util.ArrayList;
import java.util.List;

public class GitFlowUtils {

    public static final String[] DEV_REGEX = {"^develop$", "^dev$", "^feature/.+", "^support/.+", "^release/.+", "^hotfix/.+", "^master$"};
    public static final String[] TEST_REGEX = {"^develop$", "^dev$", "^feature/.+", "^support/.+", "^release/.+", "^hotfix/.+", "^master$"};
    public static final String[] PRE_REGEX = {"^support/.+", "^release/.+", "^hotfix/.+", "^master$"};
    public static final String[] PROD_REGEX = {"^support/.+", "^hotfix/.+", "^master$"};

    /**
     * 按GitFlow过滤分支
     * @param branchs
     * @param envType
     * @return
     */
    public static List<GitlabBranch> filterBranchs(List<GitlabBranch> branchs, int envType) {
        if (envType == EnvType.EnvTypeEnum.dev.getCode())
            return filterBranchs(branchs, DEV_REGEX);
        if (envType == EnvType.EnvTypeEnum.test.getCode())
            return filterBranchs(branchs, TEST_REGEX);
        if (envType == EnvType.EnvTypeEnum.pre.getCode() || envType == EnvType.EnvTypeEnum.gray.getCode())
            return filterBranchs(branchs, PRE_REGEX);
        if (envType == EnvType.EnvTypeEnum.prod.getCode())
            return filterBranchs(branchs, PROD_REGEX);
        return branchs;
    }

    private static List<GitlabBranch> filterBranchs(List<GitlabBranch> branchs, String[] regexs) {
        List<GitlabBranch> branchList = new ArrayList<>();
        for (GitlabBranch gitlabBranch : branchs) {
            for (String regex : regexs) {
                if (gitlabBranch.getName().matches(regex)) {
                    branchList.add(gitlabBranch);
                    break;
                }
            }
        }
        return branchList;
    }


}
