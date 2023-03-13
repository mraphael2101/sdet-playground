package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    @Getter
    private final List<String> listOfString = new ArrayList<>();

    public void clearListOfString() {
        this.listOfString.clear();
    }

    public void addString(String value) {
        this.listOfString.add(value);
    }

}
