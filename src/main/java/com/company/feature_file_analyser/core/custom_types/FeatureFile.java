package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class FeatureFile {

    @Getter
    @Setter
    private String filePath = "";

    @Getter
    private final List<Scenario> listOfScenarios = new ArrayList<>();

    @Getter
    private int scenarioRecurrenceCount = 0;

    @Getter
    private final List<ScenarioOutline> listOfScenarioOutlines = new ArrayList<>();

    @Getter
    @Setter
    private int scenarioOutlineRecurrenceCount = 0;

    @Getter
    @Setter
    private int totalNoOfSteps = 0;

    public void addScenario(Scenario scenario) {
        this.listOfScenarios.add(scenario);
    }

    public void addScenarioOutline(ScenarioOutline outline) {
        this.listOfScenarioOutlines.add(outline);
    }

    public void incrementScenarioRecurrenceCount() {
        this.scenarioRecurrenceCount += 1;
    }

    public void incrementScenarioOutlineRecurrenceCount() {
        this.scenarioOutlineRecurrenceCount += 1;
    }
}
