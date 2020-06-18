package com.baiyi.opscloud.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;

@Slf4j
public class IOUtils {

    /**
     * 在指定目录下生成文件，并写入内容
     *
     * @param dir：指定的目录
     * @param fileName：待生成的文件，包含名称与格式，如：23.docx；缺省时，采用
     * @param context：文件待写入的内容，覆盖旧内容。
     * @return 写入成功时，返回true，否则返回false
     */
    public static final boolean createFile(String dir, String fileName, String context) {
        boolean result = false;
        try {
            if (StringUtils.isBlank(fileName)) {
                return result;
            }
            mkdir(dir);
            if (StringUtils.isNotBlank(dir) && StringUtils.isNotBlank(context)) {
                File file = new File(new File(dir), fileName);
                /**为了为防止context在远程客户端使用HttpClient传输时乱码，对方采用UTF-8发送，这里写入时也采用UTF-8*/
                FileUtils.write(file, context, Charset.forName("UTF-8"));
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取本地文件文本内容
     *
     * @param file ：待读取的文件
     * @return ：返回读取的文本内容
     */
    public static String readLocalFile(File file) {
        String feedback = "";
        try {
            if (file.exists() && file.isFile()) {
                // 一共重载了3个方法：
                // readFileToString(File file) ：以默认编码读取文件内容
                // readFileToString(File file, String encoding)
                // readFileToString(File file, Charset encoding)
                feedback = FileUtils.readFileToString(file, "utf8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public static void mkdir(String path) {
        File file = new File(path);
        try {
            FileUtils.forceMkdir(file);
        } catch (IOException e) {
        }
    }

    /**
     * 将指定字符串内容写入指定位置的文件内
     *
     * @param body
     * @param path
     */
    public static void writeFile(String body, String path) {
        log.info(SessionUtils.getUsername() + " write file " + path);

        if (StringUtils.isEmpty(path)) {
            log.error("WriteFile path is null !");
            return;
        }

        mkdir(getPath(path));
        File file = new File(path);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(body);//将字符串写入到指定的路径下的文件中
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Long fileSize(String path){
        if (StringUtils.isEmpty(path)) {
            return 0L;
        }
        File file = new File(path);
        return file.length();
    }

    /**
     * 追加文件内容：使用FileWriter
     */
    public static void appendFile(String body, String path) {
        if (StringUtils.isEmpty(path)) {
            log.error("WriteFile path is null !");
            return;
        }
        mkdir(getPath(path));
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(path, true);
            writer.write(body);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将指定文件以字符串形式读出
     *
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
          //  throw new RuntimeException(e);
            return null;
        }
    }

    public static String getPath(String path) {
        if (path == null || path.equals("")) return "";
        String[] a = path.split("\\/");
        path = path.replace(a[a.length - 1], "");
        return path;
    }

    /**
     * 删除指定文件
     *
     * @param path
     * @return
     */
    public static boolean delFile(String path) {
        log.info(SessionUtils.getUsername() + " del file " + path);

        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                FileUtils.forceDelete(file);
                return true;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return false;
            }
        } else {
            return true;
        }
    }

}