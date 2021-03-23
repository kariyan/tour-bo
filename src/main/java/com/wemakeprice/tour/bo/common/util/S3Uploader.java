package com.wemakeprice.tour.bo.common.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class S3Uploader {

    private final TransferManager transferManager;

    @Value("${aws.s3.bucket}")
    private String bucket;
    @Value("${s3uploader.cdn-url}")
    private String cdnDomain;
    @Value("${s3uploader.file-name}")
    private String serviceCategoryKey;

    public String uploadJson(@NotBlank String jsonString) throws IOException, InterruptedException {
        byte[] jsonStringBytes = jsonString.getBytes();
        try (InputStream inputStream = new ByteArrayInputStream(jsonStringBytes)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(jsonStringBytes.length);
            metadata.setCacheControl("max-age=60");
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, serviceCategoryKey, inputStream,
                    metadata);
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            return cdnDomain + "/" + uploadResult.getKey();
        }
    }
}
