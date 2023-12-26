package com.baiyi.opscloud.datasource.jenkins.model;

import lombok.Getter;

@Getter
public class JacocoCoverageReport extends BaseModel {

    private JacocoCoverageResult lineCoverage;
    private JacocoCoverageResult classCoverage;
    private JacocoCoverageResult complexityScore;
    private JacocoCoverageResult instructionCoverage;
    private JacocoCoverageResult branchCoverage;

    public JacocoCoverageReport setLineCoverage(JacocoCoverageResult lineCoverage) {
        this.lineCoverage = lineCoverage;
        return this;
    }

    public JacocoCoverageReport setClassCoverage(JacocoCoverageResult classCoverage) {
        this.classCoverage = classCoverage;
        return this;
    }

    public JacocoCoverageReport setComplexityScore(JacocoCoverageResult complexityScore) {
        this.complexityScore = complexityScore;
        return this;
    }

    public JacocoCoverageReport setInstructionCoverage(JacocoCoverageResult instructionCoverage) {
        this.instructionCoverage = instructionCoverage;
        return this;
    }

    public JacocoCoverageReport setBranchCoverage(JacocoCoverageResult branchCoverage) {
        this.branchCoverage = branchCoverage;
        return this;
    }

}