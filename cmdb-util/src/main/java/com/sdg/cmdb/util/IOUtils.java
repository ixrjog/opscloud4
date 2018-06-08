package com.sdg.cmdb.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class IOUtils {

    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);
    private static final Logger coreLogger = LoggerFactory.getLogger("coreLogger");

    private String path ;
    private String body ;

    public IOUtils(String body, String path){
        this.body=body;
        this.path=path;
    }

    public void setPath(String path){
        this.path=path;
    }


    public void setBody(String body){
        this.body=body;
    }



    public static void createDir(String path){
        File file = new File(path);
        try {
            FileUtils.forceMkdir(file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 将指定字符串内容写入指定位置的文件内
     * @param body
     * @param path
     */
    public static void writeFile(String body, String path) {
        coreLogger.info(SessionUtils.getUsername() + " write file " + path);

        if(StringUtils.isEmpty(path)){
            coreLogger.error("WriteFile path is null !");
            return ;
        }

        createDir(getPath(path));
        File file = new File(path);
        try(FileWriter fw = new FileWriter(file)) {
            fw.write(body);//将字符串写入到指定的路径下的文件中
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将指定文件以字符串形式读出
     * @param path
     * @return
     */
    public static String readFile(String path) {
        try (
                FileReader reader = new FileReader(path);
                BufferedReader bufferedReader = new BufferedReader(reader);
        ) {
            StringBuffer buffer = new StringBuffer();
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) {
                buffer.append(tmp);
                buffer.append(System.getProperty("line.separator"));
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPath( String path){
        if(path == null || path.equals("")  ) return "";
        String[] a = path.split("\\/");
        path=path.replace(a[a.length-1],"");
        return path;
    }

    /**
     * 删除指定文件
     * @param path
     * @return
     */
    public static boolean delFile(String path) {
        coreLogger.info(SessionUtils.getUsername() + " del file " + path);

        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                FileUtils.forceDelete(file);
                return true;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        } else {
            return true;
        }
    }

}