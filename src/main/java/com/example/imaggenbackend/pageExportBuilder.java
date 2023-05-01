package com.example.imaggenbackend;

import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.zip.*;


public class pageExportBuilder {

    static Map<String, String> mp = new HashMap<String, String>();

    /**
     * Replace the placeholdders in json with image UUIDs.
     *
     * @param replacements
     * @param filepath
     * @return replacedJson
     */
    public static String replace(Map<String, String> replacements, String filepath) {

        String content;

        try {

            content = new String(Files.readAllBytes(new File(filepath).toPath()));

            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                content.replace(entry.getValue(), entry.getKey());
            }
        } catch (IOException e) {
            System.out.println("Text File Reading Failed.");
            e.printStackTrace();
            return null;
        }

        return content;
    }


    /**
     * Prepare the Zip archive of all the image files, data.json and manifest.
     *
     * @param folderPath
     * @throws IOException
     */
    public static void export(String folderPath) throws IOException {

        /**
         * Create a FileOutputStream for the zip file.
         * Create a ZipOutputStream from the FileOutputStream
         * Get a list of all the files in the folder
         */
        FileOutputStream fos = new FileOutputStream(folderPath + File.separator + "export.zip");

        ZipOutputStream zos = new ZipOutputStream(fos);

        File folder = new File(folderPath);
        File[] files = folder.listFiles();


        /**
         * Loop through the files and add them to the zip file
         * Create a new ZipEntry for the file
         * Add the ZipEntry to the ZipOutputStream
         * Create a FileInputStream for the file
         * Read the file into a buffer and write it to the ZipOutputStream
         */

        for (File file : files) {
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);

            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            fis.close();
        }

        zos.close();

        System.out.println("Files Exported successfully.");
    }

    public static void main(String[] args) throws IOException {

        /**
         * Read all files of the images folder.
         * Rename the images to their UUID.
         * Store the images in new folder named export.
         * Read the json files from template directory.
         * edit the json files and write in the new location.
         */
    }

    public static void prepareFiles() throws IOException {

        File imageDirectory = new File("D:\\BMC_Internship\\Coding\\ImagGenBackend\\artifacts\\images");
        File[] allFiles = imageDirectory.listFiles();

        int i = 0;

        for (File file : allFiles) {

            String extension = getFileExtension(file.getName());
            String filename = getFileName(file.getName());

            if (extension == "png" || extension == "jpg" || extension == "jpeg") {

                /**
                 * Currently, I am just creating new UUID here.
                 * Later, the UUID has to be fetched from the database using the image_name
                 * It is essential for consistency and minimize redundancy because UUID is PK.
                 */
                String uuid = UUID.randomUUID().toString();

                String newFileName = uuid;

                mp.put(uuid, "image_placeholder_" + i);
                i++;

                Path source = Paths.get(imageDirectory.getAbsolutePath() + File.separator + filename + '.' + extension);
                Path destination = Paths.get("D:\\BMC_Internship\\Coding\\ImagGenBackend\\artifacts\\export" + File.separator + uuid);

                Files.move(source, destination);
            } else {
                mp.put(file.getName(), "image_placeholder_" + i);
                i++;
            }

        }

        /**
         *  Read the json file and substitute UUID values
         */
        String jsonFilePath = "D:\\BMC_Internship\\Coding\\ImagGenBackend\\templates\\template1\\data.json";

        String newText = replace(mp, jsonFilePath);

        File newJsonFile = new File("D:\\BMC_Internship\\Coding\\ImagGenBackend\\artifacts\\export\\data.json");
        newJsonFile.createNewFile();

        new FileWriter(newJsonFile).write(newText);

        /**
         * Read the manifest file and substitute ID
         */
        String manifestFilePath = "D:\\BMC_Internship\\Coding\\ImagGenBackend\\templates\\template1\\export.manifest";
        String oldManifest = "";

        try {

            oldManifest = new String(Files.readAllBytes(new File(manifestFilePath).toPath()));
            String newManifest = oldManifest.replace("manifest_id_placeholder", UUID.randomUUID().toString().substring(0, 8));

            File newManifestFile = new File("D:\\BMC_Internship\\Coding\\ImagGenBackend\\artifacts\\export\\export.manifest");
            newManifestFile.createNewFile();
            new FileWriter(newManifestFile).write(newManifest);

        } catch (IOException e) {
            System.out.println("cant prepare Manifest File.");
            e.printStackTrace();
            return;
        }

        export("D:\\BMC_Internship\\Coding\\ImagGenBackend\\artifacts\\export");

    }


    private static String getFileExtension(String fileName) {

        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(index + 1);
        } else {
            return "";
        }
    }

    private static String getFileName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(0, index);
        } else {
            return "";
        }
    }


    static byte[] getByteContentFromFile(String filePathString) {

        try {

            return Files.readAllBytes(Paths.get(filePathString));

        } catch (Exception e) {

            System.out.println("Unable to read bytes from the file: " + filePathString);
            e.printStackTrace();

            return null;
        }
    }


    static String getStringContentFromFile(String filePathString) {

        try {

            return Files.readString(Paths.get(filePathString), Charset.defaultCharset());

        } catch (Exception e) {

            System.out.println("Unable to read file: " + filePathString + " as a String.");
            e.printStackTrace();

            return null;
        }

    }

    static String replaceOneTokenInAFile(String filePathString, String originalString, String finalString){

        String filecontentString = null;
        try{

            byte[] fileContentByteArray = Files.readAllBytes(Paths.get(filePathString));

            filecontentString = new String(fileContentByteArray, Charset.defaultCharset()).replaceAll(originalString, finalString);

            Files.write(Paths.get(filePathString), fileContentByteArray);

            return filecontentString;

        }
        catch(IOException e){

            e.printStackTrace();

        }

        return filecontentString;

    }

    static String replaceAllTokensInAFile(String filePathString, Map<String, String> replacements) {

        String filecontentString = null;

        try {

            byte[] fileContentByteArray = Files.readAllBytes(Paths.get(filePathString));
            filecontentString = new String(fileContentByteArray, Charset.defaultCharset());

            for (Map.Entry<String, String> entry : replacements.entrySet()) {

                filecontentString = filecontentString.replaceAll(entry.getKey(), entry.getValue());

            }

            fileContentByteArray = filecontentString.getBytes(Charset.defaultCharset());
            Files.write(Paths.get(filePathString), fileContentByteArray);

        } catch (IOException e) {

            System.out.println("File Reading/Writing Error.");
            e.printStackTrace();
            return null;

        }
        catch(Error e1){
            System.out.println("Other Exception while replacing file contents.");
            e1.printStackTrace();
        }

        return filecontentString;
    }

}
