package com.baiyi.opscloud.test;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.TimeUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/4/26 13:36
 * @Version 1.0
 */
public class NibssTest extends BaseUnit {

    /**
     * public static void cst2Date() throws ParseException {
     * SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.US); // 注意使用Locale.US参数
     * String cst = "Tue May 16 19:30:20 CST 2018";
     * Date date = sdf.parse(cst);
     * System.out.println(date);
     * }
     */

    private static final String LOG = "[26/Apr/2022:03:01:59 +0000] \"POST /webservice/nibss/NIPInterface HTTP/1.1\" \"200\" \"0.116\" \"0.116\"";
    //         dd/MMM/yyyy:HH:mm:ss


    @Test
    void statisticsTest() {
        Iterable<String> iterable = Splitter.on(" ").split(LOG);

        List<String> logs = Lists.newArrayList();
        // 26/Apr/2022:06:28:51 499 "59.999" "60.000"
        for (String s : iterable) {
            logs.add(s);
        }
        print(logs.size());
        print(logs);
        String pat = "dd/MMM/yyyy:HH:mm:ss";
        Map<String, StatisticsLog.Log> statisticsLogMap = Maps.newHashMap();


        // /Users/liangjian/temp

        //  IOUtil.readFile()

        SimpleDateFormat sdf = new SimpleDateFormat(pat, java.util.Locale.US);

//        try {
//            LineNumberReader reader = new LineNumberReader(new FileReader(auditLogPath));
//            while (session.isOpen() && (str = reader.readLine()) != null) {
//                if (!str.isEmpty()) {
//                    send(str +"\n");
//                }
//                TimeUnit.MILLISECONDS.sleep(25L);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }



        try {
            Date date = sdf.parse(logs.get(0).replace("[", ""));
            print(date.getTime());
            print(TimeUtil.toGmtDate(date));
            String timeName = TimeUtil.toGmtDate(date).substring(0, 16);
            print(timeName);
            if (statisticsLogMap.containsKey(timeName)) {
                StatisticsLog.Log log = statisticsLogMap.get(timeName);
                // 插入状态
                log.setStatus200(log.getStatus200() + 1);

                log.getRts().add(1);

            } else {
                StatisticsLog.Log log = StatisticsLog.Log.builder().build();
            }

        } catch (ParseException e) {

        }

    }


}
