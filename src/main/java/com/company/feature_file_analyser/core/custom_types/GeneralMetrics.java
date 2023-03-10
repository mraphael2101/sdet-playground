package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

public class GeneralMetrics {

    @Getter
    @Setter
    private long overallStepReuseCount = 0;

    @Getter
    @Setter
    private long totalNoOfSteps = 0;

}
