package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.TreeMap;

@Slf4j
public class StepMetaData {
    @Getter
    private final TreeMap<String, Integer> filePathsDataTableRowCountsMap = new TreeMap<>();
    @Getter
    private final TreeMap<String, Integer> filePathsScenarioCountsMap = new TreeMap<>();
    @Getter
    private final TreeMap<String, Integer> filePathsScenarioOutlineCountsMap = new TreeMap<>();
    @Getter @Setter
    private String stepName = "";
    @Getter @Setter
    private String stepType = "";
    @Getter @Setter
    private boolean isBackground = false;
    @Getter @Setter
    private boolean isDataDriven = false;
    @Getter @Setter
    private boolean isDataTableDriven = false;
    @Getter @Setter
    private int scenarioRecurrenceCount = 0;
    @Getter @Setter
    private int scenarioOutlineRecurrenceCount = 0;
    public String getFilePathWhereStepWasIdentified() {
        Optional<String> key = Optional.ofNullable(filePathsDataTableRowCountsMap.firstKey());
        return key.orElse("Something has gone wrong");
    }
    public int getFilePathDataTableDrivenCountForStep() {
        Optional<Integer> value = filePathsDataTableRowCountsMap.values().stream().findFirst();
        return value.orElse(0);
    }
    public void setFilePathsScenarioCountsMap(String key, int value) {
        this.filePathsScenarioCountsMap.put(key, value);
    }
    public void setFilePathsScenarioOutlineCountsMap(String key, int value) {
        this.filePathsScenarioOutlineCountsMap.put(key, value);
    }
    public void setFilePathsDataTableRowCountsMap(String key, int value) {
        this.filePathsDataTableRowCountsMap.put(key, value);
    }

}
