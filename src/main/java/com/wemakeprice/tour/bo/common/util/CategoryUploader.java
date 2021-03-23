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
public class CategoryUploader {

    private final TransferManager transferManager;

    @Value("${aws.s3.bucket}")
    private String bucket;
    @Value("${aws.s3.cdn.url}")
    private String cdnDomain;
    @Value("${service-category.file-name}")
    private String serviceCategoryKey;

    public String uploadFile(@NotBlank String jsonString) throws IOException, InterruptedException {
        try (InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes())) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, serviceCategoryKey, inputStream,
                    new ObjectMetadata());
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            return cdnDomain + "/" + uploadResult.getKey();
        }
    }
}
