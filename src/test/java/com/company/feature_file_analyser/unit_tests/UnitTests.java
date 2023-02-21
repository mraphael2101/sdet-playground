package com.company.feature_file_analyser.unit_tests;

import com.company.feature_file_analyser.FeatureFileAnalyser_Prototype;
import org.junit.Test;

public class UnitTests {

    private final FeatureFileAnalyser_Prototype prototype;

    public UnitTests() {
        prototype = new FeatureFileAnalyser_Prototype();
        prototype.calculateCodeReuseAtBddLevel();
    }

    @Test
    public void populateMapWithGherkinMetricsHappyPathTest() {  //TODO
    }

    @Test
    public void gherkinIncludesDataDrivenStepsTest() {  //TODO
        // Generate a Gherkin file with data-driven steps
//        prototype.calculateCodeReuseAtBddLevel();
//        prototype.printSummary();
//        validate programmatically
    }

    @Test
    public void validateLowLevelSummaryTest() {     //TODO
        prototype.printLowLevelSummary();
    }

    @Test
    public void validateHighLevelSummaryTest() {    //TODO
        prototype.printHighLevelSummary();
    }

}
