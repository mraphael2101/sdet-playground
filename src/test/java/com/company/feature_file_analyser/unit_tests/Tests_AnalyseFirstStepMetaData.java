package com.company.feature_file_analyser.unit_tests;

import com.company.feature_file_analyser.core.Analyser;
import com.company.feature_file_analyser.core.custom_types.StepMetaData;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class Tests_AnalyseFirstStepMetaData {
    private Analyser analyser;
    private StepMetaData firstStep;

    public Tests_AnalyseFirstStepMetaData() {
        analyser = new Analyser("/src/test/resources/feature_file_analyser/features/test_data_set_1");
        analyser.calculateCodeReuseAtAcceptanceTestLevel();
        firstStep = analyser.getListOfAllStepsMetaData().get(0);
    }

    @Test
    public void validateStepNameTest() {
        assertThat(firstStep.getStepName(), equalTo("Given I have details of a 'new' user"));
    }

    @Test
    public void validateStepFilePathTest() {
        assertThat(firstStep.getFilePathWhereStepWasIdentified(), containsString("demo_2.feature"));
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
