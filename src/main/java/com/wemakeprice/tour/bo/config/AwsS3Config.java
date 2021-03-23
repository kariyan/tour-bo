package com.wemakeprice.tour.bo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.wemakeprice.tour.bo.common.util.S3Uploader;
import com.wemakeprice.tour.bo.config.kms.KmsProperties;
import com.wemakeprice.tour.bo.config.kms.vo.AwsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final KmsProperties kmsProperties;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        AwsVO awsVO = kmsProperties.getAws();
        if (awsVO != null) {
            return AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsVO.getSqs().getAccessKey(), awsVO.getSqs().getSecretKey())))
                    .withRegion(Regions.fromName(region))
                    .enablePathStyleAccess()
                    .build();
        } else {
            return AmazonS3ClientBuilder
                    .standard()
                    .withRegion(Regions.fromName(region))
                    .enablePathStyleAccess()
                    .build();
        }
    }

    @Bean
    public TransferManager transferManager() {
        return TransferManagerBuilder
                .standard()
                .withS3Client(amazonS3())
                .build();
    }

    @Bean
    public S3Uploader s3Uploader(TransferManager transferManager) {
        return new S3Uploader(transferManager);
    }
}
