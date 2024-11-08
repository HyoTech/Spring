package com.example.shop.Item;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;

    String createPresignedUrl(String path) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();
        var preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(3))
                .putObjectRequest(putObjectRequest)
                .build();
        return s3Presigner.presignPutObject(preSignRequest).url().toString();
    }

    public void deleteFileFromS3(S3Client s3, String objectKey) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();
            s3.deleteObject(deleteRequest);
            System.out.println("파일 삭제 완료: " + objectKey);

        } catch (S3Exception e) {
            System.err.println("파일 삭제 실패: " + e.awsErrorDetails().errorMessage());
        }
    }
}