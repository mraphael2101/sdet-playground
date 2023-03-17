package com.company.feature_file_analyser.core;

import com.company.feature_file_analyser.core.custom_types.FeatureFile;
import com.company.feature_file_analyser.core.custom_types.Step;
import com.company.feature_file_analyser.core.custom_types.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.company.feature_file_analyser.core.constants.Frequency.*;
import static com.company.feature_file_analyser.core.file_parser.FilesReader.LIST_OF_ALL_FEATURE_FILES;
import static com.company.feature_file_analyser.core.file_parser.FilesReader.LIST_OF_ALL_STEPS;

@Slf4j
public class Metrics {
    @Getter
    @Setter
    private Set<String> setOfDistinctFilePaths = null;
    @Getter
    @Setter
    private Set<String> setOfDistinctStepNames = null;
    @Getter
    @Setter
    private List<String> listOfDistinctDataTableDrivenStepNames = new ArrayList<>();
    @Getter
    @Setter
    private float percentage = 0;
    @Getter
    @Setter
    private long overallScenarioRecurrencesCount = 0;
    @Getter
    @Setter
    private long overallScenarioOutlineRecurrencesCount = 0;
    @Getter
    @Setter
    private int overallNoOfReusedStepsOneOrMoreTimes = 0;
    @Getter
    private final int overallNoOfStepsWithoutReuse = 0;
    @Getter
    private final int overallNoOfDataDrivenSteps = 0;
    @Getter
    private final int overallNoOfDataTableDrivenSteps = 0;
    @Getter
    @Setter
    private long overallNoOfSteps = 0;
    Utils utils =  new Utils();

    public void initialiseSetOfDistinctStepNames() {
        utils.clearListOfString();
        List<String> tempList = utils.getListOfString();

        for(Step step : LIST_OF_ALL_STEPS) {
            tempList.add(step.getStepName());
        }
        this.setOfDistinctStepNames = new HashSet<>(tempList);
    }
    public void initialiseSetOfDistinctPathsString() {
        utils.clearListOfString();
        List<String> tempList = utils.getListOfString();

        for(FeatureFile file : LIST_OF_ALL_FEATURE_FILES) {
            tempList.add(file.getPath());
        }
        this.setOfDistinctFilePaths = new HashSet<>(tempList);
    }
    public void addDistinctDataTableDrivenStepName(String name) {
        this.listOfDistinctDataTableDrivenStepNames.add(name);
    }
    public void calculatePercentageForReusedStepsOneOrMoreTimes() {
        setPercentage((float) overallNoOfReusedStepsOneOrMoreTimes / overallNoOfSteps * 100);
        System.out.println("Level of Overall Code Reuse based on a Step Recurrence of one or more times { "
                + String.format("%.0f", percentage) + " % }");
    }
    private long countStepRecurrences(String step) {
        long count = LIST_OF_ALL_STEPS.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(step))
                .count();
        // 1st recurrence is considered when a step is encountered more than once
        if (count >= 1) {
            count -= 1;
        }
        return count;
    }
    private long countStepRecurrencesWithoutReuse() {
        long count = 0, result = 0;
        for(String stepName : setOfDistinctStepNames) {
            count = LIST_OF_ALL_STEPS.stream()
                    .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                    .count();
            // Increment the counter everytime only one recurrence is identified
            if (count == 1) {
                result += 1;
            }
            else {
                result += 0;
            }
        }
        return result;
    }
    private long countAllStepDataDrivenRecurrences() {
        return LIST_OF_ALL_STEPS.stream()
                .filter(Step::isDataDriven)
                .count();
    }
    private int countAllDistinctStepDataDrivenRecurrences() {
        List<Step> filteredObjList = LIST_OF_ALL_STEPS.stream()
                .filter(Step::isDataDriven)
                .toList();
        utils.clearListOfString();
        for (Step step : filteredObjList) {
            utils.addString(step.getStepName());
        }
        Set<String> distinctStep = new HashSet<>(utils.getListOfString());
        return distinctStep.size();
    }

//    private long countAllStepDataTableDrivenRecurrences() {}

//    private int countAllDistinctStepDataTableDrivenRecurrences() {}
    private long countTotalNumberOfScenarioRecurrences() {
        long count = 0;
        List<FeatureFile> filteredList = LIST_OF_ALL_FEATURE_FILES.stream()
                .filter(f -> f.getScenarioRecurrenceCount() > 0)
                .toList();
        for(FeatureFile file : LIST_OF_ALL_FEATURE_FILES) {
            count += file.getScenarioRecurrenceCount();
        }
        setOverallScenarioRecurrencesCount(count);
        return count;
    }
    private long countTotalNumberOfScenarioOutlineRecurrences() {
        long count = 0;
        List<FeatureFile> filteredList = LIST_OF_ALL_FEATURE_FILES.stream()
                .filter(f -> f.getScenarioOutlineRecurrenceCount() > 0)
                .toList();
        for(FeatureFile file : LIST_OF_ALL_FEATURE_FILES) {
            count += file.getScenarioOutlineRecurrenceCount();
        }
        setOverallScenarioOutlineRecurrencesCount(count);
        return count;
    }
    public void printLowLevelSummary() {
        System.out.println("Low Level Summary\n-------------------------------------------------------------------------");
        for (Step step : LIST_OF_ALL_STEPS) {
            System.out.println("Step { " + step.getStepName()
                    + " } \nFile Path { " + step.getPath()
                    + " } \nStep Type { " + step.getStepType()
                    + " } \nStep Recurrence Count { " + countStepRecurrences(step.getStepName())
                    + " } \nStep Data driven { " + step.isDataDriven()
                    + " } \nStep DataTable driven { " + step.isDataTableDriven() + " }");

            if (step.isDataTableDriven()) {
//                System.out.print("Step DataTable driven Row Count in File { " + step.getFilePathDataTableDrivenCountForStep() + " }\n");
//                System.out.print("Step DataTable driven Row Count across Files { "
//                        + sumDataTableDrivenRowCountAcrossFilesForSingleParameterisedStep(step.getStepName()) + " }\n");
            }
            System.out.println("-------------------------------------------------------------------------");
        }
    }
    public void printHighLevelSummary() {
        System.out.println("High Level Summary\n-----------------------------------");
        System.out.println("Total Number of Distinct Steps in the Project { " + setOfDistinctStepNames.size() + " }");
        System.out.println("Total Number of Distinct Data Driven Steps in the Project { " + countAllDistinctStepDataDrivenRecurrences() + " }");
//        System.out.println("Total Number of Distinct DataTable Driven Steps in the Project { " + countAllDistinctStepDataTableDrivenRecurrences() + " }");
        System.out.println("Total Number of Steps in the Project { " + overallNoOfSteps + " }");
        System.out.println("Total Number of Steps Reused one or more times { " + overallNoOfReusedStepsOneOrMoreTimes + " }");
        System.out.println("Total Number of Steps Not Reused one or more times { " + countStepRecurrencesWithoutReuse() + " }");
        System.out.println("Total Number of Data Driven Steps { " + countAllStepDataDrivenRecurrences() + " }");
        System.out.println("Total Number of Scenarios { " + countTotalNumberOfScenarioRecurrences() + " }");
        System.out.println("Total Number of Scenario Outlines { " + countTotalNumberOfScenarioOutlineRecurrences() + " }");
        if (overallNoOfDataDrivenSteps > 0) {
            System.out.println("DataTable Driven Reuse for Specific Steps");
//            for (Map.Entry<String, Integer> entry : sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps().entrySet()) {
//                System.out.println("DataTable Driven Reuse { " + entry.getKey() + " { " + entry.getValue() + " } }");
//            }
        }
        calculatePercentageForReusedStepsOneOrMoreTimes();
    }
    public void printHighLevelSummaryAtFeatureFileLevel() {
        for(FeatureFile file : LIST_OF_ALL_FEATURE_FILES) {
            System.out.println(file.getPath());
            System.out.println(file.getListOfScenarios());
            System.out.println(file.getListOfScenarioOutlines());
            System.out.println(file.getTotalNoOfScenarios());
            System.out.println(file.getTotalNoOfScenarioOutlines());
            System.out.println(file.getTotalNoOfStepsInFile());
        }
    }
    public void printSummaryWithThresholds() {
        int lessThanTenCounter = 0;
        int tenToTwentyCounter = 0;
        int twentyToFiftyCounter = 0;
        int fiftyToHundredCounter = 0;
        int oneHundredToOneFiftyCounter = 0;
        int oneHundredFiftyToTwoHundredCounter = 0;
        int moreThanTwoHundredCounter = 0;

        for (String distinctStepName : setOfDistinctStepNames) {
            long count = countStepRecurrences(distinctStepName);
            if (count != 0) {
                if (count < TEN)
                    lessThanTenCounter += 1;
                else if (count >= TEN && count <= TWENTY)
                    tenToTwentyCounter += 1;
                else if (count >= TWENTY && count <= FIFTY)
                    twentyToFiftyCounter += 1;
                else if (count >= FIFTY && count <= ONE_HUNDRED)
                    fiftyToHundredCounter += 1;
                else if (count >= ONE_HUNDRED && count <= ONE_HUNDRED_AND_FIFTY)
                    oneHundredToOneFiftyCounter += 1;
                else if (count >= ONE_HUNDRED_AND_FIFTY && count <= TWO_HUNDRED)
                    oneHundredFiftyToTwoHundredCounter += 1;
                else {
                    moreThanTwoHundredCounter += 1;
                }
            }
        }
        System.out.println("\nSummary based on Thresholds\n-----------------------------------");
        System.out.println("Steps Reused < 10 times { " + lessThanTenCounter + " }");
        System.out.println("Steps Reused 10 to 20 times { " + tenToTwentyCounter + " }");
        System.out.println("Steps Reused 20 to 50 times { " + twentyToFiftyCounter + " }");
        System.out.println("Steps Reused 50 to 100 times { " + fiftyToHundredCounter + " }");
        System.out.println("Steps Reused 100 to 150 times { " + oneHundredToOneFiftyCounter + " }");
        System.out.println("Steps Reused 150 to 200 times { " + oneHundredFiftyToTwoHundredCounter + " }");
        System.out.println("Steps Reused > 200 times { " + moreThanTwoHundredCounter + " }");
    }
    public void printCodeReuseLevelClassification() {
        System.out.println("\n-----------------------------------");
        if (percentage <= 40) {
            System.out.println("The Overall Level of Code Reuse is Poor");
        } else if (percentage > 40 && percentage < 60) {
            System.out.println("The Overall Level of Code Reuse is Average");
        } else if (percentage >= 60 && percentage < 75) {
            System.out.println("The Overall Level of Code Reuse is Good");
        } else {
            System.out.println("The Overall Level of Code Reuse is Excellent");
        }
    }

}
