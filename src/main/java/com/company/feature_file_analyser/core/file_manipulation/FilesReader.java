package com.company.feature_file_analyser.core.file_manipulation;

import com.company.feature_file_analyser.core.Metrics;
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
    @Getter
    protected Metrics metrics = null;
    protected Utils utils = null;
    protected Step step = null;
    protected FeatureFile file = null;
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
    public void extractFeatureFilesScenarioTypesAndStepsIncludingInline() {
        List<Path> paths = null;
        Path path = Paths.get(userDir + inputFilePath);
        try {
            paths = listFiles(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String currentPathString = "";
        String trimmedLine = "";
        int currentFileIndex = 0, rowIndex = 1, spaceIndex = 0, i = 0, nextFileIndex = 0;
        FeatureFile fmd = null;
        Step smd = null;
        DataTable dt = null;
        ScenarioOutline lastOutline = null;
        int inlineOccurrenceCount = 0;
        int outlineOccurrenceCount = 0;

        try {
            while (true) {
                assert paths != null;
                if (!(currentFileIndex < paths.size())) break;
                currentPathString = paths.get(currentFileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));

                for (String line : allLinesOfSpecificFile) {
                    trimmedLine = line.trim();
                    String name = trimmedLine.substring(trimmedLine.indexOf(":") + 1);

                    if (i == 0 || !isFeatureFilePathAlreadyPresent(currentPathString)) {
                        file = new FeatureFile();
                        genTypeFeatureFile = new GenericType<FeatureFile>(file);
                        fmd = genTypeFeatureFile.getObj();
                        fmd.setPath(currentPathString);
                        listOfAllFeatureFiles.add(fmd);
                        i++;
                    }

                    if (trimmedLine.startsWith("Given") || trimmedLine.startsWith("When")
                            || trimmedLine.startsWith("Then") || line.contains("And")) {
                        step = new Step();
                        genTypeStep = new GenericType<Step>(step);
                        smd = genTypeStep.getObj();
                        smd.setStepName(trimmedLine);
                        smd.setLineNumber(rowIndex);
                        smd.setDataDriven((smd.getStepName().chars().filter(ch -> ch == '\'').count() == 2
                                || smd.getStepName().chars().filter(ch -> ch == '\"').count() == 2));
                        smd.setDataTableDriven(smd.getStepName().contains("<") && smd.getStepName().contains(">"));
                        smd.setPath(currentPathString);
                        spaceIndex = trimmedLine.indexOf(" ");
                        smd.setStepType(trimmedLine.substring(0, spaceIndex));
                        listOfAllSteps.add(smd);

                        fmd.addStep(step);
                        fmd.putStepNameRowIndex(trimmedLine, rowIndex);
                    }

                    // Get the last created ScenarioOutline object and set flag to true when table encountered
                    if (trimmedLine.startsWith("Examples:")) {
                        lastOutline = fmd.getLastScenarioOutline();
                        lastOutline.setDataTableEncountered(true);
                        outlineOccurrenceCount = 0;
                        rowIndex++;
                        continue;
                    }

                    // Create Scenario Outline Data Table when encountering and parsing >=2 '|'
                    // Create In-line Data Table when Outline Data table is not encountered and not parsed
                    // Append Data Table rows to the Previous Step
                    if (trimmedLine.chars().filter(ch -> ch == '|').count() >= 2
                            && lastOutline != null && !lastOutline.isDataTableEncountered()
                            && !lastOutline.isDataTableParsingComplete()
                            || trimmedLine.chars().filter(ch -> ch == '|').count() >= 2
                            && lastOutline != null && lastOutline.isDataTableEncountered()
                            && lastOutline.isDataTableParsingComplete()) {

                        if (inlineOccurrenceCount == 0) {
                            dt = new DataTable();
                            dt.setPath(currentPathString);
                            dt.addHeader(trimmedLine);
                            dt.setStartRowIndex(rowIndex);
                            setPreviousStepType(file, step, dt, "In-line");
                            Step previous = getPreviousStep(file, step, dt);
                            Objects.requireNonNull(previous).setDataTable(dt);
                            previous.setDataTableDriven(true);
                            inlineOccurrenceCount++;
                        }

                        else if(!trimmedLine.equals("")) {
                            dt.addRow(trimmedLine);
                            dt.setEndRowIndex(dt.getRows().size());
                        }

                    }

                    if(lastOutline != null && lastOutline.isDataTableEncountered()
                            && !lastOutline.isDataTableParsingComplete()) {

                        if (outlineOccurrenceCount == 0) {
                            if(!trimmedLine.equals("")) {
                                dt = Objects.requireNonNull(lastOutline).getDataTable();
                                dt.setPath(currentPathString);
                                dt.addHeader(trimmedLine);
                                dt.setStartRowIndex(rowIndex);
                                outlineOccurrenceCount++;
                            }
                        }

                        // Perform the algorithm only if the line is not blank and the currentFileIndex is the
                        // same as the next File index
                        else if(!trimmedLine.equals("") && currentFileIndex == nextFileIndex) {
                                dt.addRow(trimmedLine);
                                dt.setEndRowIndex(dt.getRows().size());
                                outlineOccurrenceCount++;
                        }

                        else if(trimmedLine.equals("") && dt.getRows().size() > 0) {
                            lastOutline.setDataTableParsingComplete(true);
                            nextFileIndex++;
                        }

                    }

                    if (!trimmedLine.contains("Scenario Outline:") && trimmedLine.contains("Scenario")) {
                        Scenario scenario = new Scenario();
                        scenario.setFilePath(currentPathString);
                        scenario.setName(name);
                        scenario.setLineNumber(rowIndex);
                        fmd.incrementScenarioRecurrenceCount();
                        fmd.addScenarioName(name);
                        fmd.addScenario(scenario);
                    }

                    if (trimmedLine.contains("Scenario Outline:")) {
                        ScenarioOutline outline = new ScenarioOutline();
                        outline.setPath(currentPathString);
                        outline.setName(name);
                        outline.setLineNumber(rowIndex);
                        fmd.incrementScenarioOutlineRecurrenceCount();
                        fmd.addScenarioOutlineName(name);
                        fmd.addScenarioOutline(outline);
                    }

                    rowIndex++;

                }

                currentFileIndex++;
                rowIndex = 1;

            }

            metrics.setOverallNoOfSteps(listOfAllSteps.size());
            metrics.initialiseSetOfDistinctStepNames();

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
        String currentPathString = "";
        String trimmedLine = "";
        int fileIndex = 0, rowIndex = 1, i = 0;
        DataTable dt = null;

        try {
            while (true) {
                assert paths != null;
                if (!(fileIndex < paths.size())) break;
                currentPathString = paths.get(fileIndex).toString();
                List<String> allLinesOfSpecificFile = Files.readAllLines(Paths.get(currentPathString));

                for (String line : allLinesOfSpecificFile) {

                    for (FeatureFile file : listOfAllFeatureFiles) {

                        for (ScenarioOutline outline : file.getListOfScenarioOutlines()) {

                            for(Step step : listOfAllSteps) {

                                //TODO Revise for in-line 1 line
                                if (file.getPath().equalsIgnoreCase(currentPathString)
                                        && outline.getPath().equals(file.getPath())) {
                                    System.out.println(step);
                                }
                                System.out.println("");

                                //TODO You are here -> Populate Examples Data tables (more than one in a file)

                                if (file.getPath().equalsIgnoreCase(currentPathString)
                                        && outline.getPath().equals(file.getPath())) {

                                    getDataTableDrivenStepsForScenarioOutline(outline);

                                    dt = outline.getDataTable();
                                    if (dt != null) {
                                        for (int k = 0; k < dt.getRowCount(); k++) {
                                            dt.addRow("dt value");
                                        }
                                    }
                                }

                            }
                        }

                        rowIndex++;
                    }
                }
                fileIndex++;
                rowIndex = 1;
            }
        } catch (IOException ex) {
            log.error("Exception encountered when...");
            ex.printStackTrace();
        }
    }
    private DataTable getDataTableForInlineStep(FeatureFile file, Step step) {
        if(file.getPath().equals(step.getPath())
                && step.getStepType().equals("Inline")) {
            return step.getDataTable();
        }
        return null;
    }
    private DataTable getDataTableForScenarioOutline(FeatureFile file, String outlineName) {
        return file.getScenarioOutline(outlineName).getDataTable();
    }
    private List<DataTable> getDataTableListForAllScenarioOutlines() {
        List<ScenarioOutline> overallOutlineList = new ArrayList<>();
        for(FeatureFile file : listOfAllFeatureFiles) {
            Stream.of(file.getListOfScenarioOutlines())
                    .forEach(overallOutlineList::addAll);
        }
        List<DataTable> overallDataTableList = new ArrayList<>();
        for(ScenarioOutline outline : overallOutlineList) {
            overallDataTableList.add(outline.getDataTable());
        }
        return overallDataTableList;
    }
    private List<Step> getDataTableDrivenStepsForScenarioOutline(ScenarioOutline outline) {
        return outline.getSteps();
    }
    private Step getStepByRowIndexAndDataTablePath(String dtPath, int lineIndex) {
        try {
            return (Step) listOfAllSteps.stream()
                    .filter(s -> s.getLineNumber() == lineIndex)
                    .filter(f -> f.getPath().equals(dtPath))
                    .toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    private Step getPreviousStep(Step targetStep, DataTable dt) {
        int lineNoOfPriorStep = 0, differenceInLines = 0;
        if (targetStep.getPath().equals(dt.getPath())) {
            differenceInLines = dt.getStartRowIndex();
            lineNoOfPriorStep = Objects.requireNonNull(getStepByRowIndexAndDataTablePath(dt.getPath(),
                    differenceInLines -= 1)).getLineNumber();
            targetStep = getStepByRowIndexAndDataTablePath(dt.getPath(), differenceInLines);
            differenceInLines = dt.getStartRowIndex() - lineNoOfPriorStep;

            if (differenceInLines == 1) {
                return targetStep;
            }
        }
        return null;
    }
    private Step getPreviousStep(FeatureFile file, Step targetStep, DataTable dt) {
        int lineNoOfPriorStep = 0, differenceInLines = 0;

        if (file.getPath().equals(targetStep.getPath())
                && targetStep.getPath().equals(dt.getPath())) {
            differenceInLines = dt.getStartRowIndex();
            lineNoOfPriorStep = Objects.requireNonNull(getStepByRowIndexAndDataTablePath(dt.getPath(),
                    differenceInLines -= 1)).getLineNumber();
            targetStep = getStepByRowIndexAndDataTablePath(dt.getPath(), differenceInLines);
            differenceInLines = dt.getStartRowIndex() - lineNoOfPriorStep;

            if (differenceInLines == 1) {
                return targetStep;
            }
        }
        return null;
    }

    private void setPreviousStepType(FeatureFile file, Step targetStep, DataTable dt, String value) {
        int lineNoOfPriorStep = 0, differenceInLines = 0;

        if (file.getPath().equals(targetStep.getPath()) && dt != null) {
            differenceInLines = dt.getStartRowIndex();
            lineNoOfPriorStep = Objects.requireNonNull(getStepByRowIndexAndDataTablePath(dt.getPath(),
                    differenceInLines -= 1)).getLineNumber();
            targetStep = getStepByRowIndexAndDataTablePath(dt.getPath(), differenceInLines);
            differenceInLines = dt.getStartRowIndex() - lineNoOfPriorStep;

            if (differenceInLines == 1) {
                Objects.requireNonNull(targetStep).setStepType(value);
            }
        }

    }

}
