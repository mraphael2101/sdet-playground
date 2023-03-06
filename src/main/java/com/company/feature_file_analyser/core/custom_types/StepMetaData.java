package com.company.feature_file_analyser.core.custom_types;

import java.util.TreeMap;

public class StepMetaData {

    private final TreeMap<String, Integer> filePathsDataTableRowCountsMap = new TreeMap<>();

    private final TreeMap<String, Integer> filePathsScenarioCountsMap = new TreeMap<>();
    private String stepName = "";

    private String stepType = "";

    private boolean isBackground = false;

    private boolean isDataDriven = false;
    private boolean isDataTableDriven = false;

    private long dataTableRowCount = 0;

    public TreeMap<String, Integer> getFilePathsScenarioCountsMap() {
        return this.filePathsScenarioCountsMap;
    }

    public void setFilePathsScenarioCountsMap(String key, int value) {
        this.filePathsScenarioCountsMap.put(key, value);
    }

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

    public boolean isDataTableDriven() {
        return this.isDataTableDriven;
    }

    public void setDataTableDriven(boolean flag) {
        this.isDataTableDriven = flag;
    }

    public long getDataTableRowCount() {
        return this.dataTableRowCount;
    }

    public void setDataTableRowCount(long dataTableRowCount) {
        this.dataTableRowCount = dataTableRowCount;
    }

    public boolean isDataDriven() {
        return isDataDriven;
    }

    public void setDataDriven(boolean dataDriven) {
        isDataDriven = dataDriven;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }
}
