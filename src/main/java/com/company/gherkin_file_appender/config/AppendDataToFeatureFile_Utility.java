package com.company.gherkin_file_appender.config;

import com.company.gherkin_file_appender.interfaces.FeatureFile_DataAppender;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.copyOf;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class AppendDataToFeatureFile_Utility implements FeatureFile_DataAppender {
    private boolean cleanseSwitch;
    private String fileName = "";
    private String excelTab = "";
    private final String USER_DIR = System.getProperty("user.dir");
    private final String LINE_SEPARATOR = System.lineSeparator();
    private final List<String> inputFileSubsetAsList;
    private final List<String> inputFileAsList;
    private String[][] inputFileSubsetAsTwoDimArr;
    private String[][] inputFileAsTwoDimArr;

    public AppendDataToFeatureFile_Utility() {
        this.inputFileAsList = new ArrayList<>();
        this.inputFileSubsetAsList = new ArrayList<>();
    }

    @Override
    public String[][] readCleanseDataSourceFileInto2DArray(String fileName, boolean cleanseSwitch) {
        BufferedReader fileReader = null;
        String line = "";
        long rowCount = 0;
        int colCount = 0;
        int firstRowColCount = 0;
        String[] tokens = new String[0];
        try {
            fileReader = new BufferedReader(new FileReader(USER_DIR + getPartialInputFilePath() + fileName));
            rowCount = fileReader.lines().count();
            // re-initialise
            fileReader = new BufferedReader(new FileReader(USER_DIR + getPartialInputFilePath() + fileName));
            int counter = 0;
            while ((line = fileReader.readLine()) != null) {
                // IMPORTANT split by default removes trailing empty strings from a result array. To turn this mechanism off we need to use overloaded version
                tokens = line.split(",", -1);
                if (counter == 0) {
                    firstRowColCount = tokens.length;
                }
                colCount = tokens.length; // for every row
                if (tokens.length > firstRowColCount || tokens.length < firstRowColCount) {
                    throw new RuntimeException("Row index " + counter + " has " + colCount + " columns, and it should have " + firstRowColCount);
                }
                if (tokens.length > 0) {
                    // Cleansing happens here
                    if (cleanseSwitch) {
                        int FIELD_1_INDEX = 0;
                    }
                }
                inputFileAsList.addAll(Arrays.asList(tokens));
                counter++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Map List to 2D Array based on the colSize
        inputFileAsTwoDimArr = new String[(int) rowCount][tokens.length];
        int k = 0;
        for (int r = 0; r < rowCount; r++) {
            if (r % 2 == 0) {
                for (int c = 0; c < colCount; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList.get(k++);
                }
            } else {
                for (int c = 0; c < colCount; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList.get(k++);
                }
            }
        }
        return inputFileAsTwoDimArr;
    }

    public String[][] readCleanseDataSourceFileInto2DArray_AllSimPlans(String fileName, boolean cleanseSwitch) {
        BufferedReader fileReader = null;
        String line = "";
        long rowCount = 0;
        int colCount = 0;
        int firstRowColCount = 0;
        String[] tokens = new String[0];
        try {
            fileReader = new BufferedReader(new FileReader(USER_DIR + getPartialInputFilePath() + fileName));
            rowCount = fileReader.lines().count();
            // re-initialise
            fileReader = new BufferedReader(new FileReader(USER_DIR + getPartialInputFilePath() + fileName));
            int counter = 0;
            while ((line = fileReader.readLine()) != null) {
                // IMPORTANT split by default removes trailing empty strings from a result array. To turn this mechanism off we need to use overloaded version
                tokens = line.split(",", -1);
                if (counter == 0) {
                    firstRowColCount = tokens.length;
                }
                colCount = tokens.length; // for every row
                if (tokens.length > firstRowColCount || tokens.length < firstRowColCount) {
                    throw new RuntimeException("Row index " + counter + " has " + colCount + " columns, and it should have " + firstRowColCount);
                }
                if (tokens.length > 0) {
                    // Cleansing happens here
                    if (cleanseSwitch) {
                        int FIELD_1_INDEX = 2;
                        if (tokens[FIELD_1_INDEX].contains("2021")) {
                            tokens[FIELD_1_INDEX] = tokens[FIELD_1_INDEX].replace("2021", "");
                        }
                    }
                }
                inputFileAsList.addAll(Arrays.asList(tokens));
                counter++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Map List to 2D Array based on the colSize
        inputFileAsTwoDimArr = new String[(int) rowCount][tokens.length];
        int k = 0;
        for (int r = 0; r < rowCount; r++) {
            if (r % 2 == 0) {
                for (int c = 0; c < colCount; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList.get(k++);
                }
            } else {
                for (int c = 0; c < colCount; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList.get(k++);
                }
            }
        }
        return inputFileAsTwoDimArr;
    }

    public String[][] readCleanseDataSourceFileInto2DArray_DevicePricing(String fileName, boolean cleanseSwitch) {
        BufferedReader fileReader = null;
        String line = "";
        long rowCount = 0;
        int colCount = 0;
        int firstRowColCount = 0;
        String[] tokens = new String[0];
        try {
            fileReader = new BufferedReader(new FileReader(USER_DIR + getPartialInputFilePath() + fileName));
            rowCount = fileReader.lines().count();
            // re-initialise
            fileReader = new BufferedReader(new FileReader(USER_DIR + getPartialInputFilePath() + fileName));
            int counter = 0;
            while ((line = fileReader.readLine()) != null) {
                // IMPORTANT split by default removes trailing empty strings from a result array. To turn this mechanism off we need to use overloaded version
                tokens = line.split(",", -1);
                if(counter == 0) {
                    firstRowColCount = tokens.length;
                }
                colCount = tokens.length; // for every row
                if(tokens.length > firstRowColCount  || tokens.length < firstRowColCount ) {
                    throw new RuntimeException("Row index " + counter + " has " + colCount + " columns, and it should have " + firstRowColCount);
                }
                if (tokens.length > 0) {
                    // Cleansing happens here
                    if(cleanseSwitch) {
                        if (tokens[1].endsWith(" ")) {
                            tokens[1] = tokens[1].strip();
                        }
                    }
                }
                inputFileAsList.addAll(Arrays.asList(tokens));
                counter++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Map List to 2D Array based on the colSize
        inputFileAsTwoDimArr = new String[(int) rowCount][tokens.length];
        int k = 0;
        for (int r = 0; r < rowCount; r++) {
            if (r % 2 == 0) {
                for (int c = 0; c < colCount; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList.get(k++);
                }
            } else {
                for (int c = 0; c < colCount; c++) {
                    inputFileAsTwoDimArr[r][c] = inputFileAsList.get(k++);
                }
            }
        }
        return inputFileAsTwoDimArr;
    }

    @Override
    public boolean copyFeatureFile(String fileName) {
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            File fromFile = new File(USER_DIR + getPartialOutputFilePath() + fileName);
            String toFilePath = USER_DIR + getPartialOutputFilePath() + "Data_Vol_" + fileName;
            File toFile = new File(toFilePath);
            setFileName(toFilePath);
            inStream = new FileInputStream(fromFile);
            outStream = new FileOutputStream(toFile);
            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (outStream != null)
                    outStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean appendDataToNewFeatureFile(String scenarioType, String mode, Object... args) {
        FileWriter fw = null;
        try {
            boolean outlineFlag = true;
            switch (scenarioType.toLowerCase()) {
                case "scenario":
                    outlineFlag = false;
                    break;
                default:
                    break;
            }

            fw = new FileWriter(getFileName(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            String firstRow = getSpecificRowFromInputFile2DArray(0)
                    .toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll(",", "|") + LINE_SEPARATOR;
            String rowset = "";
            int rowIndex = 0;
            int columnSize = 0;
            Predicate<String> predicate = null;
            int queryColIndex = 0;
            int resultColStartIndex = 0;
            int resultColEndIndex = 0;
            if (args.length == 0) {
            }
            else if (args.length == 1) {
                rowIndex = (int) args[0];
            }
            else if (args.length == 2 && args[0] instanceof Integer && args[1] instanceof Integer) {
                columnSize = (int) args[1] - (int) args[0];
            }else if (args.length == 2 && args[0] instanceof Predicate && args[1] instanceof Integer) {
                predicate = (Predicate<String>) args[0];
                queryColIndex = (int) args[1];
            }
            else if (args.length == 3) {
                predicate = (Predicate<String>) args[0];
                queryColIndex = (int) args[1];
                resultColStartIndex = (int) args[2];
            }
            else if (args.length == 4) {
                predicate = (Predicate<String>) args[0];
                queryColIndex = (int) args[1];
                resultColStartIndex = (int) args[2];
                resultColEndIndex = (int) args[3];
            }
            else {
                throw new RuntimeException("Incorrect no of arguments when calling appendDataToNewFeatureFile method.");
            }

            switch (mode.toLowerCase()) {
                case "alldata":
                    if(outlineFlag) {
                        for (String[] eachRow : getRowRangeFromInputFile2DArray(0, inputFileAsTwoDimArr.length - 1)) {
                            for (String str : eachRow) {
                                bw.write("|" + str.replace(",", "|"));
                            }
                            bw.write("|");
                            bw.write(LINE_SEPARATOR);
                        }
                    }
                    else {
                        for (String[] eachRow : getRowRangeFromInputFile2DArray(1, inputFileAsTwoDimArr.length - 1)) {
                            for (String str : eachRow) {
                                bw.write("|" + str.replace(",", "|"));
                            }
                            bw.write("|");
                            bw.write(LINE_SEPARATOR);
                        }
                    }
                    break;
                case "rowrange":
                    if (args.length > 2) {
                        throw new RuntimeException("A row range cannot have more than two values");
                    }
                    if ((int) args[0] > (int) args[1]) {
                        throw new RuntimeException("The first row range index cannot be greater than the second one");
                    }
                    if ((int) args[0] == (int) args[1]) {
                        throw new RuntimeException("The row range indexes should be different");
                    }
                    if(outlineFlag) {
                        int index = 0;
                        if((int) args[0] == 0) {
                            for (String[] eachRow : getRowRangeFromInputFile2DArray((int) args[0], (int) args[1])) {
                                for (String str : eachRow) {
                                    index++;
                                    bw.write("|" + str.replace(",", "|"));
                                }
                                if(index != 0) {
                                    bw.write("|");
                                }
                                bw.write(LINE_SEPARATOR);
                            }
                        }
                        else {
                            bw.write(firstRow);
                            for (String[] eachRow : getRowRangeFromInputFile2DArray((int) args[0], (int) args[1])) {
                                for (String str : eachRow) {
                                    bw.write("|" + str.replace(",", "|"));
                                }
                                bw.write("|");
                                bw.write(LINE_SEPARATOR);
                            }
                        }
                    }
                    if(!outlineFlag) {
                        if((int) args[0] == 0) {
                            throw new RuntimeException("The first row range index cannot be zero if providing the value scenario for the scenario type attribute.");
                        }
                        for (String[] eachRow : getRowRangeFromInputFile2DArray((int) args[0], (int) args[1])) {
                            for (String str : eachRow) {
                                bw.write("|" + str.replace(",", "|"));
                            }
                            bw.write("|");
                            bw.write(LINE_SEPARATOR);
                        }
                    }
                    break;
                case "row":
                    if (args.length == 2) {
                        throw new RuntimeException("A row index cannot have more than one value");
                    }
                    if(rowIndex == 0) {
                        throw new RuntimeException("Providing the value 0 for the row index will result in only the header row being displayed. Please provide a different index.");
                    }
                    if(outlineFlag) {
                        bw.write(firstRow);
                        rowset = getSpecificRowFromInputFile2DArray(rowIndex);
                        bw.write(rowset.replace(",", "|") + LINE_SEPARATOR);
                    }
                    if(!outlineFlag) {
                        rowset = getSpecificRowFromInputFile2DArray(rowIndex);
                        bw.write(rowset.replace(",", "|") + LINE_SEPARATOR);
                    }
                    break;
                case "colrange":
                    if (args.length > 2) {
                        throw new RuntimeException("A range cannot have more than two values");
                    }
                    if(outlineFlag) {
                        for (String[] eachRow : getColumnRangeFromInputFile2DArray((int) args[0], (int) args[1])) {
                            for (String str : eachRow) {
                                bw.write("|" + str.replace(",", "|"));
                            }
                            bw.write("|" );
                            bw.write(LINE_SEPARATOR);
                        }
                    }
                    else {
                        int i = 0;
                        for (String[] eachRow : getColumnRangeFromInputFile2DArray((int) args[0], (int) args[1])) {
                            if(i == 0) {
                                i++;
                                continue;
                            }
                            for (String str : eachRow) {
                                bw.write("|" + str.replace(",", "|"));
                            }
                            bw.write("|" );
                            bw.write(LINE_SEPARATOR);
                        }
                    }
                    break;
                case "column":
                    if(outlineFlag) {
                        for (String str : getSpecificColumnFromInputFile2DArray((int) args[0])) {
                            bw.write("|" + str.replace("[", "|")
                                    .replace("]", "")
                                    .replace(",", "|") + "|" + LINE_SEPARATOR);
                        }
                    }
                    else {
                        for (String str : Iterables.skip(getSpecificColumnFromInputFile2DArray((int) args[0]), 1)) {
                            bw.write("|" + str.replace("[", "|")
                                    .replace("]", "")
                                    .replace(",", "|") + "|" + LINE_SEPARATOR);
                        }
                    }
                    break;
                case "filtered_list":
                    if(outlineFlag) {
                        bw.write(getSpecificRowFromInputFile2DArray(0)
                                .toLowerCase()
                                .replaceAll(" ", "_"));
                        bw.newLine();
                        for (ResultSelection custObj : filterRowsByList(inputFileAsTwoDimArr, predicate, queryColIndex)) {
                            bw.write("|" + custObj.toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(",", "|")
                                    + "|" + LINE_SEPARATOR);
                        }
                    }
                    else {
                        int i = 0;
                        for (ResultSelection custObj : filterRowsByList(inputFileAsTwoDimArr, predicate, queryColIndex)) {
                            bw.write("|" + custObj.toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(",", "|")
                                    + "|" + LINE_SEPARATOR);
                        }
                    }
                    break;
                case "filtered_map_by_col":
                    Map<Integer, String[]> map = filterRowsByMap(inputFileAsTwoDimArr, predicate, queryColIndex);
                    int finalResultColStartIndex = resultColStartIndex;
                    if(outlineFlag) {
                        bw.write("|");
                        bw.write(getSpecificColumnFromInputFile2DArray(0).get(finalResultColStartIndex)
                                .toLowerCase()
                                .replaceAll(" ", "_"));
                        bw.write("|");
                        bw.newLine();
                    }
                    map.forEach((k, v) -> {
                        try {
                            bw.write("|" + v[finalResultColStartIndex]
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace(",", "|")
                                    + "|" + LINE_SEPARATOR);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case "filtered_map_by_colrange":
                    Map<Integer, String[]> treeMap = filterRowsByMap(inputFileAsTwoDimArr, predicate, queryColIndex);
                    int finalResultColStartIndex1 = resultColStartIndex;
                    int finalResultColEndIndex = resultColEndIndex;
                    if(outlineFlag) {
                        bw.write(getColumnRangeForFirstRowFromInputFile2DArray(resultColStartIndex, resultColEndIndex)
                                .toLowerCase()
                                .replaceAll(" ", "_"));
                        bw.newLine();
                    }
                    treeMap.forEach((k, v) -> {
                        try {
                            if(args[3] != null && args[3] instanceof Integer) {
                                if((int) args[3] > (int) args[2]) {
                                    int colSize = finalResultColEndIndex - finalResultColStartIndex1 + 1;
                                    int[] resultColRange = new int[colSize];
                                    for (int i = 0; i < resultColRange.length; i++) {
                                        resultColRange[i] = finalResultColStartIndex1 + i;
                                        bw.write("|" +  v[resultColRange[i]].stripLeading());
                                    }
                                    bw.write("|");
                                    bw.newLine();
                                }
                                else {
                                    throw new RuntimeException("The Result Column End Index cannot be lower than the Result Column Start Index");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
            }
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getSpecificColumnFromInputFile2DArray(int colIndex) {
        return Arrays.stream(inputFileAsTwoDimArr)
                .map(object -> object[colIndex])
                .collect(toList());
    }

    //TODO Review
    public List<String> getColRangeFromInputFile2DArray(int rangeStart, int rangeEnd) {
        int startRange = rangeStart;
        if (rangeStart <= 0) {
            startRange = 1;
        }
        // Mapping of the two-dim array with indices
        inputFileSubsetAsList.clear();
        int finalRangeStart = startRange;
        range(0, inputFileAsTwoDimArr.length)
                .flatMap(row -> range(finalRangeStart, rangeEnd)
                        .map(col -> {
                            inputFileSubsetAsList.add(inputFileAsTwoDimArr[row][col]);
                            return row;
                        }))
                .forEach(row -> {
                });
        return inputFileSubsetAsList;
    }

    public String[][] getColumnRangeFromInputFile2DArray(int rangeStart, int rangeEnd) {
        int colCount = (rangeEnd - rangeStart) + 1;
        try {
            inputFileSubsetAsTwoDimArr = new String[inputFileAsTwoDimArr.length][colCount];
            for(int r = 0; r < inputFileSubsetAsTwoDimArr.length; r++) {
                for(int c = 0; c < colCount; c++) {
                    inputFileSubsetAsTwoDimArr[r][c] = inputFileAsTwoDimArr[r][rangeStart + c].stripLeading();
                }
            }
            return inputFileSubsetAsTwoDimArr;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public String  getColumnRangeForFirstRowFromInputFile2DArray(int rangeStart, int rangeEnd) {
        int colCount = (rangeEnd - rangeStart) + 1;
        try {
            String row = "";
            for(int c = 0; c < colCount; c++) {
                row += "|" + inputFileAsTwoDimArr[0][rangeStart + c].stripLeading();
            }
            row += "|";
            return row;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public String getSpecificRowFromInputFile2DArray(int rowIndex) {
        try {
            String rowset = "";
            for(int i = 0; i < inputFileAsTwoDimArr[0].length; i++) {
                rowset += "|" + inputFileAsTwoDimArr[rowIndex][i].stripLeading();;
            }
            rowset += "|";
            return rowset;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[][] getRowRangeFromInputFile2DArray(int rangeStart, int rangeEnd) {
        int rowCount = (rangeEnd - rangeStart) + 1;
        try {
            inputFileSubsetAsTwoDimArr = new String[rowCount][inputFileAsTwoDimArr[0].length];
            for(int r = 0; r < rowCount; r++) {
                for(int c = 0; c < inputFileAsTwoDimArr[0].length; c++) {
                    inputFileSubsetAsTwoDimArr[r][c] = inputFileAsTwoDimArr[rangeStart + r][c].stripLeading();
                }
            }
            return inputFileSubsetAsTwoDimArr;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public List<ResultSelection> filterRowsByList(String[][] array, Predicate<String> predicate, int column) {
        return range(0, array.length)
                .filter(i -> predicate.test(array[i][column]))
                .mapToObj(i -> new ResultSelection(i, copyOf(array[i], array[i].length)))
                .collect(toList());
    }

    public Map<Integer, String[]> filterRowsByMap(String[][] array, Predicate<String> predicate, int column) {
        TreeMap<Integer, String[]> result = new TreeMap<>();
        for (int i = 0; i < array.length; i++) {
            if (predicate.test(array[i][column])) {
                result.put(i, copyOf(array[i], array[i].length));
            }
        }
        return result;
    }




    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getInputFileAsList() {
        return this.inputFileAsList;
    }

    public String getPartialInputFilePath() {
        String PARTIAL_INPUT_FILE_PATH = "\\src\\test\\resources\\input_data\\";
        if (System.getProperty("os.name").contains("Windows")) {
            return PARTIAL_INPUT_FILE_PATH;
        } else {
            return PARTIAL_INPUT_FILE_PATH.replaceAll("\\\\", "/");
        }
    }

    public String getPartialOutputFilePath() {
        String PARTIAL_OUTPUT_FILE_PATH = "\\src\\test\\resources\\features\\" + getExcelTab() + "\\";
        if (System.getProperty("os.name").contains("Windows")) {
            return PARTIAL_OUTPUT_FILE_PATH;
        } else {
            return PARTIAL_OUTPUT_FILE_PATH.replaceAll("\\\\", "/");
        }
    }

    public String getExcelTab() {
        return excelTab;
    }

    public void setExcelTab(String excelTab) {
        this.excelTab = excelTab;
    }

    public boolean isCleanseSwitch() {
        return cleanseSwitch;
    }

    public void setCleanseSwitch(boolean cleanseSwitch) {
        this.cleanseSwitch = cleanseSwitch;
    }

}









