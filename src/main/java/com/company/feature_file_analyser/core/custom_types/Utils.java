package com.company.feature_file_analyser.core.custom_types;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    private final List<String> listOfString = new ArrayList<>();
    public List<String> getListOfString() {
        listOfString.clear();
        return listOfString;
    }

}
