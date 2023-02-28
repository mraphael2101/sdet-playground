package com.company.feature_file_analyser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.company.feature_file_analyser.Frequency.*;

/* Algorithm
   a) List all the Feature files in the specified Path
   b) Read-in all the feature files sequentially
   c) Initialise stepsCodeReuseMetrics Map based on the distinctListOfSteps values
   d) Traverse listOfAllSteps and update stepsCodeReuseMetrics Map based on the
      number of recurrences, and if the step is data-driven (low-level summary)
   e) Count the total number of steps that were not reused one or more times
   f) Print Low-level Summary
   g) Print High-level Summary
   h) Print Summary based on Thresholds
*/

public class FeatureFileAnalyser_Prototype {
    private final String userDir = System.getProperty("user.dir");
    private final String inputFilePath = "/src/test/resources/feature_file_analyser/features/";

    private final List<String> listOfAllSteps = new ArrayList<>();

    private List<String> distinctListOfGherkinSteps = null;

    private final TreeMap<String, List<? extends Object>> stepsReuseMetrics = new TreeMap<>();
    private int totalNoOfReusedSteps = 0;

    private int totalNoOfStepsWithoutReuse = 0;

    private int totalNoOfSteps = 0;

    private int totalNumberOfDataDrivenSteps = 0;

    private int stepReuseCount = 0;

    private boolean dataDriven = false;

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

    private void readInStepsFromGherkinFiles() {
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

        try {
            while (true) {
                assert paths != null;
                if (!(i < paths.size())) break;
                currentPathString = paths.get(i).toString();
                List<String> allLinesForSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesForSpecificFile) {
                    trimmedStringLine = line.trim();
                    if (trimmedStringLine.startsWith("Given") || trimmedStringLine.startsWith("When")
                            || trimmedStringLine.startsWith("Then") || line.contains("And")) {
                        listOfAllSteps.add(trimmedStringLine);
                    }
                }
                i++;
            }
            totalNoOfSteps = listOfAllSteps.size();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void calculateCodeReuseAtBddLevel() {
        readInStepsFromGherkinFiles();
        analyseGherkinSteps();
    }

    /**
     * Method which populates the gherkinStepsCodeReuseMetrics Map based on the
     * number of occurrences of the step across the different feature files, and
     * whether the step is data-driven or not
     * <pre></pre>
     */
    private void analyseGherkinSteps() {
        distinctListOfGherkinSteps = new ArrayList<>(new HashSet<>(listOfAllSteps));
        for (String distinctListOfGherkinStep : distinctListOfGherkinSteps) {
            stepsReuseMetrics.put(distinctListOfGherkinStep, new ArrayList<>() {
                {
                    add(-1);    // 1st recurrence is considered when a step is encountered for the second time
                    add(false);
                    add("Step Type");
                }
            });
        }

        for (String step : listOfAllSteps) {
            if (distinctListOfGherkinSteps.contains(step)) {
                totalNoOfReusedSteps = (int) getStepReuseCount(step);
                dataDriven = (step.contains("<") && step.contains(">"))
                        || step.chars().filter(ch -> ch == '\'').count() == 2;
                stepsReuseMetrics.put(step, new ArrayList<>() {
                    {
                        add(totalNoOfReusedSteps + 1);
                        add(dataDriven);
                        add(step.substring(0, step.indexOf(' ')));  // Step type
                    }
                });
            }
        }

        for (Map.Entry<String, List<? extends Object>> obj : getStepsReuseMetrics().entrySet()) {
            // Increment the counter everytime you identify a reuse count of 0 for a given step
            if ((int) obj.getValue().get(0) == 0) {
                ++totalNoOfStepsWithoutReuse;
            }
            // Increment the counter everytime you identify a step that is data-driven
            if ((boolean) obj.getValue().get(1)) {
                ++totalNumberOfDataDrivenSteps;
            }
        }
    }

    private Object getStepReuseCount(String key) {
        return stepsReuseMetrics.get(key).get(0);
    }

    private Map<String, List<? extends Object>> getStepsReuseMetrics() {
        return stepsReuseMetrics;
    }

    /**
     * Method which summarises the level of code reuse at a lower-level.
     * For each Gherkin Line, the Reuse Count is printed and whether the step is Data-driven.
     */
    public void printLowLevelSummary() {
        for (Map.Entry<String, List<? extends Object>> obj : getStepsReuseMetrics().entrySet()) {
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
        float percentage = 0;
        for (Map.Entry<String, List<? extends Object>> obj : getStepsReuseMetrics().entrySet()) {
            totalNoOfReusableSteps += (int) obj.getValue().get(0);
        }
        System.out.println("Total Number of Distinct Steps in the Project { " + distinctListOfGherkinSteps.size() + " }");
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
        for (Map.Entry<String, List<? extends Object>> obj : getStepsReuseMetrics().entrySet()) {
            stepReuseCount = (int) obj.getValue().get(0);
            if(stepReuseCount != 0) {
                if(stepReuseCount < TEN)
                    lessThanTenCounter += 1;
                else if(stepReuseCount >= TEN && stepReuseCount <= TWENTY)
                    tenToTwentyCounter += 1;
                else if(stepReuseCount >= TWENTY && stepReuseCount <= FIFTY)
                    twentyToFiftyCounter += 1;
                else if(stepReuseCount >= FIFTY && stepReuseCount <= ONE_HUNDRED)
                    fiftyToHundredCounter += 1;
                else if(stepReuseCount >= ONE_HUNDRED && stepReuseCount <= ONE_HUNDRED_AND_FIFTY)
                    oneHundredToOneFiftyCounter += 1;
                else if(stepReuseCount >= ONE_HUNDRED_AND_FIFTY && stepReuseCount <= TWO_HUNDRED)
                    oneHundredFiftyToTwoHundredCounter += 1;
                else {
                    moreThanTwoHundredCounter += 1;
                }
            }
        }
        System.out.println("Steps Reused < 10 times { " + lessThanTenCounter + " }");
        System.out.println("Steps Reused 10 to 20 times { " + tenToTwentyCounter + " }");
        System.out.println("Steps Reused 20 to 50 times { " + twentyToFiftyCounter + " }");
        System.out.println("Steps Reused 50 to 100 times { " + fiftyToHundredCounter + " }");
        System.out.println("Steps Reused 100 to 150 times { " + oneHundredToOneFiftyCounter + " }");
        System.out.println("Steps Reused 150 to 200 times { " + oneHundredFiftyToTwoHundredCounter + " }");
        System.out.println("Steps Reused > 200 times { " + moreThanTwoHundredCounter + " }");
    }
}
