package com.example.ncdemo;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "blobDemo", value = "/blob-demo")
public class Blob extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Play with Blob :)</h1>");
        out.println("</body></html>");

        DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder().build();

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://javaazurenthuy.blob.core.windows.net/")
                .credential(defaultCredential)
                .buildClient();

        // Create a unique name for the container
        String containerName = "container" + java.util.UUID.randomUUID();

        // Create the container and return a container client object
        BlobContainerClient blobContainerClient = blobServiceClient.createBlobContainer(containerName);
        out.println("<p>Container created!</p>");

        String localPath = "./data";
        String fileName = "blob" + java.util.UUID.randomUUID() + ".txt";

        // get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        FileWriter writer = null;
        try
        {
            writer = new FileWriter(localPath + fileName, true);
            writer.write("Hello, World!");
            writer.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

        blobClient.uploadFromFile(localPath + fileName);
    }
}
