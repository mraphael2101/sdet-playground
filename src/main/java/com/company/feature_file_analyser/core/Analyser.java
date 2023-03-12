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

    //private final Multimap<String, List<? extends Object>> allStepsMetaMultimap = LinkedHashMultimap.create();

    public Analyser(String inputFilePath) {
        super(inputFilePath);
    }

    public void calculateCodeReuseAtAcceptanceTestLevel() {
        readFeatureFile_Path__And__Step_NameLineIndex();
        readScenariosAndOutlines_PathNameLineIndex();
        readKeywords();
        analyseData();
        log.info("Project data successfully analysed");
//        var result = countTotalNumberOfScenarioRecurrences();
//        var result2 = countTotalNumberOfScenarioOutlineRecurrences();
//        System.out.println("here");
    }

    private void analyseData() {
        listTempString.clear();

        for (Step step : listOfAllSteps) {
            listTempString.add(step.getStepName());
        }

        metrics.setSetOfDistinctStepNames(new HashSet<String>(listTempString));

        for (Step step : listOfAllSteps) {
            if (metrics.getSetOfDistinctStepNames().contains(step.getStepName())) {
                step.setDataDriven((step.getStepName().chars().filter(ch -> ch == '\'').count() == 2
                        || step.getStepName().chars().filter(ch -> ch == '\"').count() == 2));
                step.setDataTableDriven(step.getStepName().contains("<") && step.getStepName().contains(">"));
            }
        }

        countOverallStepRecurrences();
        countOverallStepRecurrencesWithoutReuse();
        sumDataTableDrivenRowCountAcrossFilesForAllParameterisedSteps();

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

        for (Step step : getStepMetaDataListIfDataTableDriven()) {
            metrics.addDistinctDataTableDrivenStepName(step.getStepName());
        }

        for (String distinctDataTableDrivenStep : metrics.getListOfDistinctDataTableDrivenStepNames()) {
            mapPathsEncountered.put(distinctDataTableDrivenStep, 0);
        }

        for (Step step : getStepMetaDataListIfDataTableDriven()) {
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

    private int countAllDistinctStepDataDrivenRecurrences() {
        List<Step> filteredObjList = listOfAllSteps.stream()
                .filter(Step::isDataDriven)
                .toList();
        listTempString.clear();
        for (Step step : filteredObjList) {
            listTempString.add(step.getStepName());
        }
        Set<String> distinctStep = new HashSet<>(listTempString);
        return distinctStep.size();
    }

    private int countAllDistinctStepDataTableDrivenRecurrences() {
        List<Step> filteredObjList = listOfAllSteps.stream()
                .filter(Step::isDataTableDriven)
                .toList();
        listTempString.clear();
        for (Step step : filteredObjList) {
            listTempString.add(step.getStepName());
        }
        Set<String> distinctStep = new HashSet<>(listTempString);
        return distinctStep.size();
    }

    private long countAllStepDataDrivenRecurrences() {
        return listOfAllSteps.stream()
                .filter(Step::isDataDriven)
                .count();
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

    private long countOverallStepRecurrencesWithoutReuse(String stepName) {
        long count = listOfAllSteps.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                .count();
        // Increment the counter everytime only one recurrence is identified
        if (count == 1) return 1;
        else return 0;
    }

    private long countOverallStepRecurrences(String step) {
        long count = listOfAllSteps.stream()
                .filter(s -> s.getStepName().equalsIgnoreCase(step))
                .count();
        // 1st recurrence is considered when a step is encountered more than once
        if (count > 1) {
            count -= 1;
        }
        return count;
    }

    private long countOverallStepRecurrencesWithoutReuse() {
        long count = 0, result = 0;
        for(String stepName : metrics.getSetOfDistinctStepNames()) {
            count = listOfAllSteps.stream()
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

    private long countOverallStepRecurrences() {
        long count = 0;
        for(String stepName : metrics.getSetOfDistinctStepNames()) {
            count += listOfAllSteps.stream()
                    .filter(s -> s.getStepName().equalsIgnoreCase(stepName))
                    .count();
            // 1st recurrence is considered when a step is encountered more than once
            if (count > 1) {
                count -= 1;
            }
        }
        return count;
    }

    private long countTotalNumberOfScenarioRecurrences() {
        List<Step> filteredList = listOfAllSteps.stream()
//                .filter(s -> s.getScenarioRecurrenceCountForStep() > 0)
                .toList();
        return filteredList.stream()
//                .filter(s -> s.getScenarioRecurrenceCountForStep() > 0)
                .count();
    }

    private long countTotalNumberOfScenarioOutlineRecurrences() {
        List<Step> filteredList = listOfAllSteps.stream()
//                .filter(s -> s.getScenarioOutlineRecurrenceCountForStep() > 0)
                .toList();
        return filteredList.stream()
//                .filter(s -> s.getScenarioOutlineRecurrenceCountForStep() > 0)
                .count();
    }

    private List<Step> getStepMetaDataListIfDataTableDriven() {
        return listOfAllSteps.stream()
                .filter(Step::isDataTableDriven)
                .toList();
    }

}
