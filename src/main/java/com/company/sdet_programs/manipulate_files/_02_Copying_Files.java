package com.company.sdet_programs.manipulate_files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class _02_Copying_Files {

    /***
     Recursive copying: The copyDirectoryRecursively method handles both files and directories recursively,
     ensuring that all files and subdirectories are copied.
     File channel copying: The copyFile method uses FileChannel to copy files efficiently, especially for large files.
     Error handling: The code includes basic error handling to catch potential exceptions during file operations.
     Customizable paths: You can modify the sourceDirectory and destinationDirectory variables to specify the
     desired source and destination paths.
     */

    private static void copyDirectoryRecursively(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();
            for (String file : files)
            {
                copyDirectoryRecursively(new File(source, file), new File(destination, file));
            }
        } else {
            copyFile(source, destination);
        }
    }

    private static void copyFile(File source, File destination) throws IOException {
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(destination)) {
            FileChannel inChannel = in.getChannel();
            FileChannel outChannel = out.getChannel();


            long size = inChannel.size(); // Declare size outside the try block
            long pos = 0;
            while (pos < size) {
                pos += inChannel.transferTo(pos, size - pos, outChannel);
            }
        }
    }
}

