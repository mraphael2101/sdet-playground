package com.company.feature_file_analyser.config;

import com.company.feature_file_analyser.core.FeatureFilesAnalyser_Prototype;

public class App {

    private static FeatureFilesAnalyser_Prototype prototype;
    private static String filePath = "/src/test/resources/feature_file_analyser/features/test_data_set_2";
    public static void main(String [] args) {
        prototype = new FeatureFilesAnalyser_Prototype(filePath);
        prototype.calculateCodeReuseAtAcceptanceTestLevel();
        prototype.printLowLevelSummary();
        prototype.printHighLevelSummary();
        prototype.printSummaryWithThresholds();
        prototype.printCodeReuseLevelClassification();
    }

}
