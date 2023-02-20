package com.company.feature_file_analyser.unit_tests;

import com.company.feature_file_analyser.FeatureFileAnalyser_Prototype;
import org.junit.Test;

public class UnitTests {

    FeatureFileAnalyser_Prototype prototype = new FeatureFileAnalyser_Prototype();

    @Test
    public void populateMapWithGherkinMetricsHappyPathTest() {  //TODO
        prototype.calculateCodeReuseAtBddLevel();
        prototype.printSummary();
    }

    @Test
    public void gherkinIncludesDataDrivenStepsTest() {  //TODO
        // Generate a Gherkin file with data-driven steps
//        prototype.calculateCodeReuseAtBddLevel();
//        prototype.printSummary();
//        validate programmatically
    }

}
