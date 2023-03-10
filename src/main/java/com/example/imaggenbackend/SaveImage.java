package com.example.imaggenbackend;

import java.io.InputStream;
import java.io.IOException;

public class SaveImage {
    public static void save(String remoteImgURL) throws IOException {

        InputStream rawBytesOfImage = HTTPCaller.fetch(remoteImgURL, "", "", "", "");
        try {

        } finally { // Close the input stream
            rawBytesOfImage.close();
        }
        System.out.println("Saved image to ");
    }

    public static void main(String[] args) throws IOException {
        SaveImage.save("https://idjh.files.wordpress.com/2011/12/google-ics.jpg");
    }
    
}