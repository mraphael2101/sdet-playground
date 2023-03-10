package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    @Getter
    @Setter
    private String scenarioFilePath = "";

    @Getter
    private final List<String> scenarioNames = new ArrayList<>();

    public void addScenarioName(String name) {
        this.scenarioNames.add(name);
    }

}
