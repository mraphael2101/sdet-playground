package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Step {
    @Getter
    private final List<FeatureFile> listOfFeatureFiles = null;
    @Getter
    private final List<String> listOfFeatureFileNames = null;
    @Getter
    @Setter
    private String stepName = "";
    @Getter
    @Setter
    private String stepType = "";
    @Getter
    @Setter
    private int lineNumber = 0;
    @Getter
    @Setter
    private boolean isBackground = false;
    @Getter
    @Setter
    private boolean isDataDriven = false;
    @Getter
    @Setter
    private boolean isDataTableDriven = false;
    @Getter
    @Setter
    private DataTable dataTable = null;

    public void addFeatureFile(FeatureFile file) {
        this.listOfFeatureFiles.add(file);
    }

    public void addFeatureFileName(String name) {
        this.listOfFeatureFileNames.add(name);
    }

}