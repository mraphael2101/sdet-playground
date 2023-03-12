package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class FeatureFile {

    @Getter
    private final List<Scenario> listOfScenarios = new ArrayList<>();
    @Getter
    private final List<String> listOfScenarioNames = new ArrayList<>();
    @Getter
    private final List<ScenarioOutline> listOfScenarioOutlines = new ArrayList<>();
    @Getter
    private final List<String> listOfScenarioOutlineNames = new ArrayList<>();
    @Getter
    private final List<Step> listOfSteps = new ArrayList<>();
    @Getter
    private final Map<String, Integer> mapOfStepNamesRowIndexes = new TreeMap<>();
    @Getter
    @Setter
    private String filePath = "";
    @Getter
    private int scenarioRecurrenceCount = 0;
    @Getter
    @Setter
    private int scenarioOutlineRecurrenceCount = 0;
    @Getter
    @Setter
    private int totalNoOfStepsInFile = 0;
    private int totalNoOfScenarios = 0;
    private int totalNoOfScenarioOutlines = 0;

    public Scenario getScenario(String scenarioName) {
        return listOfScenarios.stream()
                .filter(s -> s.getName().equals(scenarioName))
                .toList()
                .get(0);
    }
    public ScenarioOutline getScenarioOutline(String scenarioOutlineName) {
        return listOfScenarioOutlines.stream()
                .filter(s -> s.getName()
                        .equals(scenarioOutlineName))
                .toList()
                .get(0);
    }
    public void addScenario(Scenario scenario) {
        this.listOfScenarios.add(scenario);
    }
    public void addScenarioOutline(ScenarioOutline outline) {
        this.listOfScenarioOutlines.add(outline);
    }
    public void addScenarioName(String name) {
        this.listOfScenarioNames.add(name);
    }
    public void addScenarioOutlineName(String name) {
        this.listOfScenarioOutlineNames.add(name);
    }
    public void incrementScenarioRecurrenceCount() {
        this.scenarioRecurrenceCount += 1;
    }
    public void incrementScenarioOutlineRecurrenceCount() {
        this.scenarioOutlineRecurrenceCount += 1;
    }
    public void addStep(Step step) {
        this.listOfSteps.add(step);
    }
    public void putStepNameRowIndex(String name, Integer index) {
        this.mapOfStepNamesRowIndexes.put(name, index);
    }
    public int getTotalNoOfScenarios() {
        return totalNoOfScenarios = listOfScenarios.size();
    }
    public int getTotalNoOfScenarioOutlines() {
        return totalNoOfScenarioOutlines = listOfScenarioOutlines.size();
    }
}
