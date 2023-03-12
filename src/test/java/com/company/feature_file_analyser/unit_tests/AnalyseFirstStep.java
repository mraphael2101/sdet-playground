package com.company.feature_file_analyser.unit_tests;

import com.company.feature_file_analyser.core.Analyser;
import com.company.feature_file_analyser.core.custom_types.Step;
import org.junit.Test;

import static com.company.feature_file_analyser.core.file_manipulation.FilesReader.listOfAllSteps;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class AnalyseFirstStep {
    private final Step firstStep;

    public AnalyseFirstStep() {
        Analyser analyser = new Analyser("/src/test/resources/feature_file_analyser/features/test_data_set_1");
        analyser.calculateCodeReuseForAcceptanceTesting();
        firstStep = listOfAllSteps.get(0);
    }

    @Test
    public void validateStepNameTest() {
        assertThat(firstStep.getStepName(), equalTo("Given I have details of a 'new' user"));
    }

    @Test
    public void validateStepFilePathTest() {
//        firstStep.getListOfFeatureFileNames().stream().toList();
//        assertThat(firstStep.getListOfFeatureFileNames(), containsString("demo_2.feature"));
    }

    @Test
    public void validateStepTypeTest() {
        assertThat(firstStep.getStepType(), equalTo("Given"));
    }

    @Test
    public void validateStepIsDataDrivenTest() {
        assertTrue(firstStep.isDataDriven());
    }

}
