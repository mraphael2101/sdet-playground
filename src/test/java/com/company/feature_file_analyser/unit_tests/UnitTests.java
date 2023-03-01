package com.company.feature_file_analyser.unit_tests;

import com.company.feature_file_analyser.FeatureFileAnalyser_Prototype;
import org.junit.Test;

public class UnitTests {

    private final FeatureFileAnalyser_Prototype prototype;

    public UnitTests() {
        prototype = new FeatureFileAnalyser_Prototype("/src/test/resources/feature_file_analyser/features/");
        prototype.calculateCodeReuseAtBddLevel();
    }

    @Test
    public void backgroundKeywordTest() {  //TODO
    }

    @Test
    public void isStepDataDrivenTest() {  //TODO
//        prototype.readDataTableRowCountFromFeatureFiles();
    }

    @Test
    public void validateDataDrivenCountForParameterisedStepTest() {  //TODO
    }

    @Test
    public void validateLowLevelSummaryTest() {     //TODO
        prototype.printLowLevelSummary();
    }

    @Test
    public void validateHighLevelSummaryTest() {    //TODO
        prototype.printHighLevelSummary();
    }

    @Test
    public void validateThresholdsSummaryTest() {   //TODO
        prototype.printSummaryWithThresholds();
    }

    @Test
    public void validateCodeReuseLevelClassificationTest() {   //TODO
        prototype.printCodeReuseLevelClassification();
    }

}
