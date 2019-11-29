package ru.reactiveturtle.reactiveexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AppHelper {
    public static File[] getSrcFilesRecursive(File[] filesBuffer) {
        ArrayList<File> filesList = new ArrayList<>();
        for (File file : filesBuffer) {
            if (file.isDirectory()) {
                if (file.listFiles().length > 0) {
                    filesList.addAll(Arrays.asList(getSrcFilesRecursive(file.listFiles())));
                } else {
                    filesList.add(file);
                }
            } else {
                filesList.add(file);
            }
        }
        File[] result = new File[filesList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = filesList.get(i);
        }
        return result;
    }

    public static File[] getSrcFiles(File[] buffer) {
        ArrayList<File> filesList = new ArrayList<>(Arrays.asList(buffer));
        ArrayList<File> filesListResult = new ArrayList<>();
        for (File file : filesList) {
            if (file.isDirectory()) {
                if (file.listFiles().length > 0) {
                    filesList.addAll(Arrays.asList(file.listFiles()));
                } else{
                    filesListResult.add(file);
                }
            } else {
                filesListResult.add(file);
            }
        }
        filesList.clear();
        File[] result = new File[filesListResult.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = filesListResult.get(i);
        }
        return result;
    }

    public static File[] getDstFiles(File[] buffer, String dstFolderPath) {
        return new File[0];
    }
}
