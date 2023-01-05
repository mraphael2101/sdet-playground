package com.company.gherkin_file_appender.unit_tests.utility.capabilities;

import com.company.gherkin_file_appender.config.AppendDataToFeatureFile_Utility;
import com.company.gherkin_file_appender.config.ResultSelection;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.lang.Double.parseDouble;
import static java.lang.System.out;

public class _03_PredicateFilterTests {
    AppendDataToFeatureFile_Utility pf_utility;

    String[][] array2d = {
            { "Concorde",     "74990.90",   "GBP" },
            { "Boeing 777",   "99990.90",   "EUR" },
            { "Airbus A380",  "149990.90",  "EUR" },
            { "Private Jet",  "60000",      "USD" }
    };

    @Before
    public void beforeTest() {
        pf_utility = new AppendDataToFeatureFile_Utility();
    }

    @Test
    public void filterResultsUsingPredicateByList_StringContains() {
        String value = "Concorde";
        Predicate<String> predStringContains = s -> (s.contains(value));
        List<ResultSelection> resultList = pf_utility.filterRowsByList(array2d, predStringContains, 0);
        resultList.forEach(out::println);
    }

    @Test
    public void filterResultsUsingPredicateByList_DoubleLessThan() {
        double value = 100000;
        Predicate<String> predDoubleLessThan = s -> (parseDouble(s) < value);
        List<ResultSelection> resultList = pf_utility.filterRowsByList(array2d, predDoubleLessThan, 1);
        resultList.forEach(out::println);
    }

    @Test
    public void filterResultsUsingPredicateByMap_GetSingleMapVal_DoubleGreaterThan() {
        double value = 75000;
        Predicate<String> predDoubleGreaterThan = s -> (parseDouble(s) > value);
        Map<Integer, String[]> resultMap = pf_utility.filterRowsByMap(array2d, predDoubleGreaterThan, 1);
        resultMap.forEach((k, v) -> out.format("%s: %n", (v[1])));
    }

    @Test
    public void filterResultsUsingPredicateByMap_GetKVPair_DoubleGreaterThan() {
        double value = 75000;
        Predicate<String> predDoubleGreaterThan = s -> (parseDouble(s) > value);
        Map<Integer, String[]> resultMap = pf_utility.filterRowsByMap(array2d, predDoubleGreaterThan, 1);
        resultMap.forEach((k, v) -> out.format("%d: %s%n", k, Arrays.toString(v)));
    }

    @Test
    public void filterResultsUsingPredicateByMap_GetMapValColRange_StringContains() {
        Predicate<String> predStringEquals = s -> (s.contains("o"));
        Map<Integer, String[]> resultMap = pf_utility.filterRowsByMap(array2d, predStringEquals, 0);
        resultMap.forEach((k, v) -> out.format(v[0] + " " + v[1] + System.lineSeparator()));
    }
}