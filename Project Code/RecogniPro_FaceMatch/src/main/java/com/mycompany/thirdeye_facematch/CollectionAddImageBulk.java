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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

public class CollectionAddImageBulk {
    public static final String collectionId = "RecordsForFace";
    public static final String bucketName = "facesketch1";
    public static final String regionName = "ap-south-1";
    //public static final String accessKey = "SECRET";
    //public static final String secretKey = "SECRET";

    public static void main(String[] args) {
        try {
            // Initialize Amazon Rekognition client
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(regionName)
                    .build();

            // Initialize Amazon S3 client
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(regionName)
                    .build();

            // List objects in the bucket
            ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request().withBucketName(bucketName);
            ListObjectsV2Result objects = s3Client.listObjectsV2(listObjectsRequest);
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();

            // Iterate over each object in the bucket
            for (S3ObjectSummary objectSummary : objectSummaries) {
                String objectKey = objectSummary.getKey();

                // Create image object for the current object
                Image image = new Image().withS3Object(new S3Object()
                        .withBucket(bucketName)
                        .withName(objectKey));

                // Index faces in the image
                IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                        .withImage(image)
                        .withCollectionId(collectionId)
                        .withExternalImageId(objectKey.substring(0, objectKey.lastIndexOf('.'))) // Use object key as external image ID
                        .withDetectionAttributes("DEFAULT");

                IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

                // Print results
                System.out.println("Results for " + objectKey);
                System.out.println("Faces indexed:");
                List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
                for (FaceRecord faceRecord : faceRecords) {
                    System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
                    System.out.println("  Location: " + faceRecord.getFaceDetail().getBoundingBox().toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
