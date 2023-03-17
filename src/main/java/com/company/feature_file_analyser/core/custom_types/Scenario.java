package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Scenario {
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
    private final List<Step> stepsAtScenarioLevel = new ArrayList<>();
    @Getter
    private final List<String> backgroundStepNames = new ArrayList<>();
    @Getter
    private final List<Step> stepsAtBackgroundLevel = new ArrayList<>();
    public void addStep(Step step) {
        this.stepsAtScenarioLevel.add(step);
    }
    public void addStepName(String name) {
        this.stepNames.add(name);
    }
    public void addBackgroundStep(Step step) {
        this.stepsAtBackgroundLevel.add(step);
    }
    public void addBackgroundStepName(String name) {
        this.backgroundStepNames.add(name);
    }

}
