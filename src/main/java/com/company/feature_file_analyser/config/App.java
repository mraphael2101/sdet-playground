package com.company.feature_file_analyser.config;

import com.company.feature_file_analyser.core.FeatureFileAnalyser_Prototype;

public class App {

    private static FeatureFileAnalyser_Prototype prototype;

    public static void main(String [] args) {
        prototype = new FeatureFileAnalyser_Prototype("/src/test/resources/feature_file_analyser/features/");

        prototype.calculateCodeReuseAtBddLevel();

        prototype.printLowLevelSummary();
        prototype.printHighLevelSummary();
        prototype.printSummaryWithThresholds();
        prototype.printCodeReuseLevelClassification();
    }

}
