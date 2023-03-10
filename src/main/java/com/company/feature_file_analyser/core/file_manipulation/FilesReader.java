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
    protected final List<String> listTempString = new ArrayList<>();
    private final String userDir = System.getProperty("user.dir");
    private String inputFilePath = "To be specified at Runtime";
    private GeneralMetrics generalMetrics = null;
    private Project project = null;
    protected Step step = null;
    @Getter
    private final List<Step> listOfAllSteps = new ArrayList<>();
    protected FeatureFile featureFile = null;
    @Getter
    private final List<FeatureFile> listOfAllFeatureFiles = new ArrayList<>();
    protected GenericType<Step> genTypeStep = null;
    protected GenericType<FeatureFile> genTypeFeatureFile = null;

    public FilesReader(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        this.project = new Project();
        this.generalMetrics = new GeneralMetrics();
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

    protected void readFeatureFileAndStepProperties() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String currentPathString = "";
        String trimmedStringLine = "";
        int fileIndex = 0, rowIndex = 1, spaceIndex = 0, i = 0;

        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));

                for (String line : allLinesOfSpecificFile) {
                    trimmedStringLine = line.trim();

                    if (i == 0 || !isFeatureFilePathAlreadyPresent(currentPathString)) {
                        featureFile = new FeatureFile();
                        genTypeFeatureFile = new GenericType<FeatureFile>(featureFile);
                        FeatureFile fmd = genTypeFeatureFile.getObj();
                        fmd.setFilePath(currentPathString);
                        listOfAllFeatureFiles.add(fmd);
                        i++;
                    }

                    if (trimmedStringLine.startsWith("Given") || trimmedStringLine.startsWith("When")
                            || trimmedStringLine.startsWith("Then") || line.contains("And")) {
                        step = new Step();
                        genTypeStep = new GenericType<Step>(step);
                        Step smd = genTypeStep.getObj();
                        smd.setStepName(trimmedStringLine);
                        smd.setStepType(trimmedStringLine.substring(0, spaceIndex));
                        listOfAllSteps.add(smd);
                    }
                    rowIndex++;
                }
                fileIndex++;
            }
            generalMetrics.setTotalNoOfSteps(listOfAllSteps.size());
        } catch (IOException ex) {
            log.error("Exception encountered when reading in the Keywords and Parameters");
            ex.printStackTrace();
        }
    }

    protected void readScenariosAndOutlines() {
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
        int fileIndex = 0;
        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));
                for (String line : allLinesOfSpecificFile) {
                    trimmedLine = line.trim();
                    int startIndex = trimmedLine.indexOf(":");

                    for(FeatureFile file : listOfAllFeatureFiles) {
                        if(file.getFilePath().equalsIgnoreCase(currentPathString)) {

                            if(!trimmedLine.contains("Scenario Outline:") && trimmedLine.contains("Scenario")) {
                                Scenario scenario = new Scenario();
                                scenario.setScenarioFilePath(currentPathString);
                                scenario.setScenarioName(trimmedLine);
                                file.incrementScenarioRecurrenceCount();
                                file.addScenarioName(trimmedLine.substring(startIndex + 1));
                                file.addScenario(scenario);
                            }

                            if(!trimmedLine.contains("Scenario:") && trimmedLine.contains("Scenario Outline:")){
                                ScenarioOutline outline = new ScenarioOutline();
                                outline.setScenarioOutlineFilePath(currentPathString);
                                outline.setScenarioOutlineName(trimmedLine);
                                file.incrementScenarioOutlineRecurrenceCount();
                                file.addScenarioOutlineName(trimmedLine.substring(startIndex + 1));
                                file.addScenarioOutline(outline);
                            }
                        }

//                        for(Step step : listOfAllSteps) {
//                            if(file.getFilePath().equalsIgnoreCase(currentPathString)
//                                    && step.getStepName().equals(trimmedLine)
//                                    && file.getScenario("").contains()) {
//
//                                //remember that there are multiple steps in the same file that could be the same
//                                //how to identify the right step object?
//                                step.setBackground(true);
//
//
//                            }
//                        }






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

    private boolean isFeatureFilePathAlreadyPresent(String filePath) {
        long count = getListOfAllFeatureFiles().stream()
                .filter(f -> f.getFilePath().equalsIgnoreCase(filePath))
                .count();
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

}
