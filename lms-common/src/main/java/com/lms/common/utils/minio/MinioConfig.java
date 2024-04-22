package com.lms.common.utils.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whn
 * MinIO 的配置类
 */

@Component
@ConfigurationProperties("minio")
@Data
public class MinioConfig {
    /* URL to S3 service. */
    private String endpoint;

    /* Access key (aka user ID) of an account in the S3 service. */
    private String accessKey;

    /* Secret key (aka password) of an account in the S3 service. */
    private String secretKey;
}
