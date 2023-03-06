package com.example.imaggenbackend;

import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveImage {
    public static void save(String remoteImgURL) throws IOException {

        InputStream rawBytesOfImage = HTTPCaller.fetch(remoteImgURL, "", "", "", "");
        try {
            // Create a Path object for the target file in the local directory
            Path path = Paths.get("images", "image.png");

            // Create the necessary directories if they don't exist
            Files.createDirectories(path.getParent());

            // Create the target file if it doesn't exist
            if (Files.exists(path)) {
                Scanner sc = new Scanner(System.in);
                try {
                    System.out
                            .print("A file already exists with the given filename. Do you want to overwrite it? [Y/n]? :");
                    String a = sc.nextLine();

                    if (a == "Y" || a == "" || a == "y") {
                        System.out.println(path);
                        rawBytesOfImage.close();
                        boolean deleted = Files.deleteIfExists(path);
                        if (deleted) {
                            System.out.println("File deleted successfully");
                        }
                    }
                    Files.createFile(path);
                } finally {
                    sc.close();
                }
            }

            // Use the Files.copy() method to copy the input stream to the target file
            Files.copy(rawBytesOfImage, path);
        } finally { // Close the input stream
            rawBytesOfImage.close();
        }
        System.out.println("Saved image to ");
    }

    public static void main(String args[]) throws IOException {
        SaveImage.save("https://idjh.files.wordpress.com/2011/12/google-ics.jpg");
    }
    
}