package com.courpedia.crackjeepro.Services;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;

public class S3ImageProcessor implements Runnable{

    public static boolean flag=false;
    private static final String BUCKET_NAME = "crackjeepro";

    private static Bot bot=new Bot();
    private static S3Client s3Client;
    private static final String AWS_ACCESS_KEY = "";
    private static final String AWS_SECRET_KEY = "";
    public S3ImageProcessor() {
        s3Client = getS3Client();
    }

    public static S3Client getS3Client() {
        s3Client = S3Client.builder()
                .region(Region.US_EAST_1) // Set your region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
                .build();

        return s3Client;
    }

    public static void closeClient() {
        s3Client.close();
    }

    public static void listAndProcessImages()  {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder().bucket(BUCKET_NAME).build();
        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        for (S3Object s3Object : listObjectsResponse.contents()) {
            if(flag) return ;
            String key = s3Object.key();
            File downloadedImage = downloadImage(s3Client, key);
            if (downloadedImage != null) {
                send(downloadedImage);
                // Delete the downloaded image locally
                if (downloadedImage.exists()) {
                    downloadedImage.delete();
                    System.out.println("Deleted file: " + downloadedImage.getName());
                }
            }
            try{ Thread.sleep(18000000);}catch(Exception e){
                System.out.println(e);
            }
        }
    }

    private static File downloadImage(S3Client s3Client, String key) {
        try {
            InputStream inputStream = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build());

            File file = new File(Paths.get(System.getProperty("java.io.tmpdir"), key).toString());
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void send(File file) {
        // Implement your logic to send the file here
        System.out.println("Sending file: " + file.getName());
        for(Map.Entry<String, Long> mpp : ProcessMessage.channelList.entrySet()){
            bot.sendMessageToChannel(mpp.getValue().toString(),new InputFile(file),file.getName());
        }

    }

    @Override
    public void run() {

        listAndProcessImages();

    }
}
