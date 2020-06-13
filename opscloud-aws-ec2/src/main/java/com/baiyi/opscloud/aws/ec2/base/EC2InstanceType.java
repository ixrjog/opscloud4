package com.baiyi.opscloud.aws.ec2.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties
public class EC2InstanceType implements Serializable {

    private static final long serialVersionUID = 6227054742749854788L;
    private String memory; // GiB,

    private String clockSpeed; //Up to 3.3 GHz",
    private String physicalProcessor; //"Intel Xeon Family",
    private int vcpu; //1,
    private boolean currentGeneration;//true,
    @JsonIgnore
    private String prices;
    @JsonIgnore
    private String regions;
    @JsonIgnore
    private String ecu;
    @JsonIgnore
    private String gpu;
    private boolean enhancedNetworkingSupported;
    private String dedicatedEbsThroughput;
    private String networkPerformance;//"Low",
    private int normalizationSizeFactor;//0,
    private String storage;//EBS only",

    public Integer acqMemory() {
        try{
            Double number = (Double.parseDouble(memory.split(" ")[0]) * 1024);
            return number.intValue();
        }catch (Exception e){
            return 0;
        }
    }
}
