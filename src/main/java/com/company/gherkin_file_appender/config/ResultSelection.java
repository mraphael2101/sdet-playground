package com.company.gherkin_file_appender.config;

import java.util.Arrays;

public class ResultSelection {
    private final int rowIndex;
    private final String[] row;

    public ResultSelection(int rowIndex, String[] row) {
        this.rowIndex = rowIndex;
        this.row = row;
    }


    // Only executed when the class is instantiated
    @Override
    public String toString() {
        return Arrays.toString(row);
    }


    /***
     @Override
     public String toString() {
     return String.format("%d: %s", rowIndex, Arrays.toString(row));
     }
     ***/

}