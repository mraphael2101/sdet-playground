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
    private int startIndex = 0;

    @Getter
    @Setter
    private int endIndex = 0;

    @Getter
    @Setter
    private int rowCount = 0;

    @Getter
    @Setter
    private int columnCount = 0;

    @Getter
    @Setter
    private List<String> rows = new ArrayList<>();

}
