package ru.topjava.basejava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class MainFile {
    private static final Logger log = Logger.getLogger(MainFile.class.getName());

    public static void main(String[] args) {
        File file = new File("./.gitignore");
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException ex) {
            log.warning(ex.getMessage());
            throw new RuntimeException("get path error", ex);
        }

        File dir = new File("src/ru/topjava/basejava");
        System.out.println(dir.isDirectory());
        for (File next : Objects.requireNonNull(dir.listFiles())) {
            System.out.println(next);
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String next;
            while ((next = bufferedReader.readLine()) != null) {
                System.out.println(next);
            }
        } catch (IOException ex) {
            log.warning(ex.getMessage());
            throw new RuntimeException("Error", ex);
        }
    }
}
