package com.company.feature_file_analyser.core;

import com.company.feature_file_analyser.core.custom_types.GenericType;
import com.company.feature_file_analyser.core.custom_types.StepMetaData;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.company.feature_file_analyser.config.constants.Frequency.*;

/* Algorithm
   a) List all the Feature files in the specified Path
   b) Read-in all the feature files sequentially

    TBU after refactor

   e) Count the total number of steps that were not reused one or more times
   f) Print Low-level Summary
   g) Print High-level Summary
   h) Print Summary based on Thresholds
*/

public class FeatureFileAnalyser_Prototype {

    private final String userDir = System.getProperty("user.dir");

    private final List<String> listTempString = new ArrayList<>();
    private final List<StepMetaData> listOfAllStepsMetaData = new ArrayList<>();
    private Set<String> setOfDistinctPathsString = null;

    //    private final Multimap<String, List<? extends Object>> allStepsMetaMultimap = LinkedHashMultimap.create();
    private String inputFilePath = "To be specified at Runtime";
    private StepMetaData stepMetaData = null;
    private GenericType<StepMetaData> genTypeStepMeta = null;
    private Set<String> setOfDistinctStepNames = null;

    private List<String> listOfDistinctDataTableDrivenStepNames = new ArrayList<>();
    private int totalNoOfReusedSteps = 0;
    private int totalNoOfStepsWithoutReuse = 0;
    private int totalNoOfSteps = 0;

    private int totalNoOfDataDrivenSteps = 0;
    private int totalNoOfDataTableDrivenSteps = 0;
    private long stepReuseCount = 0;

    private boolean isDataDriven = false;
    private boolean isDataTableDriven = false;

    private boolean isBackground = false;

    private boolean isScenario = false;

    private int scenarioRecurrenceCount = 0;

    private int dataTableRowCount = 0;

    private float percentage = 0;

    public FeatureFileAnalyser_Prototype(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    private Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException {
        return walk(start, Integer.MAX_VALUE, options);
    }

    private List<Path> listFiles(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;

    }

    public void readDataTableRowCounts() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        listTempString.clear();
        String currentPathString = "";
        int i = 0;
        try {
            while (true) {
                assert paths != null;
                if (!(i < paths.size())) break;
                currentPathString = paths.get(i).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesOfSpecificFile) {
                    if (line.chars().filter(ch -> ch == '|').count() >= 2) {
                        //Read in the number of data table rows excluding the header
                        dataTableRowCount++;
                    }
                }

                // Update the file data table rowcount in List<StepMetaData>
                for (StepMetaData step : listOfAllStepsMetaData) {
                    if (step.getFilePathsDataTableRowCountsMap().containsKey(currentPathString)) {
                        if (dataTableRowCount > 0) {
                            // Exclude the header value
                            step.setFilePathsDataTableRowCountsMap(currentPathString, dataTableRowCount - 1);
                        } else {
                            step.setFilePathsDataTableRowCountsMap(currentPathString, 0);
                        }
                    }
                    step.setDataTableRowCount(step.getFilePathDataTableDrivenCountForStep());
                    listTempString.add(currentPathString);
                }
                i++;
                dataTableRowCount = 0;  // Reset the count for the next file
            }
            setOfDistinctPathsString = new HashSet<>(listTempString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readSteps() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String currentPathString = "";
        String trimmedStringLine = "";
        int i = 0;
        int rowIndex = 1;

        try {
            while (true) {
                assert paths != null;
                if (!(i < paths.size())) break;
                currentPathString = paths.get(i).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                int noOfRowsInFile = allLinesOfSpecificFile.size();
                for (String line : allLinesOfSpecificFile) {
                    trimmedStringLine = line.trim();

                    if (trimmedStringLine.startsWith("Background")) {
                        isBackground = true;
                    }
                    if (trimmedStringLine.contains("Scenario")) {
                        isScenario = true;
                        isBackground = false;
                        scenarioRecurrenceCount += 1;
                    }
                    if (trimmedStringLine.startsWith("Given") || trimmedStringLine.startsWith("When")
                            || trimmedStringLine.startsWith("Then") || line.contains("And")) {

                        stepMetaData = new StepMetaData();
                        genTypeStepMeta = new GenericType<StepMetaData>(stepMetaData);
                        StepMetaData smd = genTypeStepMeta.getObj();

                        smd.setStepName(trimmedStringLine);
                        smd.setFilePathsDataTableRowCountsMap(currentPathString, 0);
                        int index = trimmedStringLine.indexOf(" ");
                        smd.setStepType(trimmedStringLine.substring(0, index));
                        if (!isScenario) {
                            smd.setBackground(isBackground);
                        }
                        listOfAllStepsMetaData.add(smd);
                    }
                    if (rowIndex == noOfRowsInFile) {
                        //TODO for each step update path and scenario recurrence
                        // you cant know scenario recurrence value until reaching EOF
//                        for(StepMetaData step : listOfAllStepsMetaData) {
//                            for (Map.Entry<String, Integer> entry : step.getFilePathsDataTableRowCountsMap().entrySet()) {
//                                step.setFilePathsScenarioCountsMap(entry.getKey(), scenarioRecurrenceCount);
//                            }
//                        }
                    }
                    rowIndex++;
                }
                i++;
                rowIndex = 1;
                isBackground = false;
                isScenario = false;
                scenarioRecurrenceCount = 0;
            }
            totalNoOfSteps = listOfAllStepsMetaData.size();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void calculateCodeReuseAtBddLevel() {
        readSteps();
        readDataTableRowCounts();
        analyseSteps();
    }

    private void analyseSteps() {
        listTempString.clear();

        for (StepMetaData step : listOfAllStepsMetaData) {
            listTempString.add(step.getStepName());
        }
        setOfDistinctStepNames = new HashSet<String>(listTempString);

        for (StepMetaData step : listOfAllStepsMetaData) {
            if (setOfDistinctStepNames.contains(step.getStepName())) {
                isDataDriven = (step.getStepName().chars().filter(ch -> ch == '\'').count() == 2);
                step.setDataDriven(isDataDriven);
                isDataTableDriven = (step.getStepName().contains("<") && step.getStepName().contains(">"));
                step.setDataTableDriven(isDataTableDriven);
            }
        }

        for (int i = 0; i < setOfDistinctStepNames.size(); i++) {
            totalNoOfReusedSteps += countStepRecurrences(setOfDistinctStepNames.toArray()[i].toString());
            totalNoOfStepsWithoutReuse += countStepRecurrencesWithoutReuse(setOfDistinctStepNames.toArray()[i].toString());
        }

        totalNoOfDataDrivenSteps += countAllStepDataDrivenRecurrences();
        totalNoOfDataTableDrivenSteps += countAllStepDataTableDrivenRecurrences();
        sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps();
    }

    //TODO
    private void calculateImpactOfBackgroundKeyword() {
        //TODO for each step that isBackground
        // multiply 1 x no of occurrences of scenario keyword
        long count = listOfAllStepsMetaData.stream().distinct()
                .filter(StepMetaData::isBackground)
                .count();
    }

    private Map<String, Integer> sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps() {
        int count = 0, i = 0;
        TreeMap<String, Integer> mapResults = new TreeMap<>();
        TreeMap<String, Integer> mapPathsEncountered = new TreeMap<>();

        for(StepMetaData step: getStepMetaDataListIfDataTableDriven()){
            listOfDistinctDataTableDrivenStepNames.add(step.getStepName());
        }

        for (String distinctDataTableDrivenStep : listOfDistinctDataTableDrivenStepNames) {
            mapPathsEncountered.put(distinctDataTableDrivenStep, 0);
        }

        for (StepMetaData step : getStepMetaDataListIfDataTableDriven()) {
            long recurrences = countSpecificStepDataTableDrivenRecurrences(step.getStepName());

            if (mapPathsEncountered.get(step.getStepName()) == 0) {
                count = 0;
                i = 0;
            }
            if (mapPathsEncountered.get(step.getStepName()) <= recurrences) {
                mapPathsEncountered.put(step.getStepName(), ++i);
                count += step.getDataTableRowCount();
                mapResults.put(step.getStepName(), count);
            }
        }
        return mapResults;
    }

    private int sumDataTableDrivenRowCountAcrossFilesForSingleParameterisedStep(String stepName) {
        List<StepMetaData> filteredObjList = listOfAllStepsMetaData.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .toList();
        int count = 0;
        for (StepMetaData step : filteredObjList) {
            if (step.isDataTableDriven()) {
                count += step.getFilePathDataTableDrivenCountForStep();
            }
        }
        return count;
    }

    private int countAllDistinctStepDataDrivenRecurrences() {
        List<StepMetaData> filteredObjList = listOfAllStepsMetaData.stream()
                .filter(StepMetaData::isDataDriven)
                .toList();
        listTempString.clear();
        for (StepMetaData step : filteredObjList) {
            listTempString.add(step.getStepName());
        }
        Set<String> distinctStep = new HashSet<>(listTempString);
        return distinctStep.size();
    }

    private int countAllDistinctStepDataTableDrivenRecurrences() {
        List<StepMetaData> filteredObjList = listOfAllStepsMetaData.stream()
                .filter(StepMetaData::isDataTableDriven)
                .toList();
        listTempString.clear();
        for (StepMetaData step : filteredObjList) {
            listTempString.add(step.getStepName());
        }
        Set<String> distinctStep = new HashSet<>(listTempString);
        return distinctStep.size();
    }

    private long countAllStepDataDrivenRecurrences() {
        return listOfAllStepsMetaData.stream()
                .filter(StepMetaData::isDataDriven)
                .count();
    }

    private long countSpecificStepDataTableDrivenRecurrences(String stepName) {
        return listOfAllStepsMetaData.stream()
                .filter(StepMetaData::isDataTableDriven)
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .count();
    }

    private long countAllStepDataTableDrivenRecurrences() {
        return listOfAllStepsMetaData.stream()
                .filter(StepMetaData::isDataTableDriven)
                .count();
    }

    private long countStepRecurrencesWithoutReuse(String stepName) {
        long count = listOfAllStepsMetaData.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .count();
        // Increment the counter everytime only one recurrence is identified
        if (count == 1) return 1;
        else return 0;
    }

    private long countStepRecurrences(String step) {
        long count = listOfAllStepsMetaData.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(step))
                .count();
        // 1st recurrence is considered when a step is encountered more than once
        if (count > 0) {
            count -= 1;
        }
        return count;
    }

    private List<StepMetaData> getStepMetaDataListIfDataTableDriven() {
        return listOfAllStepsMetaData.stream()
                .filter(StepMetaData::isDataTableDriven)
                .toList();
    }


    /**
     * Method which summarises the level of code reuse at a lower-level.
     * For each Gherkin Line, the Reuse Count is printed and whether the step is Data-driven.
     */
    public void printLowLevelSummary() {
        System.out.println("Low Level Summary\n-------------------------------------------------------------------------");
        for (StepMetaData step : listOfAllStepsMetaData) {
            System.out.println("Step { " + step.getStepName()
                    + " } \nFile Path { " + step.getFilePathWhereStepWasIdentified()
                    + " } \nStep Type { " + step.getStepType()
                    + " } \nStep Recurrence Count { " + countStepRecurrences(step.getStepName())
                    + " } \nStep Data driven { " + step.isDataDriven()
                    + " } \nStep DataTable driven { " + step.isDataTableDriven() + " }");

            if (step.isDataTableDriven()) {
                System.out.print("Step DataTable driven Row Count in File { " + step.getDataTableRowCount() + " }\n");
                System.out.print("Step DataTable driven Row Count across Files { "
                        + sumDataTableDrivenRowCountAcrossFilesForSingleParameterisedStep(step.getStepName()) + " }\n");
            }
            System.out.println("-------------------------------------------------------------------------");
        }
    }

    /**
     * Method which calculates and summarises the level of code reuse at a higher-level.
     * The calculation is based on the formula:
     * totalNoOfReusableSteps / (totalNoOfSteps - totalNoOfReusableSteps) * 100
     * i.e. It informs us of the overall level of Code Reuse based on analysing if each
     * step was reused at least once or more
     */
    public void printHighLevelSummary() {
        System.out.println("High Level Summary\n-----------------------------------");
        System.out.println("Total Number of Distinct Steps in the Project { " + setOfDistinctStepNames.size() + " }");
        System.out.println("Total Number of Distinct Data Driven Steps in the Project { " + countAllDistinctStepDataDrivenRecurrences() + " }");
        System.out.println("Total Number of Distinct DataTable Driven Steps in the Project { " + countAllDistinctStepDataTableDrivenRecurrences() + " }");
        System.out.println("Total Number of Steps in the Project { " + totalNoOfSteps + " }");
        System.out.println("Total Number of Steps Reused one or more times { " + totalNoOfReusedSteps + " }");
        System.out.println("Total Number of Steps Not Reused one or more times { " + totalNoOfStepsWithoutReuse + " }");
        System.out.println("Total Number of Data Driven Steps { " + totalNoOfDataDrivenSteps + " }");
        System.out.println("Total Number of DataTable Driven Steps { " + totalNoOfDataTableDrivenSteps + " }");
        if(totalNoOfDataDrivenSteps > 0) {
            System.out.println("DataTable Driven Reuse for Specific Steps");
            for(Map.Entry<String, Integer> entry : sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps().entrySet()) {
                System.out.println("DataTable Driven Reuse { " + entry.getKey() + " { " + entry.getValue() + " } }");
            }
        }



//        percentage = (float) totalNoOfReusedSteps / (totalNoOfSteps - totalNoOfReusedSteps) * 100;
//        System.out.println("Level of Overall Code Reuse based on a Step Recurrence of one or more times { " + String.format("%.0f", percentage) + " % }");
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
            stepReuseCount = countStepRecurrences(distinctStepName);
            if (stepReuseCount != 0) {
                if (stepReuseCount < TEN)
                    lessThanTenCounter += 1;
                else if (stepReuseCount >= TEN && stepReuseCount <= TWENTY)
                    tenToTwentyCounter += 1;
                else if (stepReuseCount >= TWENTY && stepReuseCount <= FIFTY)
                    twentyToFiftyCounter += 1;
                else if (stepReuseCount >= FIFTY && stepReuseCount <= ONE_HUNDRED)
                    fiftyToHundredCounter += 1;
                else if (stepReuseCount >= ONE_HUNDRED && stepReuseCount <= ONE_HUNDRED_AND_FIFTY)
                    oneHundredToOneFiftyCounter += 1;
                else if (stepReuseCount >= ONE_HUNDRED_AND_FIFTY && stepReuseCount <= TWO_HUNDRED)
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
