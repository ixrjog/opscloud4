package com.sdg.cmdb.util.prometheus.template.iptables;

import com.sdg.cmdb.util.prometheus.template.format.IptablesRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangjian on 16/11/2.
 */
public class Iptables {

    private String desc = "";

    private String getDesc() {
        if (desc == null) return "";
        return "# " + desc + "\n";
    }

    private String body = "";

    private List<IptablesRule> irList;

    private String info;

    private String getInfo() {
        if (StringUtils.isEmpty(info)) {
            FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
            this.info = "# Created by cmdb on " + fastDateFormat.format(new Date()) + "\n\n";
        }
        return this.info;
    }

    public Iptables(List<IptablesRule> irList, String desc) {
        if (irList != null) this.irList = irList;
        if (desc != null) this.desc = desc;
    }

    public Iptables(IptablesRule ir, String desc) {
        if (ir != null) {
            this.irList = new ArrayList<IptablesRule>();
            irList.add(ir);
        }
        if (desc != null) this.desc = desc;
    }

    public String toBody() {
        body = getInfo();
        body = body + getDesc();
        for (IptablesRule ir : irList) {
            body = body + ir.getRule() +"\n";
        }
        return body;
    }


}
