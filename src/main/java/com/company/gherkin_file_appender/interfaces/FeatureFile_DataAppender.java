package com.company.gherkin_file_appender.interfaces;

public interface FeatureFile_DataAppender {

    String[][] readCleanseDataSourceFileInto2DArray(String fileName, boolean cleanseSwitch);

    boolean copyFeatureFile(String fileName);

    /***
     Example usages:
     a) "scenario", "row",                      Object[] args = { 3 }
     b) "scenario", "rowrange",                 Object[] args = { 1 , 5 }
     c) "scenario", "column",                   Object[] args = { 1 }
     d) "outline",  "colrange",                 Object[] args = { 1 , 3 }
     e) "outline",  "filtered_list",            Object[] args = { predicate, intQueryColIndex }
     f) "outline",  "filtered_map_by_col",      Object[] args = { predicate, intQueryColIndex, intResultColIndex }
     g) "outline",  "filtered_map_by_colrange", Object[] args = { predicate, intQueryColIndex, intResultStartColIndex, intResultEndColIndex }
     ***/
    boolean appendDataToNewFeatureFile(String scenarioType, String mode, Object... args);

}
