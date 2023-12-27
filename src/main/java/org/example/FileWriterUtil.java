package org.example;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterUtil implements FileWriterUtilInterface {
    @Override
    public void appendToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(content + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
