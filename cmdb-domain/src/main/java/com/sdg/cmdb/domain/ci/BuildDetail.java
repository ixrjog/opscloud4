package com.sdg.cmdb.domain.ci;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class BuildDetail  extends CiBuildDO implements Serializable{

    public static final String COLOR_RED= "#b92c28";
    public static final String COLOR_GREEN ="#20a03f";
    public static final String COLOR_ORANGE ="#ff8700";
    public static final String COLOR_YELLOW ="#f0ad4e";


    private String color = "#3abee8";
    private boolean building = true;

    public BuildDetail(){
    }

    public BuildDetail(CiBuildDO ciBuildDO){
        setBuildNumber(ciBuildDO.getBuildNumber());
        setBuildPhase(ciBuildDO.getBuildPhase());
        setBuildStatus(ciBuildDO.getBuildStatus());
        if(!StringUtils.isEmpty(getBuildPhase()) && getBuildPhase().equals("FINALIZED")){
            this.building = false;
            if(StringUtils.isEmpty(getBuildStatus())) {
                this.color = COLOR_RED;
            }else if(getBuildStatus().equals("SUCCESS")) {
                this.color = COLOR_GREEN;
            }else if(getBuildStatus().equals("FAILURE")){
                this.color = COLOR_RED;
            }else if(getBuildStatus().equals("ABORTED")){
                this.color = COLOR_YELLOW;
            }else if(getBuildStatus().equals("UNSTABLE")){
                this.color = COLOR_YELLOW;
            }
        }
    }


}
