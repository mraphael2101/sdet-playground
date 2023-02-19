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

    private final TreeMap<String, List<? extends Object>> gherkinStepMetrics = new TreeMap<>();

    private final List<String> distinctListOfGherkinSteps = new ArrayList<>();

    private final List<String> listOfAllGherkinSteps = new ArrayList<>();

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

    public void calculateCodeReuseAtBddLevel() {
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
            while (i < paths.size()) {
                currentPathString = paths.get(i).toString();
                List<String> allLinesForSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesForSpecificFile) {
                    analyseGherkinSteps(line);
                }
                i++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for(Map.Entry<String, List<? extends Object>> obj : gherkinStepMetrics.entrySet()) {
            System.out.println(obj.getKey() + " " + obj.getValue());
        }
    }

    private void analyseGherkinSteps(String line) {
//        if (distinctListOfGherkinSteps) {
//
//            // add business logic
//
//            // for this one update the values or insert new record
//            gherkinStepMetrics.put(line, new ArrayList<>(){ { add(3); add(true); } });
//
//            // for this one append value
//            distinctListOfGherkinSteps.add(line);
//
//        }

    }

}
