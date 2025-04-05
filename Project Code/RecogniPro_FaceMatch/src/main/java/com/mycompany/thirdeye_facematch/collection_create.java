package com.mycompany.thirdeye_facematch;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.ResourceAlreadyExistsException;

public class collection_create {
    public static void main(String[] args) {
        String collectionId = "RecordsForFace"; // Collection Name to be Created
        try {
            AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("ap-south-1").build();
            System.out.println("Creating collection: " + collectionId);
            CreateCollectionRequest request = new CreateCollectionRequest().withCollectionId(collectionId);
            CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request);
            System.out.println("CollectionArn : " + createCollectionResult.getCollectionArn());
            System.out.println("Status code : " + createCollectionResult.getStatusCode().toString());
        } catch (ResourceAlreadyExistsException e) {
            System.out.println("Collection already exists.");
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
