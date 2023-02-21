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
   c) Initialise gherkinStepsCodeReuseMetrics Map based on the distinctListOfGherkinSteps values
   d) Traverse listOfAllGherkinSteps and update gherkinStepsCodeReuseMetrics Map based on the
      number of recurrences, and if the step is data-driven (low-level summary)
   e) Calculate and print the high-level summary of code reuse at a Feature File level
*/

public class FeatureFileAnalyser_Prototype {
    private final String userDir = System.getProperty("user.dir");
    private final String inputFilePath = "/src/test/resources/features/";
    private final TreeMap<String, List<? extends Object>> stepsReuseMetrics = new TreeMap<>();
    private final List<String> listOfAllGherkinSteps = new ArrayList<>();
    private  int countForStep = 0;

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
                        listOfAllGherkinSteps.add(trimmedStringLine);
                    }
                }
                i++;
            }
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
        List<String> distinctListOfGherkinSteps = new ArrayList<>(new HashSet<>(listOfAllGherkinSteps));
        for (String distinctListOfGherkinStep : distinctListOfGherkinSteps) {
            stepsReuseMetrics.put(distinctListOfGherkinStep, new ArrayList<>() {
                {
                    add(0);
                    add(false);
                }
            });
        }
        for (String step : listOfAllGherkinSteps) {
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

    public void printSummary() {
        for (Map.Entry<String, List<? extends Object>> obj : getStepsReuseMetrics().entrySet()) {
            System.out.println(obj.getKey() + " " + obj.getValue());
        }
    }

}
