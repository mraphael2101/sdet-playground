package com.company.feature_file_analyser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* Algorithm:
   a) List all the Feature files in the specified Path
   b) Read-in all the feature files sequentially
   c) Initialise stepsCodeReuseMetrics Map based on the distinctListOfSteps values
   d) Traverse listOfAllSteps and update stepsCodeReuseMetrics Map based on the
      number of recurrences, and if the step is data-driven (low-level summary)
   e) Calculate and print the high-level summary of code reuse at a Feature File level
   Ideas ->
     - Detect the version of Cucumber
     - Reusability classification based on formula
        a) Code Reusability is Moderate
        b) Code Reusability is Excellent
     - Data-driven using the predefined value from the step definition
     - Complexity based on the no of lines of code in a SD method
*/

public class FeatureFileAnalyser_Prototype {
    private final String userDir = System.getProperty("user.dir");
    private final String inputFilePath = "/src/test/resources/feature_file_analyser/features/";

    private final List<String> listOfAllSteps = new ArrayList<>();

    private List<String> distinctListOfGherkinSteps = null;

    private final TreeMap<String, List<? extends Object>> stepsReuseMetrics = new TreeMap<>();
    private  int countForStep = 0;

    private int totalNoOfSteps = 0;

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
                    // A first recurrence is considered when a step is encountered for the second time
                    add(-1);
                    add(false);
                }
            });
        }
        //TODO after first iteration then plus 1
        for (String step : listOfAllSteps) {
            if (distinctListOfGherkinSteps.contains(step)) {
                countForStep = (int) getStepReuseCount(step);
                dataDriven = step.contains("<") && step.contains(">");
                stepsReuseMetrics.put(step, new ArrayList<>() {
                    {
                        add(countForStep + 1);
                        add(dataDriven);
                    }
                });
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
                    + " } \nReuse Count { " + obj.getValue().get(0)
                    + " } \nData-driven { " + obj.getValue().get(1)
                    + " } \n");
        }
    }


    /**
     * Method which calculates and summarises the level of code reuse at a higher-level.
     * The calculation is based on the formula:
     * totalNoOfReusableSteps / (totalNoOfSteps - totalNoOfReusableSteps) * 100
     */
    public void printHighLevelSummary() {
        float totalNoOfReusableSteps = 0;
        float percentage = 0;
        for (Map.Entry<String, List<? extends Object>> obj : getStepsReuseMetrics().entrySet()) {
            totalNoOfReusableSteps += (int) obj.getValue().get(0);
        }
        System.out.println("Total Number of Steps Reused in the Project { " + (int) totalNoOfReusableSteps + " }");
        System.out.println("Total Number of Steps in the Project { " + totalNoOfSteps + " }");
        percentage = totalNoOfReusableSteps / (totalNoOfSteps - totalNoOfReusableSteps) * 100;
        System.out.println("Code Reuse calculated for all BDD Steps { " + String.format("%.2f", percentage) + " % }");
    }

    public void printSummaryWithThresholds() {
    }

}
