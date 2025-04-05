/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public static final String bucket = "faceskechbucket";
        public static final String photo = "vijay-original.jpg";

    public static void main(String[] args) throws Exception {
        String bucketName ="faceskechbucket";
        String regionName ="us-east-1";


        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionName)
                .build();
//AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
        //AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().setSignerRegionOverride("us-east-1").build();

        Image image = new Image()
                .withS3Object(new S3Object()
                .withBucket(bucket)
                .withName(photo));
        
        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(image)
                .withCollectionId(collectionId)
                .withExternalImageId(photo)
                .withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);
        
        System.out.println("Results for " + photo);
        System.out.println("Faces indexed:");
        List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        for (FaceRecord faceRecord : faceRecords) {
            System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
            System.out.println("  Location:" + faceRecord.getFaceDetail().getBoundingBox().toString());
        }
    }
}
