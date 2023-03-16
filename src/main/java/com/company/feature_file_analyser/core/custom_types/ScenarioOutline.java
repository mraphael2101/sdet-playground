package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ScenarioOutline {
    @Getter
    @Setter
    private String Path = "";
    @Getter
    @Setter
    private String name = "";
    @Getter
    @Setter
    private int lineNumber = 0;
    @Getter
    private final List<String> stepNames = new ArrayList<>();
    @Getter
    private final List<Step> steps = new ArrayList<>();
    @Getter
    @Setter
    private boolean isDataTableEncountered = false;
    @Getter
    @Setter
    private boolean isDataTableParsingComplete = false;
    @Getter
    @Setter
    private DataTable dataTable = new DataTable();
    public void addStepName(String name) {
        this.stepNames.add(name);
    }
    public void addStep(Step step) {
        this.steps.add(step);
    }

}
