package ru.topjava.basejava;

import java.io.File;
import java.util.*;

public class RecursiveDirTraversal {
    public static void main(String[] args) {
        String path = "./";
        File directory = new File(path);
        Stack<File> stack = new Stack<>();
        listFiles(directory, stack);
    }

    private static void listFiles(File directory, Stack<File> stack) {
        stack.addAll(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
        File next;
        while(!stack.isEmpty()) {
            next = stack.pop();
            if (next.isDirectory()) {
                listFiles(next, stack);
            } else {
                System.out.println(next.getName());
            }
        }
    }
}
