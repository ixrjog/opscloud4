package com.sdg.cmdb.processor;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by zxxiao on 2017/3/18.
 */
public class GitProcessor {

    /**
     * clone remote repository to local
     *
     * @param localPathPrefix
     * @param remotePath
     * @param username
     * @param pwd
     * @param branchList
     * @return
     * @throws Exception
     */
    public static Git cloneRepository(String localPathPrefix, String remotePath,
                                      String username, String pwd, List<String> branchList) throws Exception {
        int index = remotePath.lastIndexOf("/");
        String repositoryName = remotePath.substring(index + 1);
        repositoryName = repositoryName.substring(0, repositoryName.length() - 4);

        File localFile = new File(localPathPrefix + repositoryName);
        delDir(localFile);

        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, pwd);

        if (branchList == null || branchList.isEmpty()) {
            return Git.cloneRepository()
                    .setURI(remotePath)
                    .setDirectory(localFile)
                    .setCredentialsProvider(credentialsProvider)
                    .call();
        } else {
            return Git.cloneRepository()
                    .setURI(remotePath)
                    .setDirectory(localFile)
                    .setBranchesToClone(branchList)
                    .setCredentialsProvider(credentialsProvider)
                    .call();
        }
    }

    /**
     * 获取指定仓库的所有分支
     *
     * @param repository
     * @return
     */
    public static Set<String> getRefList(Repository repository) {
        return repository.getAllRefs().keySet();
    }

    /**
     * 删除文件夹及其下的内容
     *
     * @param file
     */
    private static void delDir(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File tmpFile : file.listFiles()) {
                    delDir(tmpFile);
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }
}
