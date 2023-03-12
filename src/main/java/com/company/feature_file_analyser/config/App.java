package com.company.feature_file_analyser.config;

import com.company.feature_file_analyser.core.Analyser;

public class App {
    private static Analyser analyser;
    private static String filePath = "/src/test/resources/feature_file_analyser/features/test_data_set_2";
    public static void main(String [] args) {
        analyser = new Analyser(filePath);
        analyser.calculateCodeReuseForAtdd();
        analyser.getMetrics().printLowLevelSummary();
        analyser.getMetrics().printHighLevelSummary();
        analyser.getMetrics().printSummaryWithThresholds();
        analyser.getMetrics().printCodeReuseLevelClassification();
    }

}
