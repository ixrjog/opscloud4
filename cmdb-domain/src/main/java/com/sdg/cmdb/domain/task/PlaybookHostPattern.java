package com.sdg.cmdb.domain.task;

import com.sdg.cmdb.domain.server.HostPattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class PlaybookHostPattern extends HostPattern implements Serializable {
    private static final long serialVersionUID = 5638639075179493483L;

    private String hostPattern;

    public String getHostPattern(){
        if(StringUtils.isEmpty(this.hostPattern)) return "";
        if(this.hostPattern.indexOf("@") == -1)
            return this.hostPattern;
        return  this.hostPattern.split("@")[1];
    }


}
