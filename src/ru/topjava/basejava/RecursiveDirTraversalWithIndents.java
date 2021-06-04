package ru.topjava.basejava;

import java.io.File;

public class RecursiveDirTraversalWithIndents {
    public static void main(String[] args) {
        String path = "./src";
        File directory = new File(path);
        listFiles(directory, 0);
    }

    private static void listFiles(File directory, int level) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File next : files) {
                if (next.isDirectory()) {
                    System.out.println("\t".repeat(level) + "-" + next.getName());
                    listFiles(next, level + 1);
                } else {
                    System.out.println("\t".repeat(level) + next.getName());
                }
            }
        }
    }
}
