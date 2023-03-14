package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Step {
    @Getter
    @Setter
    private String Path = "";
    @Getter
    @Setter
    private String stepName = "";
    @Getter
    @Setter
    private String stepType = "";
    @Getter
    @Setter
    private int lineNumber = 0;
    @Getter
    @Setter
    private boolean isBackground = false;
    @Getter
    @Setter
    private boolean isDataDriven = false;
    @Getter
    @Setter
    private boolean isDataTableDriven = false;
    @Getter
    @Setter
    private DataTable dataTable = null;
    public void setDataTableProperties(int rowStart, int rowEnd) {
        this.dataTable.setStartRowIndex(rowStart);
        this.dataTable.setEndRowIndex(rowEnd);
    }
}