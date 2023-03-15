package com.company.feature_file_analyser.core;

import com.company.feature_file_analyser.core.custom_types.Step;
import com.company.feature_file_analyser.core.file_manipulation.FilesReader;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/* Algorithm
   a) List all the Feature files in the specified Path
   b) Read-in all the feature files sequentially

    TBU after refactor

   e) Count the total number of steps that were not reused one or more times
   f) Print Low-level Summary
   g) Print High-level Summary
   h) Print Summary based on Thresholds
*/

@Slf4j
public class Analyser extends FilesReader {
    public Analyser(String inputFilePath) {
        super(inputFilePath);
    }

    public void calculateCodeReuseForAtdd() {

        extractFeatureFilesScenarioTypesAndStepsIncludesInline();
        log.info("Project data successfully extracted");

        enrichData();
        log.info("Project data successfully enriched");

        analyseData();
        log.info("Project data successfully analysed");

    }

    private void analyseData() {

        //sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps();

        int recurrenceCount = 0;
        int distinctStepsSize = metrics.getSetOfDistinctStepNames().size();
        for (int i = 0; i < distinctStepsSize; i++) {
            recurrenceCount = metrics.getOverallNoOfReusedStepsOneOrMoreTimes();
            metrics.setOverallNoOfReusedStepsOneOrMoreTimes(recurrenceCount +=
                    countStepRecurrences(metrics.getSetOfDistinctStepNames().toArray()[i].toString()));
        }

        // Populate the total number of Scenarios/Outlines in every Feature File
        metrics.initialiseSetOfDistinctPathsString();
        int noOfFiles = metrics.getSetOfDistinctFilePaths().size();
        for (int i = 0; i < noOfFiles;  i++) {
            listOfAllFeatureFiles.get(i).setTotalNoOfStepsInFile(listOfAllSteps.size());
            listOfAllFeatureFiles.get(i).setScenarioRecurrenceCount(i);
            listOfAllFeatureFiles.get(i).setScenarioOutlineRecurrenceCount(i);
        }

    }
    private long countStepRecurrences(String step) {
        long count = listOfAllSteps.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(step))
                .count();
        // 1st recurrence is considered when a step is encountered more than once
        if (count >= 1) {
            count -= 1;
        }
        return count;
    }
    private void calculateImpactOfBackgroundKeyword() {
        //TODO for each step that isBackground
        // multiply 1 x no of occurrences of scenario keyword
        long count = listOfAllSteps.stream().distinct()
                .filter(Step::isBackground)
                .count();
    }
    private Map<String, Integer> sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps() {
        int count = 0, i = 0;
        TreeMap<String, Integer> mapResults = new TreeMap<>();
        TreeMap<String, Integer> mapPathsEncountered = new TreeMap<>();

        for (Step step : getStepListIfDataTableDriven()) {
            metrics.addDistinctDataTableDrivenStepName(step.getStepName());
        }

        for (String distinctDataTableDrivenStep : metrics.getListOfDistinctDataTableDrivenStepNames()) {
            mapPathsEncountered.put(distinctDataTableDrivenStep, 0);
        }

        for (Step step : getStepListIfDataTableDriven()) {
            long recurrences = countSpecificStepDataTableDrivenRecurrences(step.getStepName());
            if (mapPathsEncountered.get(step.getStepName()) == 0) {
                count = 0;
                i = 0;
            }
            if (mapPathsEncountered.get(step.getStepName()) <= recurrences) {
                mapPathsEncountered.put(step.getStepName(), ++i);
//                count += step.getFilePathDataTableDrivenCountForStep();
                mapResults.put(step.getStepName(), count);
            }
        }
        return mapResults;
    }
    private int sumDataTableDrivenRowCountAcrossFilesForSingleParameterisedStep(String stepName) {
        List<Step> filteredObjList = listOfAllSteps.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .toList();
        int count = 0;
        for (Step step : filteredObjList) {
            if (step.isDataTableDriven()) {
//                count += step.getFilePathDataTableDrivenCountForStep();
            }
        }
        return count;
    }
    private int countAllDistinctStepDataTableDrivenRecurrences() {
        List<Step> filteredObjList = listOfAllSteps.stream()
                .filter(Step::isDataTableDriven)
                .toList();
        for (Step step : filteredObjList) {
            utils.getListOfString().add(step.getStepName());
        }
        Set<String> distinctStep = new HashSet<>(utils.getListOfString());
        return distinctStep.size();
    }
    private long countSpecificStepDataTableDrivenRecurrences(String stepName) {
        return listOfAllSteps.stream()
                .filter(Step::isDataTableDriven)
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .count();
    }
    private long countAllStepDataTableDrivenRecurrences() {
        return listOfAllSteps.stream()
                .filter(Step::isDataTableDriven)
                .count();
    }
    private long countStepRecurrencesWithoutReuse(String stepName) {
        long count = listOfAllSteps.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .count();
        // Increment the counter everytime only one recurrence is identified
        if (count == 1) return 1;
        else return 0;
    }
    private List<Step> getStepListIfDataTableDriven() {
        return listOfAllSteps.stream()
                .filter(Step::isDataTableDriven)
                .toList();
    }

}
