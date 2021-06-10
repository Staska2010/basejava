package ru.topjava.basejava;

import java.io.File;

public class RecursiveDirTraversalWithIndents {
    public static void main(String[] args) {
        String path = "./src";
        File directory = new File(path);
        listFiles(directory, "");
    }

    private static void listFiles(File directory, String level) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File next : files) {
                if (next.isDirectory()) {
                    System.out.println(level + "-" + next.getName());
                    listFiles(next, level + "\t");
                } else {
                    System.out.println(level + next.getName());
                }
            }
        }
    }
}
