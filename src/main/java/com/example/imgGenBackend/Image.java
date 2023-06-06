package com.example.imgGenBackend;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

/**
 * A class to store images. Create an image by passing the URL. It will automatically be downloaded and assigned to an ID.
 *
 * @author Vishal N (vishalnandagopal.com)
 * @version 1.0
 * @since 2023-01-09
 */
final class Image {
    String id;
    String src;
    String generatedFrom;

    /**
     * Constructor for Image class. Automatically downloads the image using the {@link Image#downloadImage()} method. This constructor must be used if you are dealing with Dall-E's response since it returns the URLs of the image unlike Stability AI, which returns base64 encoded strings of the images.
     *
     * @param url The url of the image returned by Dall-e, as a URL object
     * @throws IOException If I/O error occurs
     */
    public Image(URL url, String generatedFrom) throws IOException {
        this.id = UUID.randomUUID().toString();
        this.generatedFrom = generatedFrom;
        this.src = url.toString();

        // Downloads the image since it is a URL.
        this.downloadImage();
    }

    /**
     * Constructor for Image class. Automatically downloads the image using the {@link #downloadImage()} () downloadImage} ()} method. This constructor must be used if you are dealing with Stability AI's response since it returns the base64 encoded strings unlike Stability AI, which returns URLs of the images.
     *
     * @param base64EncodedImage Base64 encoded strings of the images
     * @throws IOException If I/O error occurs
     */
    public Image(String base64EncodedImage, String generatedFrom) throws IOException {
        this.id = UUID.randomUUID().toString();
        this.generatedFrom = generatedFrom;
        this.src = base64EncodedImage;

        // Save the image instead of downloading it, since the response is the base64 of the image
        this.saveImage();
    }

    /**
     * Static method for saving an image when given the Base-64 encoded version of it. Decodes it and writes it to a file with the same name as the image's UUID.
     *
     * @param base64EncodedImage The base-64 encoded string of the image
     * @param uuid               The image's UUID, the one to use as the filename while saving it
     * @throws IOException If there is an error when writing the bytes of the image to the file
     */
    private static void saveImage(String base64EncodedImage, String uuid) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64EncodedImage);
        File file = new File(PageExportBuilder.IMG_FOLDER_PATH + uuid);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(imageBytes);
        }
        System.out.println("Image saved successfully!");
    }

    /**
     * A static method for automatically downloading the URL and assigning it to the given ID. To download an image without providing the ID, you can offer call the method with just the URL.
     *
     * @param imageURL The URL of the image to download.
     * @param uuid     The ID to use for the image.
     */
    private static void downloadImage(URL imageURL, String uuid) {
        Thread threadForDownloadingImage = new Thread(() -> {
            File file = new File(PageExportBuilder.IMG_FOLDER_PATH + uuid);
            try {
                Miscellaneous.checkIfExists(PageExportBuilder.IMG_FOLDER_PATH, true);
                try (InputStream inputStream = imageURL.openStream(); OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[2048];
                    int length;

                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Downloaded " + imageURL + " - " + uuid);
        });
        threadForDownloadingImage.start();
    }


    /**
     * Non-static method for calling the static {@link Image#downloadImage(URL, String)} method. Just think {@code this.downloadImage()} looks cleaner
     *
     * @throws IOException If there is any error when downloading the image
     */
    public void downloadImage() throws IOException {
        Image.downloadImage(new URL(this.src), this.id);
    }

    /**
     * Non-static method for calling the static {@link Image#saveImage(String, String) saveImage()} method. Just think {@code this.saveImage()} looks cleaner
     *
     * @throws IOException if there is any error when writing the image to disk
     */
    public void saveImage() throws IOException {
        Image.saveImage(this.src, this.id);
    }

    /**
     * Custom toString() method
     *
     * @return ID & where it is generated from
     */
    @Override
    public String toString() {
        return String.format("%s - %s", this.id, this.generatedFrom);
    }

    // Getters and setters 👇. Must set otherwise spring cannot serialize this object when a class in a response. Very useful since do not have to build custom JSON, and it automatically serializes it and creates and sends a JSON as a response. So now, can parse the JSON response and do parsedResponse.src, parsedResponse.id, etc.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getGeneratedFrom() {
        return generatedFrom;
    }

    public void setGeneratedFrom(String generatedFrom) {
        this.generatedFrom = generatedFrom;
    }
}
