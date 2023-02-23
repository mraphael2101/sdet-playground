package com.company.feature_file_analyser;

public class App {

    private static FeatureFileAnalyser_Prototype prototype;

    public static void main(String [] args) {
        prototype = new FeatureFileAnalyser_Prototype();
        prototype.calculateCodeReuseAtBddLevel();
        prototype.printHighLevelSummary();
    }

}
