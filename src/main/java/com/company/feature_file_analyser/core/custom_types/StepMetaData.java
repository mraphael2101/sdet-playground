package com.company.feature_file_analyser.core.custom_types;

import java.util.Optional;
import java.util.TreeMap;

public class StepMetaData {

    private final TreeMap<String, Integer> filePathsDataTableRowCountsMap = new TreeMap<>();
    private final TreeMap<String, Integer> filePathsScenarioCountsMap = new TreeMap<>();
    private final TreeMap<String, Integer> filePathsScenarioOutlineCountsMap = new TreeMap<>();
    private String stepName = "";
    private String stepType = "";
    private boolean isBackground = false;
    private boolean isDataDriven = false;
    private boolean isDataTableDriven = false;
    private long dataTableRowCount = 0;
    private int scenarioRecurrenceCount = 0;
    private int scenarioOutlineRecurrenceCount = 0;
    public String getFilePathWhereStepWasIdentified() {
        Optional<String> key = Optional.ofNullable(filePathsDataTableRowCountsMap.firstKey());
        return key.orElse("Something has gone wrong");
    }

    public int getFilePathDataTableDrivenCountForStep() {
        Optional<Integer> value = filePathsDataTableRowCountsMap.values().stream().findFirst();
        return value.orElse(0);
    }

    public TreeMap<String, Integer> getFilePathsScenarioCountsMap() {
        return this.filePathsScenarioCountsMap;
    }

    public void setFilePathsScenarioCountsMap(String key, int value) {
        this.filePathsScenarioCountsMap.put(key, value);
    }

    public TreeMap<String, Integer> getFilePathsScenarioOutlineCountsMap() {
        return this.filePathsScenarioOutlineCountsMap;
    }

    public void setFilePathsScenarioOutlineCountsMap(String key, int value) {
        this.filePathsScenarioOutlineCountsMap.put(key, value);
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

    public int getScenarioRecurrenceCount() {
        return scenarioRecurrenceCount;
    }

    public void setScenarioRecurrenceCount(int scenarioRecurrenceCount) {
        this.scenarioRecurrenceCount = scenarioRecurrenceCount;
    }

    public int getScenarioOutlineRecurrenceCount() {
        return scenarioOutlineRecurrenceCount;
    }

    public void setScenarioOutlineRecurrenceCount(int scenarioOutlineRecurrenceCount) {
        this.scenarioOutlineRecurrenceCount = scenarioOutlineRecurrenceCount;
    }
}
