package common;

import java.io.*;
import java.util.ArrayList;

import static settings.SeleniumListener.LOG;

public class FileEditor {

    private static ArrayList<String> list = new ArrayList<>();

    public static void updateFile(String filePath, String update) {
        LOG.info("Try to update file with values: " + update);
        readFromFile(filePath);
        list.set(1, update);
        writeToFile(filePath);
        LOG.info("Successfully updated");
    }

    public static ArrayList<String> readFromFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } return list;
    }

    private static void writeToFile(String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (String x : list) {
                writer.write(x);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
