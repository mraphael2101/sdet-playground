package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class BackgroundStep {
    @Getter
    @Setter
    private String Path = "";
    @Getter
    @Setter
    private int lineNumber = 0;
    @Getter
    private final List<String> backgroundStepNames = new ArrayList<>();
    @Getter
    private final List<Step> stepsAtBackgroundLevel = new ArrayList<>();

    public void addStep(Step step) {
        this.stepsAtBackgroundLevel.add(step);
    }

    public void addStepName(String name) {
        this.backgroundStepNames.add(name);
    }

}
