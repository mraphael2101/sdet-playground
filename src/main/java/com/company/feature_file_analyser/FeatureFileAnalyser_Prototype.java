package com.company.feature_file_analyser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* Algorithm version 0:
*  1) Read-in all .feature files sequentially
*  2) Append distinct gherkin statements to Map<String, object[]>
      "Given...", Recurrence Count int, DataDriven  boolean
       -> parameterised with "static" variable
       -> data driven via table 1..* signified by "<angular brackets>"
   3) After you have traversed through all files calculate the % of all steps that have
      been reused based on the overall count of all steps
*/

public class FeatureFileAnalyser_Prototype {
    private final String userDir = System.getProperty("user.dir");
    private final String inputFilePath = "/src/test/resources/features/";
    private final TreeMap<String, List<? extends Object>> gherkinStepsCodeReuseMetrics = new TreeMap<>();
    private final List<String> listOfAllGherkinSteps = new ArrayList<>();
    private int count = 0, countForStep = 0;
    private List<String> distinctListOfGherkinSteps = null;

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
        this.readInStepsFromGherkinFiles();
        this.analyseGherkinSteps();
        System.out.println(gherkinStepsCodeReuseMetrics);
    }

    private void analyseGherkinSteps() {
        this.distinctListOfGherkinSteps = new ArrayList<>(new HashSet<>(listOfAllGherkinSteps));

        for (String step : listOfAllGherkinSteps) {
            if (distinctListOfGherkinSteps.contains(step)) {

//               countForStep = (int) getCountForStep(step);



                gherkinStepsCodeReuseMetrics.put(step, new ArrayList<>() {
                    {
                        add(0);
                        add(true);
                    }
                });
            }
        }
    }

    public Map<String, List<? extends Object>> getGherkinStepsCodeReuseMetrics() {
        return this.gherkinStepsCodeReuseMetrics;
    }

    private Object getCountForStep(String key) {
        return gherkinStepsCodeReuseMetrics.get(key).get(0);
    }

    public void printSummary() {
        for (Map.Entry<String, List<? extends Object>> obj : getGherkinStepsCodeReuseMetrics().entrySet()) {
            System.out.println(obj.getKey() + " " + obj.getValue());
        }
    }

}
