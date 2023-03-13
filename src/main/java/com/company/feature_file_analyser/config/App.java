package com.company.feature_file_analyser.config;

import com.company.feature_file_analyser.core.Analyser;

public class App {
    private static final String filePath = "/src/test/resources/feature_file_analyser/features/test_data_set_1";
    public static void main(String [] args) {
        Analyser analyser = new Analyser(filePath);
        analyser.calculateCodeReuseForAtdd();
        analyser.getMetrics().printLowLevelSummary();
        analyser.getMetrics().printHighLevelSummaryAtFeatureFileLevel();
        analyser.getMetrics().printHighLevelSummary();
        analyser.getMetrics().printSummaryWithThresholds();
        analyser.getMetrics().printCodeReuseLevelClassification();
    }

}
