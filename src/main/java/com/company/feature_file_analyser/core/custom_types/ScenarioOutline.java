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
    private final List<String> scenarioOutlineNames = new ArrayList<>();

    @Getter
    @Setter
    private DataTable dataTable = null;

    public void addScenarioOutlineName(String name) {
        this.scenarioOutlineNames.add(name);
    }
}
