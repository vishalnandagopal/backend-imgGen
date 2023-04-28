package com.example.imaggenbackend;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "images")
class Image {
    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "file")
    private Blob file;

//    Unused getters and Setters --------------------------------
//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public byte[] getFile() {
//        return file;
//    }
//
//    public void setFile(byte[] file) {
//        this.file = file;
//    }
}


@RestController
public class SaveImage {

    private static Blob blobFromUrl(URL imageUrl) {

        /**
         * Download the image from given URL and return the blob of the image.
         */

        try {

            InputStream inputStream = imageUrl.openStream();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = baos.toByteArray();

            return new javax.sql.rowset.serial.SerialBlob(imageBytes);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    private static boolean store(String filename, URL fileURL, String UUID) {

        /**
         * Read the database Credentials from env file.
         */

        Dotenv dotenv;
        String envFileName = "";
        String databaseUrl = "";
        String uname = "";
        String pwd = "";


        try {
            dotenv = getDotenv(envFileName);

            databaseUrl = dotenv.get("databaseurl");
            uname = dotenv.get("uname");
            pwd = dotenv.get("pwd");


        } catch (IOException e) {
            System.out.println("ENV File / Database Credentials not set properly.");
            e.printStackTrace();
            return false;
        }


        /**
         * Connect to the database and insert the images, uuid, name, etc. into the database.
         */

        Connection conn;
        Statement stmt;
        ResultSet rs;
        String updateQuery;

        try {
            conn = DriverManager.getConnection(databaseUrl, uname, pwd);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM images");

            while (rs.next()) {
                System.out.println("File name: " + rs.getString("name"));
            }
            System.out.println("Query Completed.");

            updateQuery = "INSERT INTO images VALUES (?, ?, ?)";

            PreparedStatement prepStmt = conn.prepareStatement(updateQuery);

            Blob blob = blobFromUrl(fileURL);
            if (blob == null) {
                throw new Exception("Couldnt Retrieve Blob from URL");
            }

            prepStmt.setString(1, UUID);
            prepStmt.setString(2, filename);
            prepStmt.setBlob(3, blob);

            prepStmt.executeUpdate();

            System.out.println("File Stored Successfully");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @NotNull
    static Dotenv getDotenv(String envFileName) throws IOException {
        Dotenv dotenv;
        if (Miscellaneous.checkIfExistsInDirectory(Miscellaneous.generatePath("./", "bmc.env"), false)) {
            envFileName = "./bmc.env";
        } else if (Miscellaneous.checkIfExistsInDirectory(Miscellaneous.generatePath("./../", "bmc.env"), false)) {
            envFileName = "./../bmc.env";
        }

        if (envFileName.length() > 0) {
            dotenv = Dotenv.configure().ignoreIfMissing().filename(envFileName).load();
        } else {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }
        return dotenv;
    }


    /**
     * Define the API that will accept image details from the user and store it in database.
     *
     * @param formData contains { name: "imgname", url: "imgurl"}
     */

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/saveImage")
    public void saveImage(@RequestBody Map<String, String> formData) {

//        System.out.println("test123123");

        String name = formData.get("name");
        URL url;


        /**
         * Try to build an URL object from the received URL.
         * If invalid, Notify the user, and stop the further execution of the code.
         */

        try {
            url = new URL(formData.get("url"));
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL.");
            e.printStackTrace();
            return;
        }


        /**
         * Construct a Global Unique Identifier to identify each image blob.
         */

        String uuidString = UUID.randomUUID().toString();

        try {
            if (!store(name, url, uuidString)) {
                throw new Exception("File Saving Failed.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("Image Saved Successfully,  Name: " + name + " URL: " + url + " UUID: " + uuidString);
    }

}
