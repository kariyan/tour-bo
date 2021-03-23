package com.wemakeprice.tour.bo.common.util;

import com.wemakeprice.tour.bo.config.AwsS3Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = AwsS3Config.class)
class S3UploaderTest {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("json 파일 업로드 테스트")
    void uploadTest() throws IOException, InterruptedException {
        // given
        String testJsonString = "{\"id\": \"test\"}";

        // when
        String results = s3Uploader.uploadJson(testJsonString);

        // then
        log.info(results);
        assertThat(results).isNotBlank();
    }
}
