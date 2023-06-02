package com.example.imgGenBackend;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Helps you prepare a page export. Handles exporting {@code data.json},{@code export.manifest} and generating the zip file for Innovation Studio
 *
 * @author Vishal N (vishalnandagopal.com)
 * @version 1.0
 * @since 2023-01-09
 */

public class PageExportBuilder {

    /**
     * The path to the folder where images must be saved
     */
    static final String IMG_FOLDER_PATH = new File("./images/").getAbsolutePath() + "/";

    /**
     * This is the path where the temp files are stored on the respective OS. It can be used to store the zip files temporarily.
     */
    static final String sysTmpDirectory = System.getProperty("java.io.tmpdir");

    /**
     * The path where the zip files will be stored. They will be stored in the system's temp directory since it is not necessary for them to be available permanently..
     */
    static final String ZIP_FOLDER_PATH = new File(sysTmpDirectory + "/imgGen/ZIP Files/").getAbsolutePath() + "/";


    /**
     * Iterates through imageURLs array, creates Image object for each URL provided and returns an array of their Image objects..
     *
     * @param imageURLs - An ArrayList of URLs as strings.
     * @return An {@link ArrayList} of {@link Image} objects.
     * @throws IOException If an I/O issue occurs when downloading the image, which is done automatically when creating an {@code Image} object
     */
    public static ArrayList<Image> dallEImageRequestResponse(ArrayList<String> imageURLs) throws IOException {

        ArrayList<Image> images = new ArrayList<>();

        for (String imageURL : imageURLs) {
            images.add(new Image(new URL(imageURL), "d"));
        }

        return images;
    }

    /**
     * Iterates through all the images returned by Stability AI,, creates Image object for each base64 image provided and returns an array of their Image objects..
     *
     * @param base64EncodedStrings The base64 encoded strings of the images as an {@link ArrayList}
     * @return An {@link ArrayList} of {@link Image} objects.
     * @throws IOException
     */
    public static ArrayList<Image> stabilityAIImageRequestResponse(ArrayList<String> base64EncodedStrings) throws IOException {

        ArrayList<Image> images = new ArrayList<>();

        for (String base64EncodedString : base64EncodedStrings) {
            images.add(new Image(base64EncodedString, "s"));
        }

        return images;
    }

    /**
     * A method that generates a ZIP file and keeps it ready in the {@link #ZIP_FOLDER_PATH} so that it can be called via the {@code /getzip API}.
     *
     * @param imageIDs The IDs of the images to include in the zip export
     * @return ZipID
     * @throws IOException If I/O error occurs
     */
    public static String generateZipFile(ArrayList<String> imageIDs, String pageDescription) throws IOException {

        String zipID = Miscellaneous.generateRandomZipID();
        String exportID = Miscellaneous.generateRandomPageExportID();

        String zipFilePath = ZIP_FOLDER_PATH + zipID + ".zip";

        if (imageIDs.size() != 4) {
            throw new IllegalArgumentException(String.format("The current template only supports generating exports when you give 4 image IDs. you have request an export for %d image(s).", imageIDs.size()));
        }
        for (String imgID : imageIDs) {
            if (!Miscellaneous.checkIfExists(IMG_FOLDER_PATH + imgID)) {
                throw new NoSuchElementException(String.format("Requested image ID \"%s\" is not present in the downloaded images directory so cannot return it", imgID));
            }
        }

        // Generate data.json
        String filecontentString = JSONPreparer.processDataJson(imageIDs, exportID, "vishal-page" + Miscellaneous.generateRandomFileName(), pageDescription);
        String dataFilePath = IMG_FOLDER_PATH + "data.json";
        FileWriter fileWriter = new FileWriter(dataFilePath);
        fileWriter.write(filecontentString);
        fileWriter.close();

        // Generate export.manifest
        String exportManifestString = JSONPreparer.processExportManifest(exportID);
        String exportFilePath = IMG_FOLDER_PATH + "export.manifest";
        fileWriter = new FileWriter(exportFilePath);
        fileWriter.write(exportManifestString);
        fileWriter.close();
        // Zip the relevant images and store as a file in ZIP_FOLDER_PATH
        Miscellaneous.checkIfExists(zipFilePath, true);
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);

        // Create an ArrayList of files that needed to be added to this current zip export, and add the data.json and export.manifest file to it initially.
        ArrayList<File> filesToAddToZip = new ArrayList<>() {
            {
                add(new File(IMG_FOLDER_PATH + "export.manifest"));
                add(new File(IMG_FOLDER_PATH + "data.json"));
            }
        };

        // Add all images to the filesToAddToZip, since they have to be exported
        for (String imgID : imageIDs) {
            filesToAddToZip.add(new File(IMG_FOLDER_PATH + imgID));
        }

        for (File file : filesToAddToZip) {
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, bytesRead);
                }
                fis.close();
                zos.closeEntry();
            }
        }
        System.out.println(zipID);
        zos.close();
        System.out.println(zipID + " - generated zip with this id");
        return zipID;
    }

    /**
     * Get the absolute path to the zip file for the given zip ID.
     *
     * @param zipID The ID with which the zip is stored with.
     * @return The absolute path of the zip file.
     */
    public static String getZipPath(String zipID) throws IOException {
        // Return the absolute path of the zip file
        String zipFilePath = ZIP_FOLDER_PATH + zipID + ".zip";
        if (!Miscellaneous.checkIfExists(zipFilePath, false)) {
            throw new FileNotFoundException(String.format("Zip file with ID %s not found", zipID));
        }
        return new File(zipFilePath).getAbsolutePath();
    }
}
