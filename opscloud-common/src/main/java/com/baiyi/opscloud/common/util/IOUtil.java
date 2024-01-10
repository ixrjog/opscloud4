package com.baiyi.opscloud.common.util;


import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class IOUtil {

    private IOUtil() {
    }

    public static final String COMMENT_SIGN = "#";

    public static String getHeadInfo(String symbol) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        return Joiner.on(" ").join(symbol, Global.CREATED_BY, "on", fastDateFormat.format(new Date()), "\n\n");
    }

    /**
     * 在指定目录下生成文件，并写入内容
     *
     * @param dir：指定的目录
     * @param fileName：待生成的文件，包含名称与格式，如：23.docx；缺省时，采用
     * @param context：文件待写入的内容，覆盖旧内容。
     * @return 写入成功时，返回true，否则返回false
     */
    public static boolean createFile(String dir, String fileName, String context) {
        boolean result = false;
        try {
            if (StringUtils.isBlank(fileName)) {
                return false;
            }
            mkdir(dir);
            if (StringUtils.isNotBlank(dir) && StringUtils.isNotBlank(context)) {
                File file = new File(new File(dir), fileName);
                // 为了为防止context在远程客户端使用HttpClient传输时乱码，对方采用UTF-8发送，这里写入时也采用UTF-8
                FileUtils.write(file, context, StandardCharsets.UTF_8);
                result = true;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
        }
        return feedback;
    }

    public static void mkdir(String path) {
        File file = new File(path);
        try {
            FileUtils.forceMkdir(file);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 将指定字符串内容写入指定位置的文件内
     *
     * @param body
     * @param path
     */
    public static void writeFile(String body, String path) {
        log.info("Write file: path={}", path);

        if (StringUtils.isEmpty(path)) {
            log.error("WriteFile path is null !");
            return;
        }

        mkdir(getPath(path));
        File file = new File(path);
        try (FileWriter fw = new FileWriter(file)) {
            //将字符串写入到指定的路径下的文件中
            fw.write(body);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Long fileSize(String path) {
        try {
            if (StringUtils.isEmpty(path)) {
                return 0L;
            }
            File file = new File(path);
            return file.length();
        } catch (Exception e) {
            return 0L;
        }
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
            log.error(e.getMessage());
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
                BufferedReader bufferedReader = new BufferedReader(reader)
        ) {
            StringBuilder buffer = new StringBuilder();
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) {
                buffer.append(tmp);
                buffer.append(System.lineSeparator());
            }
            return buffer.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getPath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        String[] a = path.split("/");
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
        log.info(SessionHolder.getUsername() + " del file " + path);

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