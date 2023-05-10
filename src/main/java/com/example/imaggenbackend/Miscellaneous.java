package com.example.imaggenbackend;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Miscellaneous {
    public static String readDotenv(String filename, String key, boolean relativePathToFile) throws IOException {
        Dotenv dotenv;
        if (checkIfExists(Miscellaneous.generatePath("./", filename), false)) {
            filename = "./" + filename;
        } else if (checkIfExists(Miscellaneous.generatePath("./../", "bmc.env"), false)) {
            filename = "./../" + filename;
        }

        if (filename.length() > 0) {
            dotenv = Dotenv.configure().ignoreIfMissing().filename(filename).load();
        } else {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }
        return dotenv.get(key);
    }

    public static Path generatePath(String filename, String directory) {
        return Paths.get(directory, filename);
    }

    public static boolean deleteFile(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public static boolean checkIfExists(Path path, boolean createIfDoesntExist) throws IOException {
        if (Files.exists(path)) {
            return true;
        } else if (createIfDoesntExist) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return true;
        }
        return false;
    }

    public static boolean saveToFile(InputStream rawBytes, Path path) throws IOException {
        // Use the Files.copy() method to copy the input stream to the target file
        if (checkIfExists(path, true)) {
            Files.copy(rawBytes, path);
            rawBytes.close();
            return true;
        } else {
            return false;
        }
    }

    public static void saveImage(String remoteImgURL) throws IOException {

        try (InputStream rawBytesOfImage = HTTPCaller.fetch(remoteImgURL, "", "", "", "")) {

        } // Close the input stream

        System.out.println("Saved image to ");
    }


    public static int generateRandomZipID() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generates a random number between 100000 and 999999
    }

    public static String generateRandomPageExportID() {
        int PageExportIDLength = 8;
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        builder.append(letters.charAt(random.nextInt(letters.length())));
        for (int i = 1; i < PageExportIDLength; i++) {
            if (i < 5 || random.nextBoolean()) {
                builder.append(letters.charAt(random.nextInt(letters.length())));
            } else {
                builder.append(numbers.charAt(random.nextInt(numbers.length())));
            }
        }
        return builder.toString();
    }
}