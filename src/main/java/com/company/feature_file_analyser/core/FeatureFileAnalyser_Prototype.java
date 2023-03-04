package com.company.feature_file_analyser.core;

import com.company.feature_file_analyser.core.custom_types.GenericType;
import com.company.feature_file_analyser.core.custom_types.StepMeta;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.company.feature_file_analyser.config.Constants.Frequency.*;

/* Algorithm
   a) List all the Feature files in the specified Path
   b) Read-in all the feature files sequentially
   c) Initialise stepsCodeReuseMetrics Map based on the distinctListOfSteps values
   d) Traverse listOfAllSteps and update stepsCodeReuseMetrics Map based on the
      number of recurrences, and if the step is data-driven
   e) Count the total number of steps that were not reused one or more times
   f) Print Low-level Summary
   g) Print High-level Summary
   h) Print Summary based on Thresholds
*/

public class FeatureFileAnalyser_Prototype {

    private String inputFilePath = "To be specified at Runtime";

    private final String userDir = System.getProperty("user.dir");

    private StepMeta stepMeta = null;

    private GenericType<StepMeta> genTypeStepMeta = null;

    private final List<StepMeta> listOfAllStepsMeta = new ArrayList<>();

    private Set<String>  listOfDistinctStepNames = null;

    private final TreeMap<String, Integer> filePathsDataTableRowCountsMap = new TreeMap<>();

    private final Multimap<String, List<? extends Object>> allStepsMetaMultimap = LinkedHashMultimap.create();

    private final TreeMap<String, List<? extends Object>> overallStepMetricsMap = new TreeMap<>();
    private int totalNoOfReusedSteps = 0;

    private int totalNoOfStepsWithoutReuse = 0;

    private int totalNoOfSteps = 0;

    private int totalNumberOfDataDrivenSteps = 0;

    private int stepReuseCount = 0;

    private final List<Object> stepFilePaths = new ArrayList<>();

    private boolean isDataDriven = false;

    private int dataTableRowCount = 0;

    private float percentage = 0;

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

    public FeatureFileAnalyser_Prototype(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public void readDataTableRowCountFromFeatureFiles() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

                if (dataTableRowCount > 0) {
                    filePathsDataTableRowCountsMap.put(currentPathString, dataTableRowCount - 1);
                } else {
                    filePathsDataTableRowCountsMap.put(currentPathString, 0);
                }
                i++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void readStepsFromFeatureFiles() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String currentPathString = "";
        String trimmedStringLine = "";
        int i = 0, j = -1;

        try {
            while (true) {
                assert paths != null;
                if (!(i < paths.size())) break;
                currentPathString = paths.get(i).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesOfSpecificFile) {
                    trimmedStringLine = line.trim();
                    if (trimmedStringLine.startsWith("Given") || trimmedStringLine.startsWith("When")
                            || trimmedStringLine.startsWith("Then") || line.contains("And")) {
                        stepMeta = new StepMeta();
                        genTypeStepMeta = new GenericType<StepMeta>(stepMeta);
                        StepMeta sm = genTypeStepMeta.getObj();
                        sm.setStepName(trimmedStringLine);
                        sm.setFilePaths(currentPathString);
                        listOfAllStepsMeta.add(sm);
                    }
                }
                i++;
            }
            totalNoOfSteps = listOfAllStepsMeta.size();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void calculateCodeReuseAtBddLevel() {
        readStepsFromFeatureFiles();
        readDataTableRowCountFromFeatureFiles();
        analyseSteps();
    }


    //TODO
    private void analyseBackgroundKeywordImpactOnFileSteps() {
    }

    /**
     * Method which populates the gherkinStepsCodeReuseMetrics Map based on the
     * number of occurrences of the step across the different feature files, and
     * whether the step is data-driven or not
     * <pre></pre>
     */

    private void analyseSteps() {
        //TODO Perhaps there is a better way of doing this
        // ArrayList<String> strList = (ArrayList<String>)(ArrayList<?>)(listOfAllStepsMeta);
        List<String> tempList = new ArrayList<>();
        for (StepMeta stepMeta : listOfAllStepsMeta) {
            tempList.add(stepMeta.getStepName());
        }
        listOfDistinctStepNames  = new HashSet<String>(tempList);

        for (String step : listOfDistinctStepNames) {
            overallStepMetricsMap.put(step, new ArrayList<>() {
                {
                    add(-1);    // step reuse count (1st recurrence is considered when a step is encountered for the second time)
                    add(false);
                    add("Step Type");
                    if (allStepsMetaMultimap.get(step).size() > 1) {
                        for (int i = 0; i < allStepsMetaMultimap.get(step).size(); i++) {
                            add("File Name in which the Step is identified");
                        }
                    }
                    add(0); // overall summed data table row count for the different feature files
                }
            });
        }

        for (String step : allStepsMetaMultimap.keys()) {
            if (listOfDistinctStepNames.contains(step)) {
                totalNoOfReusedSteps = (int) getStepReuseCount(step);
                isDataDriven = (step.contains("<") && step.contains(">"))
                        || step.chars().filter(ch -> ch == '\'').count() == 2;
                for (int i = 0; i < allStepsMetaMultimap.get(step).size(); i++) {
                    stepFilePaths.add(allStepsMetaMultimap.get(step).toArray()[i]);
                }
                overallStepMetricsMap.put(step, new ArrayList<>() {
                    {
                        add(totalNoOfReusedSteps + 1);
                        add(isDataDriven);
                        add(step.substring(0, step.indexOf(' ')));  // Step type
                        int i = 0;
                        while (i < allStepsMetaMultimap.get(step).size()) {
                            add(stepFilePaths.get(i));
                            i++;
                        }
                        add(getFilePathsDataTableRowCountsMap());   //TODO in-progress
                    }
                });

                //                        sm.setStepReuseCount(stepMeta.getStepReuseCount() + 1);
            }
        }

        for (Map.Entry<String, List<? extends Object>> obj : getOverallStepMetricsMap().entrySet()) {
            // Increment the counter everytime a reuse count of 0 is identified for a given step
            if ((int) obj.getValue().get(0) == 0) {
                ++totalNoOfStepsWithoutReuse;
            }
            // Increment the counter everytime a step is identified to be data-driven
            if ((boolean) obj.getValue().get(1)) {
                ++totalNumberOfDataDrivenSteps;
            }
        }
    }

    private int sumOverallDataTableRowCountAcrossFeatureFiles(String path) {
        //TODO In progress
        //        getFilePathsDataTableRowCountsMap().get("");
        return 0;
    }

    private Object getStepReuseCount(String key) {
        return this.overallStepMetricsMap.get(key).get(0);
    }

    private Map<String, List<? extends Object>> getOverallStepMetricsMap() {
        return this.overallStepMetricsMap;
    }

    private TreeMap<String, Integer> getFilePathsDataTableRowCountsMap() {
        return this.filePathsDataTableRowCountsMap;
    }


    /**
     * Method which summarises the level of code reuse at a lower-level.
     * For each Gherkin Line, the Reuse Count is printed and whether the step is Data-driven.
     */
    public void printLowLevelSummary() {
        System.out.println("Low Level Summary\n-----------------------------------");
        for (Map.Entry<String, List<? extends Object>> obj : getOverallStepMetricsMap().entrySet()) {
            System.out.println("Step { " + obj.getKey()
                    + " } \nStep Type { " + obj.getValue().get(2)
                    + " } \nReuse Count { " + obj.getValue().get(0)
                    + " } \nData-driven { " + obj.getValue().get(1)
                    + " } \n");
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
        float totalNoOfReusableSteps = 0;
        for (Map.Entry<String, List<? extends Object>> obj : getOverallStepMetricsMap().entrySet()) {
            totalNoOfReusableSteps += (int) obj.getValue().get(0);
        }
        System.out.println("High Level Summary\n-----------------------------------");
        System.out.println("Total Number of Distinct Steps in the Project { " + listOfDistinctStepNames.size() + " }");
        System.out.println("Total Number of Steps in the Project { " + totalNoOfSteps + " }");
        System.out.println("Total Number of Steps Reused one or more times { " + (int) totalNoOfReusableSteps + " }");
        System.out.println("Total Number of Steps Not Reused one or more times { " + totalNoOfStepsWithoutReuse + " }");
        System.out.println("Total Number of Distinct Data Driven Steps { " + totalNumberOfDataDrivenSteps + " }");
        percentage = totalNoOfReusableSteps / (totalNoOfSteps - totalNoOfReusableSteps) * 100;
        System.out.println("Level of Overall Code Reuse based on a Step Recurrence of one or more times { " + String.format("%.0f", percentage) + " % }");
    }

    public void printSummaryWithThresholds() {
        int lessThanTenCounter = 0;
        int tenToTwentyCounter = 0;
        int twentyToFiftyCounter = 0;
        int fiftyToHundredCounter = 0;
        int oneHundredToOneFiftyCounter = 0;
        int oneHundredFiftyToTwoHundredCounter = 0;
        int moreThanTwoHundredCounter = 0;
        for (Map.Entry<String, List<? extends Object>> obj : getOverallStepMetricsMap().entrySet()) {
            stepReuseCount = (int) obj.getValue().get(0);
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
        }
        else if (percentage > 40 && percentage < 60) {
            System.out.println("The Overall Level of Code Reuse is Average");
        }
        else if (percentage >= 60 && percentage < 75) {
            System.out.println("The Overall Level of Code Reuse is Good");
        }
        else  {
            System.out.println("The Overall Level of Code Reuse is Excellent");
        }
    }
}
