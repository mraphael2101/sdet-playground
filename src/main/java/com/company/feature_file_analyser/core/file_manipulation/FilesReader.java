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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FilesReader {
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
    private String currentPathString = "", trimmedLine = "";
    private int fileIndex = 0, rowIndex = 1, i = 0;

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
    private boolean isFeatureFilePathAlreadyPresent(String filePath) {
        long count = listOfAllFeatureFiles.stream()
                .filter(f -> f.getPath().equalsIgnoreCase(filePath))
                .count();
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }
    public void extractFeatureAndStepsToFeatureFile() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int spaceIndex = 0;
        FeatureFile fmd = null;
        Step smd = null;
        DataTable dt = null;
        int tableRowCount = 0;

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
                        fmd = genTypeFeatureFile.getObj();
                        fmd.setPath(currentPathString);
                        listOfAllFeatureFiles.add(fmd);
                        i++;
                    }

                    if (trimmedLine.startsWith("Given") || trimmedLine.startsWith("When")
                            || trimmedLine.startsWith("Then") || line.contains("And")
                            || trimmedLine.chars().filter(ch -> ch == '|').count() >= 2
                            || trimmedLine.startsWith("Examples:")) {

                        step = new Step();
                        genTypeStep = new GenericType<Step>(step);
                        smd = genTypeStep.getObj();
                        smd.setPath(currentPathString);

                        // If a step has an in-line data table, record it as so
                        if (!trimmedLine.startsWith("Examples:")) {
                            if (trimmedLine.chars().filter(ch -> ch == '|').count() >= 2) {
                                smd.setStepType("In-line");
                                smd.setLineNumber(rowIndex);
                                if (tableRowCount == 0) {
                                    dt = smd.createDataTable();
                                    dt.setPath(currentPathString);
                                    dt.addHeader(trimmedLine);
                                    dt.setStartRowIndex(rowIndex);
                                    ++tableRowCount;
                                } else {
                                    dt.addRow(trimmedLine);
                                }
                            }
                        }
                        if (trimmedLine.startsWith("Examples:")) {
                            break;
                        } else {
                            if(trimmedLine.contains("|")) {
                                smd.setStepType("In-line");
                            } else {
                                spaceIndex = trimmedLine.indexOf(" ");
                                smd.setStepType(trimmedLine.substring(0, spaceIndex));
                            }
                            smd.setStepName(trimmedLine);
                            smd.setLineNumber(rowIndex);
                            smd.setDataDriven((smd.getStepName().chars().filter(ch -> ch == '\'').count() == 2
                                    || smd.getStepName().chars().filter(ch -> ch == '\"').count() == 2));
                            smd.setDataTableDriven(smd.getStepName().contains("<") && smd.getStepName().contains(">"));
                        }

                        listOfAllSteps.add(smd);

                        fmd.addStep(step);
                        fmd.putStepNameRowIndex(trimmedLine, rowIndex);
                    }
                    rowIndex++;
                }
                fileIndex++;
                rowIndex = 1;
            }
            metrics.setOverallNoOfSteps(listOfAllSteps.size());
            metrics.initialiseSetOfDistinctStepNames();
        } catch (IOException ex) {
            log.error("Exception encountered when...");
            ex.printStackTrace();
        }
    }
    public void extractScenariosAndOutlinesToFeatureFile() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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
                        if (file.getPath().equalsIgnoreCase(currentPathString)) {
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

                    // Populate the total number of Scenarios and Scenario Outlines in each Feature File
                    metrics.initialiseSetOfDistinctPathsString();
                    for (int i = 0; i < metrics.getSetOfDistinctFilePaths().size(); i++) {
                        listOfAllFeatureFiles.get(fileIndex).setTotalNoOfStepsInFile(listOfAllSteps.size());
                        listOfAllFeatureFiles.get(fileIndex).setScenarioRecurrenceCount(i);
                        listOfAllFeatureFiles.get(fileIndex).setScenarioOutlineRecurrenceCount(i);
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
    public void enrichData() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        DataTable dt = null;

        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));

                for (String line : allLinesOfSpecificFile) {
                    trimmedLine = line.trim();

                    for (FeatureFile file : listOfAllFeatureFiles) {
                        for (Step step : listOfAllSteps) {
                            dt = step.getDataTable();
                            int stepPredecessorLineNo = 0;
                            int lineNoDifference = 0;
                            if (file.getPath().equals(step.getPath()) && dt != null) {
                                lineNoDifference = dt.getStartRowIndex();
                                stepPredecessorLineNo = Objects.requireNonNull(getStepByLineIndexAndDtPath(dt.getPath(),
                                        lineNoDifference -= 1)).getLineNumber();
                                step = getStepByLineIndexAndDtPath(dt.getPath(), lineNoDifference);
                                lineNoDifference = dt.getStartRowIndex() - stepPredecessorLineNo;
                                if (lineNoDifference == 1) {
                                    Objects.requireNonNull(step).setStepType("In-line");
                                }
                            }
                        }
                    }

                    for (ScenarioOutline outline : featureFile.getListOfScenarioOutlines()) {
                        System.out.println(outline.getName());
                        System.out.println(outline.getFilePath());
                        System.out.println(outline.getLineNumber());
                        System.out.println(outline.getDataTable());
                        System.out.println(outline.getStepNames());
                        dt = outline.getDataTable();
                        if (dt != null) {
                            for (int k = 0; k < dt.getRowCount(); k++) {
                                dt.addRow("dt value");
                            }
                        }
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
    private Step getStepByLineIndexAndDtPath(String dtPath, int lineIndex) {
        try {
            return (Step) listOfAllSteps.stream()
                    .filter(s -> s.getLineNumber() == lineIndex)
                    .filter(f -> f.getPath().equals(dtPath))
                    .toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

}
