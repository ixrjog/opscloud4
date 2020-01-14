package com.baiyi.opscloud.aws.ec2.base;

import lombok.Data;

/**
 * @Author <a href="mailto:xujian@gegejia.com">修远</a>
 * @Date 2019/7/2 2:14 PM
 * @Since 1.0
 */
@Data
public class EC2Volume {

    private String volumeId;
    private String deviceName;
    private Integer size;
    private Integer iops;
    private Boolean isRootDevice;

    public EC2Volume() {
    }

    public EC2Volume(com.amazonaws.services.ec2.model.Volume volume, String rootDeviceName) {
        this.volumeId = volume.getVolumeId();
        this.deviceName = volume.getAttachments().get(0).getDevice();
        this.size = volume.getSize();
        this.iops = volume.getIops();
        this.isRootDevice = this.deviceName.equals(rootDeviceName);
    }
}
