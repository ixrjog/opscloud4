package com.offbytwo.jenkins.model;

public class JacocoCoverageReport extends BaseModel {

    private JacocoCoverageResult lineCoverage;
    private JacocoCoverageResult classCoverage;
    private JacocoCoverageResult complexityScore;
    private JacocoCoverageResult instructionCoverage;
    private JacocoCoverageResult branchCoverage;

    public JacocoCoverageResult getLineCoverage() {
        return lineCoverage;
    }

    public JacocoCoverageReport setLineCoverage(JacocoCoverageResult lineCoverage) {
        this.lineCoverage = lineCoverage;
        return this;
    }

    public JacocoCoverageResult getClassCoverage() {
        return classCoverage;
    }

    public JacocoCoverageReport setClassCoverage(JacocoCoverageResult classCoverage) {
        this.classCoverage = classCoverage;
        return this;
    }

    public JacocoCoverageResult getComplexityScore() {
        return complexityScore;
    }

    public JacocoCoverageReport setComplexityScore(JacocoCoverageResult complexityScore) {
        this.complexityScore = complexityScore;
        return this;
    }

    public JacocoCoverageResult getInstructionCoverage() {
        return instructionCoverage;
    }

    public JacocoCoverageReport setInstructionCoverage(JacocoCoverageResult instructionCoverage) {
        this.instructionCoverage = instructionCoverage;
        return this;
    }

    public JacocoCoverageResult getBranchCoverage() {
        return branchCoverage;
    }

    public JacocoCoverageReport setBranchCoverage(JacocoCoverageResult branchCoverage) {
        this.branchCoverage = branchCoverage;
        return this;
    }

}