package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DataTable {

    @Getter
    @Setter
    private int numberOfRows = 0;

    @Getter
    @Setter
    private List<String> rows = new ArrayList<>();

}
