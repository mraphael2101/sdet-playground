package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Project {

    @Getter
    private Set<String> setOfDistinctPathsString = null;

    public void initialiseSetOfDistinctPathsString(List<String> value) {
        this.setOfDistinctPathsString = new HashSet<>(value);
    }
}
