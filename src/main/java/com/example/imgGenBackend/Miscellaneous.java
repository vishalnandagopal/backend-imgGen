package com.example.imgGenBackend;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A class to hold miscellaneous functions like reading env files.
 *
 * @author Vishal N (vishalnandagopal.com)
 * @version 1.0
 * @since 2023-01-09
 */

public class Miscellaneous {
    /**
     * Common random generator for all random functions.
     */
    private final static Random randomGenerator = new Random();
    private final static String logFileName = "./imgGen backend.log";

    static {
        try {
            FileHandler fh = new FileHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String logToFile(String toLog) {
        logToFile(Level.FINE, toLog);
        return "";
    }

    public static void logToFile(Level level, String toLog) {
        final Logger logger = Logger.getLogger("imgGen Backend Logger");
        logger.setLevel(Level.INFO);
        logger.log(new LogRecord(level, toLog));
        logger.log(level, toLog);
    }

    /**
     * Reads the environmental variable file at the given filename, if it exists and returns the value of the given key; This checks the current directory, the parent directory and also one more level above it. Uses the {@link Dotenv} library
     *
     * @param filename The name of the .env file. Current one is "bmc.env" but it is not hardcoded.
     * @param key      The key to search in the env file.
     * @return The value of the key in the env file, or null if it does not exist.
     * @throws IOException If I/O error occurs
     */
    public static String readDotenv(String filename, String key) throws IOException {
        Dotenv dotenv;
        if (checkIfExists("./" + filename, false)) {
            filename = "./" + filename;
        }
        else if (checkIfExists("./../" + filename, false)) {
            filename = "./../" + filename;
        }
        else if (checkIfExists("./../../" + filename, false)) {
            filename = "./../../" + filename;
        }

        if (filename.length() > 0) {
            dotenv = Dotenv.configure().ignoreIfMissing().filename(filename).load();
        }
        else {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }
        return dotenv.get(key);
    }

    /**
     * Generates a Path object for the given file.
     *
     * @param filename - The relative or absolute path of the file as a string.
     * @return Path object of the given file.
     */
    public static Path generatePath(String filename) {
        return Paths.get(filename);
    }

    /**
     * Checks if the given path (as a string) exists, and provides ways to ensure it is created if it does not exist.
     *
     * @param stringPath        The absolute of relative path of the file, as a string. This is converted to a Path object by this method before performing anything.
     * @param createIfNotExists Create all the parent directories of the given path if it does not exist, and also an empty file with the given filename. Uses {@code Files.createFile()} and {@code Files.createDirectories()}) to achieve the same.;
     * @return {@code True} if it exists or if it did not exist but has been created now. {@code False} if it does not exist and was specified not to create it.
     * @throws IOException If I/O error occurs
     */
    public static boolean checkIfExists(String stringPath, boolean createIfNotExists) throws IOException {
        Path path = generatePath(stringPath);
        if (Files.exists(path)) {
            return true;
        }
        else if (createIfNotExists) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return true;
        }
        return false;
    }

    /**
     * Checks if the given path (as a string) exists and does NOT create it if it doesn't exist. Uses the {@link #checkIfExists(String, boolean)} method by providing false for the {@code createIfNotExists} argument.
     *
     * @param stringPath The absolute of relative path of the file, as a string. This is converted to a Path object by this method before performing anything.
     * @return {@code True} if it exists. {@code False} if it does not exist.
     * @throws IOException If I/O error occurs
     */
    public static boolean checkIfExists(String stringPath) throws IOException {
        return checkIfExists(stringPath, false);
    }

    /**
     * Generates a random number between 100000 and 999999
     *
     * @return A random number 6-digit number to be used as a zipID.
     */
    public static String generateRandomZipID() {
        return Integer.toString(randomGenerator.nextInt(900000) + 100000);
    }

    /**
     * Generates a alphanumeric string that can be used in the {@code export.manifest} and {@code data.json} files of a DWP page export.
     *
     * @return An alphanumeric string of length 8.
     */
    public static String generateRandomPageExportID() {
        final int PageExportIDLength = 8;
        final String letters = "abcdefghijklmnopqrstuvwxyz";
        final String numbers = "0123456789";
        final StringBuilder builder = new StringBuilder();
        builder.append(letters.charAt(randomGenerator.nextInt(letters.length())));
        for (int i = 1; i < PageExportIDLength; i++) {
            if (i < 5 || randomGenerator.nextBoolean()) {
                builder.append(letters.charAt(randomGenerator.nextInt(letters.length())));
            }
            else {
                builder.append(numbers.charAt(randomGenerator.nextInt(numbers.length())));
            }
        }
        return builder.toString();
    }

    /**
     * Get file size by searching for a file with the name {@code imageID} in the folder {@link PageExportBuilder#IMG_FOLDER_PATH IMG_FOLDER_PATH}
     *
     * @param imageID The ID of the image
     * @return
     */
    public static long getFileSize(String imageID) {
        return new File(PageExportBuilder.IMG_FOLDER_PATH + imageID).length();
    }

    /**
     * Generate a random alphanumeric string, can be used anywhere.
     * @return Alphanumeric string
     */
    public static String generateRandomFileName() {
        final int randomFileNameSuffixLength = 6;
        final String charsToUse = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final StringBuilder builder = new StringBuilder();
        builder.append(charsToUse.charAt(randomGenerator.nextInt(charsToUse.length())));
        for (int i = 1; i < randomFileNameSuffixLength; i++) {
            builder.append(charsToUse.charAt(randomGenerator.nextInt(charsToUse.length())));
        }
        return builder.toString();
    }
}