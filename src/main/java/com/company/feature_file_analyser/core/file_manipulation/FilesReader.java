package com.company.feature_file_analyser.core.file_manipulation;

import com.company.feature_file_analyser.core.custom_types.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FilesReader {
    private static final List<FeatureFile> listOfAllFeatureFiles = new ArrayList<>();
    public static final List<Step> listOfAllSteps = new ArrayList<>();
    protected final List<String> listTempString = new ArrayList<>();
    private final String userDir = System.getProperty("user.dir");
    @Getter
    protected GeneralMetrics metrics = null;
    protected Step step = null;
    protected FeatureFile featureFile = null;
    protected GenericType<Step> genTypeStep = null;
    protected GenericType<FeatureFile> genTypeFeatureFile = null;
    protected String inputFilePath = "To be specified at Runtime";
    protected Project project = null;

    public FilesReader(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        this.project = new Project();
        this.metrics = new GeneralMetrics();
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

    protected void readFeatureFile_Path__And__Step_NameLineIndex() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String currentPathString = "";
        String trimmedLine = "";
        int fileIndex = 0, rowIndex = 1, spaceIndex = 0, i = 0;

        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));

                for (String line : allLinesOfSpecificFile) {
                    trimmedLine = line.trim();

                    if (i == 0 || !isFeatureFilePathAlreadyPresent(currentPathString)) {
                        featureFile = new FeatureFile();
                        genTypeFeatureFile = new GenericType<FeatureFile>(featureFile);
                        FeatureFile fmd = genTypeFeatureFile.getObj();
                        fmd.setFilePath(currentPathString);
                        listOfAllFeatureFiles.add(fmd);
                        i++;
                    }

                    if (trimmedLine.startsWith("Given") || trimmedLine.startsWith("When")
                            || trimmedLine.startsWith("Then") || line.contains("And")) {
                        step = new Step();
                        genTypeStep = new GenericType<Step>(step);
                        Step smd = genTypeStep.getObj();
                        smd.setStepName(trimmedLine);
                        spaceIndex = trimmedLine.indexOf(" ");
                        smd.setStepType(trimmedLine.substring(0, spaceIndex));
                        smd.setLineNumber(rowIndex);
                        listOfAllSteps.add(smd);
                    }
                    rowIndex++;
                }
                fileIndex++;
            }
            metrics.setTotalNoOfSteps(listOfAllSteps.size());
        } catch (IOException ex) {
            log.error("Exception encountered when reading in the Keywords and Parameters");
            ex.printStackTrace();
        }
    }

    protected void readScenariosAndOutlines_PathNameLineIndex() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        listTempString.clear();
        String currentPathString = "";
        String trimmedLine = "";
        int fileIndex = 0, rowIndex = 1;

        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesOfSpecificFile) {
                    trimmedLine = line.trim();
                    int startIndex = trimmedLine.indexOf(":");

                    for (FeatureFile file : listOfAllFeatureFiles) {
                        if (file.getFilePath().equalsIgnoreCase(currentPathString)) {

                            if (!trimmedLine.contains("Scenario Outline:") && trimmedLine.contains("Scenario")) {
                                Scenario scenario = new Scenario();
                                scenario.setFilePath(currentPathString);
                                scenario.setName(trimmedLine);
                                scenario.setLineNumber(rowIndex);
                                file.incrementScenarioRecurrenceCount();
                                file.addScenarioName(trimmedLine.substring(startIndex + 1));
                                file.addScenario(scenario);
                            }

                            if (!trimmedLine.contains("Scenario:") && trimmedLine.contains("Scenario Outline:")) {
                                ScenarioOutline outline = new ScenarioOutline();
                                outline.setFilePath(currentPathString);
                                outline.setName(trimmedLine);
                                outline.setLineNumber(rowIndex);
                                file.incrementScenarioOutlineRecurrenceCount();
                                file.addScenarioOutlineName(trimmedLine.substring(startIndex + 1));
                                file.addScenarioOutline(outline);
                            }
                        }


                        //Block to detect when isBackground not applicable
//                        isScenario = true;
//                        isBackground = false;
                    }


//                    if (trimmedLine.startsWith("Background")) {
//                        isBackground = true;
//                    }
//
//
//                    if (!isScenario) {
//                        smd.setBackground(isBackground);
//                    }
//
//
//
//                    if (line.chars().filter(ch -> ch == '|').count() >= 2) {
//                        //Read in the number of data table rows excluding the header
//                        dataTableRowCount++;
//                    }
                }

                // Update the file data table rowcount in List<StepMetaData>
                for (Step step : listOfAllSteps) {
//                    if (step.getFilePathsDataTableRowCountsMap().containsKey(currentPathString)) {
//                        if (dataTableRowCount > 0) {
//                            // Exclude the header value
//                            step.setFilePathsDataTableRowCountsMap(currentPathString, dataTableRowCount - 1);
//                        } else {
//                            step.setFilePathsDataTableRowCountsMap(currentPathString, 0);
//                        }
//                    }
//                    if (step.getFilePathsScenarioCountsMap().containsKey(currentPathString)) {
//                        step.setFilePathsScenarioCountsMap(currentPathString, scenarioRecurrenceCount);
//                    }
//                    if(step.getFilePathsScenarioOutlineCountsMap().containsKey(currentPathString)) {
//                        step.setFilePathsScenarioOutlineCountsMap(currentPathString, scenarioOutlineRecurrenceCount);
//                    }
                    listTempString.add(currentPathString);


//
//                    for(FeatureFile file : listOfAllFeatureFiles) {
//                        for (Map.Entry<String, Integer> entry : file.getFilePathsScenarioCountsMap().entrySet()) {
//                            file.setFilePathsScenarioCountsMap(entry.getKey(), scenarioRecurrenceCount);
//                        }
//                        for (Map.Entry<String, Integer> entry : file.getFilePathsScenarioOutlineCountsMap().entrySet()) {
//                            file.setFilePathsScenarioOutlineCountsMap(entry.getKey(), scenarioOutlineRecurrenceCount);
//                        }
//                    }
                    rowIndex++;


                }
                fileIndex++;
//                dataTableRowCount = 0;  // Reset the count for the next file
//                scenarioRecurrenceCount = 0;
//                scenarioOutlineRecurrenceCount = 0;
//                isBackground = false;
//                isScenario = false;
//                scenarioRecurrenceCount = 0;
            }
            project.initialiseSetOfDistinctPathsString(listTempString);
        } catch (IOException ex) {
            log.error("Exception encountered when reading in the Data Table Row Counts");
            ex.printStackTrace();
        }
    }

    protected void readKeywords() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String currentPathString = "";
        String trimmedLine = "";
        int fileIndex = 0, rowIndex = 1, i = 0;

        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));

                // readFeatureFile_Path__And__Step_NameLineIndex();
                // readScenariosAndOutlines_PathNameLineIndex();

                for (String line : allLinesOfSpecificFile) {
                    trimmedLine = line.trim();

                    if (trimmedLine.startsWith("Given") || trimmedLine.startsWith("When")
                            || trimmedLine.startsWith("Then") || line.contains("And")
                            && rowIndex == step.getLineNumber()) {


                    }

                    for (FeatureFile file : listOfAllFeatureFiles) {
                        for (Step step : listOfAllSteps) {
                            if (file.getFilePath().equalsIgnoreCase(currentPathString)
                                    && step.getStepName().equals(trimmedLine)
                                    && rowIndex == step.getLineNumber()) {

                            }
                        }
                    }

                    rowIndex++;
                }
                fileIndex++;
            }
            metrics.setTotalNoOfSteps(listOfAllSteps.size());
        } catch (IOException ex) {
            log.error("Exception encountered when reading in the Keywords and Parameters");
            ex.printStackTrace();
        }
    }

    private boolean isFeatureFilePathAlreadyPresent(String filePath) {
        long count = listOfAllFeatureFiles.stream()
                .filter(f -> f.getFilePath().equalsIgnoreCase(filePath))
                .count();
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

}
