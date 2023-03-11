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
    private String filePath = "";

    @Getter
    @Setter
    private String name = "";

    @Getter
    @Setter
    private int lineNumber = 0;

    @Getter
    private final List<String> stepNames = new ArrayList<>();

    public void addStepName(String name) {
        this.stepNames.add(name);
    }

}
