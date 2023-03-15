package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
    private String Path = "";
    @Getter
    @Setter
    private int scenarioRecurrenceCount = 0;
    @Getter
    @Setter
    private int scenarioOutlineRecurrenceCount = 0;
    @Getter
    @Setter
    private int totalNoOfStepsInFile = 0;

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }
        /* Check if o is an instance of FeatureFile or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof FeatureFile)) {
            return false;
        }
        // typecast o to FeatureFile so that we can compare data members
        FeatureFile file = (FeatureFile) o;

        return file.getPath().equals(getPath()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath().hashCode());
    }

    public Scenario getScenario(String scenarioName) {
        return listOfScenarios.stream()
                .filter(s -> s.getName().equals(scenarioName))
                .toList()
                .get(0);
    }
    public Scenario getLastScenario() {
        return this.getListOfScenarios().get(listOfScenarios.size() -1);
    }
    public ScenarioOutline getScenarioOutline(String scenarioOutlineName) {
        return listOfScenarioOutlines.stream()
                .filter(s -> s.getName()
                        .equals(scenarioOutlineName))
                .toList()
                .get(0);
    }
    public ScenarioOutline getScenarioOutlineByIndex(int index) {
        return this.getListOfScenarioOutlines().get(index);
    }
    public ScenarioOutline getLastScenarioOutline() {
        return this.getListOfScenarioOutlines().get(listOfScenarioOutlines.size() -1);
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
        return scenarioRecurrenceCount = listOfScenarios.size();
    }
    public int getTotalNoOfScenarioOutlines() {
        return scenarioOutlineRecurrenceCount = listOfScenarioOutlines.size();
    }

}
