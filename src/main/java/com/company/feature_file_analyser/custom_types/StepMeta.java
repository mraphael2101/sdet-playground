package com.company.feature_file_analyser.custom_types;

import java.util.ArrayList;
import java.util.List;

public class StepMeta {

    private List<String> filePaths = new ArrayList<>();
    private String stepName = "";

    private String stepType = "";

    private boolean isDataDriven = false;

    private int stepReuseCount = -1;


    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String path) {
        this.filePaths.add(path);
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public boolean isDataDriven() {
        return isDataDriven;
    }

    public void setDataDriven(boolean dataDriven) {
        isDataDriven = dataDriven;
    }

    public int getStepReuseCount() {
        return stepReuseCount;
    }

    public void setStepReuseCount(int stepReuseCount) {
        this.stepReuseCount = stepReuseCount;
    }
}
