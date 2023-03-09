package com.company.feature_file_analyser.core.file_manipulation;

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

public class FilesReader {
    private final String userDir = System.getProperty("user.dir");
    private String inputFilePath = "To be specified at Runtime";
    protected final List<String> listTempString = new ArrayList<>();
    protected StepMetaData stepMetaData = null;
    protected GenericType<StepMetaData> genTypeStepMeta = null;
    protected final List<StepMetaData> listOfAllStepsMetaData = new ArrayList<>();
    protected Set<String> setOfDistinctPathsString = null;
    protected boolean isDataDriven = false;
    protected boolean isDataTableDriven = false;
    protected boolean isBackground = false;
    protected boolean isScenario = false;
    protected int totalNoOfSteps = 0;
    protected long stepReuseCount = 0;
    protected int scenarioRecurrenceCount = 0;
    protected int scenarioOutlineRecurrenceCount = 0;
    protected int dataTableRowCount = 0;

    private Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException {
        return walk(start, Integer.MAX_VALUE, options);
    }

    public FilesReader(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    private List<Path> listFiles(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;

    }

    protected void readDataTableRowCounts() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        listTempString.clear();
        String currentPathString = "";
        String trimmedStringLine = "";
        int i = 0;
        try {
            while (true) {
                assert paths != null;
                if (!(i < paths.size())) break;
                currentPathString = paths.get(i).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesOfSpecificFile) {
                    trimmedStringLine = line.trim();
                    if (line.chars().filter(ch -> ch == '|').count() >= 2) {
                        //Read in the number of data table rows excluding the header
                        dataTableRowCount++;
                    }
                    if (trimmedStringLine.contains("Scenario:")) {
                        scenarioRecurrenceCount++;
                    }
                    if (trimmedStringLine.contains("Scenario Outline:")) {
                        scenarioOutlineRecurrenceCount++;
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
                    if (step.getFilePathsScenarioCountsMap().containsKey(currentPathString)) {
                        step.setFilePathsScenarioCountsMap(currentPathString, scenarioRecurrenceCount);
                    }
                    if(step.getFilePathsScenarioOutlineCountsMap().containsKey(currentPathString)) {
                        step.setFilePathsScenarioOutlineCountsMap(currentPathString, scenarioOutlineRecurrenceCount);
                    }
                    step.setDataTableRowCount(step.getFilePathDataTableDrivenCountForStep());
                    listTempString.add(currentPathString);
                }
                i++;
                dataTableRowCount = 0;  // Reset the count for the next file
                scenarioRecurrenceCount = 0;
                scenarioOutlineRecurrenceCount = 0;
            }
            setOfDistinctPathsString = new HashSet<>(listTempString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



//                        if (rowIndex == noOfRowsInFile) {
//        for(StepMetaData step : listOfAllStepsMetaData) {
//            for (Map.Entry<String, Integer> entry : step.getFilePathsDataTableRowCountsMap().entrySet()) {
//                step.setFilePathsScenarioCountsMap(entry.getKey(), scenarioRecurrenceCount);
//            }
//        }
//    }
//    rowIndex++;
//}
//                i++;
//                        isBackground = false;
//                        isScenario = false;
//                        scenarioRecurrenceCount = 0;
//                        }
//                        totalNoOfSteps = listOfAllStepsMetaData.size();
//                        } catch (IOException ex) {
//                        ex.printStackTrace();
//                        }


    protected void readKeywordsAndParameters() {
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
                        smd.setFilePathsScenarioCountsMap(currentPathString, 0);
                        smd.setFilePathsScenarioOutlineCountsMap(currentPathString, 0);
                        int index = trimmedStringLine.indexOf(" ");
                        smd.setStepType(trimmedStringLine.substring(0, index));
                        if (!isScenario) {
                            smd.setBackground(isBackground);
                        }
                        listOfAllStepsMetaData.add(smd);
                    }
                    if (rowIndex == noOfRowsInFile) {
                        for(StepMetaData step : listOfAllStepsMetaData) {
                            for (Map.Entry<String, Integer> entry : step.getFilePathsDataTableRowCountsMap().entrySet()) {
                                step.setFilePathsScenarioCountsMap(entry.getKey(), scenarioRecurrenceCount);
                            }
                        }
                    }
                    rowIndex++;
                }
                i++;
                isBackground = false;
                isScenario = false;
                scenarioRecurrenceCount = 0;
            }
            totalNoOfSteps = listOfAllStepsMetaData.size();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
