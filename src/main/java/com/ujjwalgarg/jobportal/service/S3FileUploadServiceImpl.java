package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j(topic = "S3_FILE_UPLOAD_SERVICE")
@Service
public class S3FileUploadServiceImpl implements
    FileUploadService {

  @Value("${aws.s3.bucketName}")
  private String bucketName;

  @Value("${aws.s3.accessKey}")
  private String accessKey;

  @Value("${aws.s3.secretKey}")
  private String secretKey;

  private S3Client s3;

  private S3Presigner s3Presigner;

  @PostConstruct
  private void setUp() {
    var awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
    this.s3 = S3Client.builder()
        .region(Region.AP_SOUTH_1)
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .build();
    this.s3Presigner = S3Presigner.builder()
        .region(Region.AP_SOUTH_1)
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
        .build();
  }


  @Override
  public void deleteFile(String filePath) throws NotFoundException {
    var delObjReq = DeleteObjectRequest.builder()
        .bucket(bucketName).key(filePath).build();

    try {
      s3.deleteObject(delObjReq);
    } catch (Exception err) {
      log.error("Error while deleting file from s3", err);
    }
  }

  @Override
  public boolean uploadFile(String filePath, byte[] file) {
    return this.uploadFile(filePath, file, Map.of());
  }

  @Override
  public boolean uploadFile(String filePath, byte[] file, Map<String, String> metadata) {
    var putObjReq = PutObjectRequest.builder()
        .bucket(bucketName).key(filePath).metadata(metadata).build();

    try {
      s3.putObject(putObjReq, RequestBody.fromBytes(file));
      return true;
    } catch (Exception err) {
      log.error("Error while uploading file to s3", err);
      return false;
    }
  }

  @Override
  public String getFileUrl(String filePath) throws NotFoundException {
    var getObjPresignReq = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(5))
        .getObjectRequest(builder -> builder.bucket(bucketName).key(filePath).build())
        .build();

    return s3Presigner
        .presignGetObject(getObjPresignReq)
        .url()
        .toString();
  }
}
