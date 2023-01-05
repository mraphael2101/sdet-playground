package com.company.gherkin_file_appender.unit_tests.config;

import com.company.gherkin_file_appender.config.AppendDataToFeatureFile_Utility;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

public class TestBase {

    protected AppendDataToFeatureFile_Utility caps_utility;
    protected String[][] rows;

    @Before
    public void beforeTest() {
        caps_utility = new AppendDataToFeatureFile_Utility();
        caps_utility.setExcelTab("Sample_Data_Tab");
        rows = caps_utility.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", false); }

    protected String[][] initExpected2DArr(int rowSize, int colSize) {
        String[][] sampleData = new String[rowSize][colSize];
        int rowTemp = 1;
        int colTemp = 1;

        for(int c = 0; c < colSize; c++) {
            sampleData[0][c] = String.format("Column %d", c + colTemp);
        }

        for(int r = 1; r < rowSize; r++) {
            for(int c = 0; c < colSize; c++) {
                sampleData[r][c] = String.format("Row %d Col %d", rowTemp,  colTemp++);
            }
            rowTemp += 1;
            colTemp = 1;
        }
        return sampleData;
    }

    protected void print(String value) {
        System.out.println(value);
        System.out.println("");
    }

    protected void print(String[][] inputFileAsTwoDimArr) {
        System.out.println(Arrays.deepToString(inputFileAsTwoDimArr));
        System.out.println("");
    }

    protected void print(List<String> fileInputRowsList) {
        System.out.println(fileInputRowsList);
        System.out.println("");
    }

}
