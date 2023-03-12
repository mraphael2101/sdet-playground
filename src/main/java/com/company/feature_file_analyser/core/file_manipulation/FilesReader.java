package com.company.feature_file_analyser.core.file_manipulation;

import com.company.feature_file_analyser.config.ExtractionAlgorithm;
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
public class FilesReader implements ExtractionAlgorithm {
    public static final List<FeatureFile> listOfAllFeatureFiles = new ArrayList<>();
    public static final List<Step> listOfAllSteps = new ArrayList<>();
    private final String userDir = System.getProperty("user.dir");
    protected Utils utils = null;
    @Getter
    protected Metrics metrics = null;
    protected Step step = null;
    protected FeatureFile featureFile = null;
    protected GenericType<Step> genTypeStep = null;
    protected GenericType<FeatureFile> genTypeFeatureFile = null;
    protected String inputFilePath = "To be specified at Runtime";

    public FilesReader(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        this.utils = new Utils();
        this.metrics = new Metrics();
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

    public void extractFeatureFilesAndSteps() {
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
                        smd.setDataDriven((smd.getStepName().chars().filter(ch -> ch == '\'').count() == 2
                                || smd.getStepName().chars().filter(ch -> ch == '\"').count() == 2));
                        smd.setDataTableDriven(smd.getStepName().contains("<") && smd.getStepName().contains(">"));
                        listOfAllSteps.add(smd);
                    }
                    rowIndex++;
                }
                fileIndex++;
                rowIndex = 1;
            }
            metrics.setTotalNoOfSteps(listOfAllSteps.size());
            metrics.initialiseSetOfDistinctStepNames();
        } catch (IOException ex) {
            log.error("Exception encountered when reading in the Keywords and Parameters");
            ex.printStackTrace();
        }
    }

    public void extractScenariosAndOutlines() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

                            if (trimmedLine.contains("Scenario Outline:")) {
                                ScenarioOutline outline = new ScenarioOutline();
                                outline.setFilePath(currentPathString);
                                outline.setName(trimmedLine);
                                outline.setLineNumber(rowIndex);
                                file.incrementScenarioOutlineRecurrenceCount();
                                file.addScenarioOutlineName(trimmedLine.substring(startIndex + 1));
                                file.addScenarioOutline(outline);
                            }

                        }
                    }
                    rowIndex++;
                }
                fileIndex++;
                rowIndex = 1;
            }
            metrics.initialiseSetOfDistinctPathsString(utils.getListOfString());
        } catch (IOException ex) {
            log.error("Exception encountered when...");
            ex.printStackTrace();
        }
    }

    public void extractToBeDetermined() {
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

                    for (ScenarioOutline outline : featureFile.getListOfScenarioOutlines()) {
                        System.out.println(outline.getName());
                        System.out.println(outline.getFilePath());
                        System.out.println(outline.getLineNumber());
                        System.out.println(outline.getDataTable());
                        System.out.println(outline.getStepNames());
                    }

                    for (Scenario scenario : featureFile.getListOfScenarios()) {
                        System.out.println(scenario.getName());
                        System.out.println(scenario.getFilePath());
                        System.out.println(scenario.getLineNumber());
                        System.out.println(scenario.getStepNames());
                    }

                    rowIndex++;
                }
                fileIndex++;
                rowIndex = 1;
            }
        } catch (IOException ex) {
            log.error("Exception encountered when...");
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
