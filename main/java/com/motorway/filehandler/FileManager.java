package com.motorway.filehandler;

import java.io.*;

public class FileManager {
    private static final String DATA_DIR = "data/";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Data directory created: " + DATA_DIR);
        }
    }

    public static void saveToFile(Object obj, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + filename))) {
            oos.writeObject(obj);
            System.out.println("Saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving: " + e.getMessage());
        }
    }

    public static Object loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_DIR + filename))) {
            System.out.println("Loaded from: " + filename);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("ℹ File not found (first run): " + filename);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading: " + e.getMessage());
            return null;
        }
    }

    public static boolean fileExists(String filename) {
        return new File(DATA_DIR + filename).exists();
    }
}