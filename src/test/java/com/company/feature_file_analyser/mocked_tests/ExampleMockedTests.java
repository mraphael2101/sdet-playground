package com.company.feature_file_analyser.mocked_tests;

import com.company.feature_file_data_appender.unit_tests.config.TestBase_Mocked;
import org.junit.Test;

import static org.mockito.Mockito.when;

public class ExampleMockedTests extends TestBase_Mocked {

    @Test
    public void mockitoKeyFeaturesExample() {
        //TODO
        // mock the value returned when calling a specific method
        when(utility.getPartialOutputFilePath()).thenReturn("SampleTestName");

        // verify that the method is called once
        //verify(DemoImpl, times(1)).setTestMethodName(testInfo);
    }
}
