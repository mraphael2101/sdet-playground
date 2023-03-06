package com.company.feature_file_analyser.core.custom_types;

import java.util.TreeMap;

public class StepMetaData {

    private final TreeMap<String, Integer> filePathsDataTableRowCountsMap = new TreeMap<>();

    private String stepName = "";

    private String stepType = "";

    private boolean isDataDriven = false;

    public TreeMap<String, Integer> getFilePathsDataTableRowCountsMap() {
        return this.filePathsDataTableRowCountsMap;
    }

    public void setFilePathsDataTableRowCountsMap(String key, int value) {
        this.filePathsDataTableRowCountsMap.put(key, value);
    }

    public String getStepName() {
        return this.stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepType() {
        return this.stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public boolean isDataDriven() {
        return this.isDataDriven;
    }

    public void setDataDriven(boolean flag) {
        this.isDataDriven = flag;
    }

}
