package com.company.gherkin_file_appender.unit_tests.utility.pilot;

import com.company.gherkin_file_appender.config.AppendDataToFeatureFile_Utility;
import com.company.gherkin_file_appender.unit_tests.config.TestBase;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

public class _01_Sample_Data_Tab_Tests extends TestBase {
    private AppendDataToFeatureFile_Utility utility;

    public _01_Sample_Data_Tab_Tests() {
        utility = new AppendDataToFeatureFile_Utility();
        utility.setExcelTab("Sample_Data_Tab");
        utility.readCleanseDataSourceFileInto2DArray("Sample_Data.csv", true);
    }

    @Test
    public void happyPath_RetrieveColumn_WithCleansing() {
        Object[] range = { 0 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","column", range));
    }

    @Test
    public void happyPath_RetrieveColumnRange_WithCleansing() {
        Object[] range = { 1, 3 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","colrange", range));
    }

    @Test
    public void happyPath_RetrieveRow_WithCleansing() {
        Object[] range = { 2 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","row", range));
    }

    @Test
    public void happyPath_RetrieveRowRange_WithCleansing() {
        //utility.cleanseColumn("MarksColumn", "2021", "");
        Object[] range = { 2, 7 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","rowrange", range));
    }

    @Test
    public void filterResultsByMapUsingPredicate_FilteredList() {
        String value = "Column 1";
        Predicate<String> predStringEquals = s -> (s.equalsIgnoreCase(value));
        Object[] args = {  predStringEquals, 0 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","filtered_list", args));
    }

    @Test
    public void filterResultsByMapUsingPredicate_FilteredMapByCol() {
        String value = "Column 1";
        Predicate<String> predStringEquals = s -> (s.equalsIgnoreCase(value));
        Object[] args = {  predStringEquals, 0, 1 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","filtered_map_by_col", args));
    }

    @Test
    public void filterResultsByMapUsingPredicate_FilteredMapByColRange() {
        String value = "Column 1";
        Predicate<String> predStringEquals = s -> (s.equalsIgnoreCase(value));
        Object[] args = {  predStringEquals, 0, 1, 2 };
        assertTrue(utility.copyFeatureFile("demo.feature"));
        assertTrue(utility.appendDataToNewFeatureFile("outline","filtered_map_by_colrange", args));

    }
}