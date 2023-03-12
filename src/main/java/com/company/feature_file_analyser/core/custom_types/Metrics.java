package com.company.feature_file_analyser.core.custom_types;

import com.company.feature_file_analyser.core.file_manipulation.FilesReader;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.company.feature_file_analyser.core.file_manipulation.FilesReader.listOfAllSteps;

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
    private int totalNoOfReusedSteps = 0;
    @Getter
    private int totalNoOfStepsWithoutReuse = 0;
    @Getter
    private int totalNoOfDataDrivenSteps = 0;
    @Getter
    private int totalNoOfDataTableDrivenSteps = 0;
    @Getter
    private long overallStepReuseCount = 0;
    @Getter
    @Setter
    private long totalNoOfSteps = 0;

    Utils utils =  new Utils();

    public void initialiseSetOfDistinctStepNames() {
        List<String> tempList = utils.getListOfString();

        for(Step step : listOfAllSteps) {
            tempList.add(step.getStepName());
        }
        this.setOfDistinctStepNames = new HashSet<>(tempList);
    }

    public void initialiseSetOfDistinctPathsString(List<String> value) {
        this.setOfDistinctFilePaths = new HashSet<>(value);
    }

    public void addDistinctDataTableDrivenStepName(String name) {
        this.listOfDistinctDataTableDrivenStepNames.add(name);
    }

    public void calculatePercentageOfCodeReuse() {
        setPercentage((float) totalNoOfReusedSteps / totalNoOfSteps * 100);
        System.out.println("Level of Overall Code Reuse based on a Step Recurrence of one or more times { "
                + String.format("%.0f", percentage) + " % }");
    }

    public void printLowLevelSummary() {
        System.out.println("Low Level Summary\n-------------------------------------------------------------------------");
        for (Step step : listOfAllSteps) {
            System.out.println("Step { " + step.getStepName()
//                    + " } \nFile Path { " + step.getFilePathOfStep()
                    + " } \nStep Type { " + step.getStepType()
//                    + " } \nStep Recurrence Count { " + countStepRecurrences(step.getStepName())
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
//        System.out.println("Total Number of Distinct Steps in the Project { " + setOfDistinctStepNames.size() + " }");
//        System.out.println("Total Number of Distinct Data Driven Steps in the Project { " + countAllDistinctStepDataDrivenRecurrences() + " }");
//        System.out.println("Total Number of Distinct DataTable Driven Steps in the Project { " + countAllDistinctStepDataTableDrivenRecurrences() + " }");
        System.out.println("Total Number of Steps in the Project { " + totalNoOfSteps + " }");
        System.out.println("Total Number of Steps Reused one or more times { " + totalNoOfSteps + " }");
        System.out.println("Total Number of Steps Not Reused one or more times { " + totalNoOfStepsWithoutReuse + " }");
        System.out.println("Total Number of Data Driven Steps { " + totalNoOfDataDrivenSteps + " }");
//        System.out.println("Total Number of Scenarios TBU { " + scenarioRecurrenceCount + " }");
//        System.out.println("Total Number of Scenario Outlines TBU { " + scenarioOutlineRecurrenceCount + " }");
        if (totalNoOfDataDrivenSteps > 0) {
            System.out.println("DataTable Driven Reuse for Specific Steps");
//            for (Map.Entry<String, Integer> entry : sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps().entrySet()) {
//                System.out.println("DataTable Driven Reuse { " + entry.getKey() + " { " + entry.getValue() + " } }");
//            }
        }
        calculatePercentageOfCodeReuse();
    }

    public void printSummaryWithThresholds() {
        int lessThanTenCounter = 0;
        int tenToTwentyCounter = 0;
        int twentyToFiftyCounter = 0;
        int fiftyToHundredCounter = 0;
        int oneHundredToOneFiftyCounter = 0;
        int oneHundredFiftyToTwoHundredCounter = 0;
        int moreThanTwoHundredCounter = 0;

//        for (String distinctStepName : setOfDistinctStepNames) {
//            stepReuseCount = countStepRecurrences(distinctStepName);
//            if (stepReuseCount != 0) {
//                if (stepReuseCount < TEN)
//                    lessThanTenCounter += 1;
//                else if (stepReuseCount >= TEN && stepReuseCount <= TWENTY)
//                    tenToTwentyCounter += 1;
//                else if (stepReuseCount >= TWENTY && stepReuseCount <= FIFTY)
//                    twentyToFiftyCounter += 1;
//                else if (stepReuseCount >= FIFTY && stepReuseCount <= ONE_HUNDRED)
//                    fiftyToHundredCounter += 1;
//                else if (stepReuseCount >= ONE_HUNDRED && stepReuseCount <= ONE_HUNDRED_AND_FIFTY)
//                    oneHundredToOneFiftyCounter += 1;
//                else if (stepReuseCount >= ONE_HUNDRED_AND_FIFTY && stepReuseCount <= TWO_HUNDRED)
//                    oneHundredFiftyToTwoHundredCounter += 1;
//                else {
//                    moreThanTwoHundredCounter += 1;
//                }
//            }
//        }
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
