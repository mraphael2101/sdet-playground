package com.company.gherkin_file_appender.unit_tests.utility.capabilities;

import com.company.gherkin_file_appender.unit_tests.config.TestBase;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;

public class _02_GetDataSubsetFromInputFileTests extends TestBase {

    @Test
    public void retrieveSingleRowFromInputFileTest_WithCleansing() {
        caps_utility.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", false);
        print(caps_utility.getSpecificRowFromInputFile2DArray(4));
        assertEquals("|Row 4 Col 1|Row 4 Col 2|Row 4 Col 3|Row 4 Col 4|",
                caps_utility.getSpecificRowFromInputFile2DArray(4));
    }

    @Test
    public void retrieveRowRangeFromInputFileTest_WithCleansing(){
        caps_utility.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", false);
        String[][] expected2DArr = initExpected2DArr(2,4);
        String[][] actual2DArr = caps_utility.getRowRangeFromInputFile2DArray(0,1);
        print(expected2DArr);
        print(actual2DArr);
        assertThat(actual2DArr).isNotSameAs(expected2DArr);  // compares both object refs
        assertThat(Arrays.deepEquals(expected2DArr, actual2DArr)).isTrue();
    }

    @Test
    public void retrieveSingleColumnFromInputFileTest_WithCleansing() {
        caps_utility.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", false);

        print(caps_utility.getSpecificColumnFromInputFile2DArray(0));
        assertEquals("[Column 1, Row 1 Col 1, Row 2 Col 1, Row 3 Col 1, Row 4 Col 1, Row 5 Col 1, Row 6 Col 1, Row 7 Col 1]",
                caps_utility.getSpecificColumnFromInputFile2DArray(0).toString());

        print(caps_utility.getSpecificColumnFromInputFile2DArray(3));
        assertEquals("[ Column 4,  Row 1 Col 4,  Row 2 Col 4,  Row 3 Col 4,  Row 4 Col 4,  Row 5 Col 4,  Row 6 Col 4,  Row 7 Col 4]",
                caps_utility.getSpecificColumnFromInputFile2DArray(3).toString());
    }

    @Test
    public void retrieveColumnRangeFromInputFileTest_WithCleansing() {
        caps_utility.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", false);
        String[][] expected2DArr = initExpected2DArr(8,4);
        String[][] actual2DArr = caps_utility.getColumnRangeFromInputFile2DArray(0,3);
        print(expected2DArr);
        print(actual2DArr);
        assertThat(actual2DArr).isNotSameAs(expected2DArr);  // compares both object refs
        assertThat(Arrays.deepEquals(expected2DArr, actual2DArr)).isTrue();
    }

}