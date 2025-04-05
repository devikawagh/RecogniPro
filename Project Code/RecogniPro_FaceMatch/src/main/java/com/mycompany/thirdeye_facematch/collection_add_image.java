package com.mycompany.thirdeye_facematch;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import java.util.List;

public class collection_add_image {
    public static final String collectionId = "RecordsForFace";
    public static final String bucketName = "facesketch1";
    public static final String regionName = "ap-south-1";

    public static final String photo = "shahrukh.jpg";

    public static void main(String[] args) {
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(regionName)
                    .build();

            Image image = new Image()
                    .withS3Object(new S3Object()
                    .withBucket(bucketName)
                    .withName(photo));

            System.out.println("Image loaded successfully.");

            IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                    .withImage(image)
                    .withCollectionId(collectionId)
                    .withExternalImageId("shahrukh") // Provide a unique identifier for the image
                    .withDetectionAttributes("DEFAULT");

            IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

            System.out.println("Results for " + photo);
            System.out.println("Faces indexed:");
            List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
            for (FaceRecord faceRecord : faceRecords) {
                System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
                System.out.println("  Location:" + faceRecord.getFaceDetail().getBoundingBox().toString());
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
