package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataTable {
    @Getter
    @Setter
    private String path = "";
    @Getter
    @Setter
    private int startRowIndex = 0;
    @Setter
    private int endRowIndex = 0;
    @Getter
    private int rowCount = this.countRows();
    @Getter
    private String header = "";
    @Getter
    private final List<String> rows = new ArrayList<>();
    public String getDataTableHeader() {
        return this.rows.get(0);
    }
    public void addHeader(String line) {
        this.header = line;
    }
    public void addRow(String line) {
        this.rows.add(line);
    }
    private int countRows() {
        return this.rowCount = this.rows.size() - this.startRowIndex;
    }

}
