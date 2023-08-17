package com.company.feature_file_analyser.mocked_tests;

import com.company.feature_file_data_appender.config.AppendDataToFeatureFile;
import com.company.feature_file_data_appender.unit_tests.config.pojo.sample_data_tab.SampleData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class ExampleBase_Mocked {
    protected AppendDataToFeatureFile utility2;
    protected String[][] rows;

    @InjectMocks
    protected AppendDataToFeatureFile utility;

    @Mock
    protected SampleData sampleData;

    @BeforeClass
    public void beforeTestSuite() {
        MockitoAnnotations.openMocks(this);

    }

    @Before
    public void beforeTest() {
        utility2 = new AppendDataToFeatureFile();
        rows = utility2.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", false);

        //String[][] expected2DArr = new SampleData().getSampleData();
    }


    protected void print(String value) {
        System.out.println(value);
    }

    protected void print(String[][] inputFileAsTwoDimArr) {
        System.out.println(Arrays.deepToString(inputFileAsTwoDimArr));
    }

    protected void print(List<String> fileInputRowsList) {
        System.out.println(fileInputRowsList);
    }
}

