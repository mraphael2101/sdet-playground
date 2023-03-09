package com.company.sdet_programs.fileswalk;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _01_FilesWalk_Example {
    private static final String userDir = System.getProperty("user.dir");
    private static final String inputFilePath = "/src/test/resources/features/";

    public static void main(String[] args) throws IOException {
        Path path = Paths.get(userDir + inputFilePath);
        List<Path> paths = listFiles(path);
        paths.forEach(x -> System.out.println(x));
    }

    public static Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException {
        return walk(start, Integer.MAX_VALUE, options);
    }

    // list all files from this path
    public static List<Path> listFiles(Path path) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }

}
