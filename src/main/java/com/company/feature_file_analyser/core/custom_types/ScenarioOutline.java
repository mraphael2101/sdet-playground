package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ScenarioOutline {

    @Getter
    @Setter
    private String scenarioOutlineFilePath = "";

    @Getter
    @Setter
    private String scenarioOutlineName = "";

    @Getter
    private final List<String> stepNames = new ArrayList<>();

    @Getter
    @Setter
    private DataTable dataTable = new DataTable();

    public void addStepName(String name) {
        this.stepNames.add(name);
    }

}
