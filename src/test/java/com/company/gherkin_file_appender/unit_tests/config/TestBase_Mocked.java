package com.company.gherkin_file_appender.unit_tests.config;

import com.company.gherkin_file_appender.config.AppendDataToFeatureFile_Utility;
import com.company.gherkin_file_appender.unit_tests.config.pojo.sample_data_tab.SampleData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class TestBase_Mocked {
    protected AppendDataToFeatureFile_Utility utility2;
    protected String[][] rows;

    @InjectMocks
    protected AppendDataToFeatureFile_Utility utility;

    @Mock
    protected SampleData sampleData;

    @BeforeClass
    public void beforeTestSuite() {
        MockitoAnnotations.openMocks(this);

    }

    @Before
    public void beforeTest() {
        utility2 = new AppendDataToFeatureFile_Utility();
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

