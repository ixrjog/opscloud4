package com.sdg.cmdb.domain.ci;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class BuildArtifactVO extends BuildArtifactDO implements Serializable {

    private static final long serialVersionUID = 7294772174686082059L;
    private String size;

}
