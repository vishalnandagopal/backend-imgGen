package com.example.imaggenbackend;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Miscellaneous {
    public static Path pathify(String filename, String directory) {
        return Paths.get(directory, filename);
    }

    public static boolean deleteFile(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public static boolean checkIfExistsInDirectory(Path path, boolean create_if_doesnt_exist)
            throws IOException {
        if (Files.exists(path)) {
            return true;
        } else if (create_if_doesnt_exist) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return true;
        }
        return false;
    }

    public static boolean saveToFile(InputStream rawBytes, Path path, boolean overwrite) throws IOException {
        // Use the Files.copy() method to copy the input stream to the target file
        if (checkIfExistsInDirectory(path, true)) {
            Files.copy(rawBytes, path);
            rawBytes.close();
            return true;
        } else {
            return false;
        }
    }
}
