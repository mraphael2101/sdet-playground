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
    @Setter
    private String scenarioName = "";

    @Getter
    private final List<String> stepNames = new ArrayList<>();

    public void addStepName(String name) {
        this.stepNames.add(name);
    }

}
