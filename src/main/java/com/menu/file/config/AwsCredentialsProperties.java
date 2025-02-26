package com.menu.file.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cloud.aws")
public class AwsCredentialsProperties {
    private final Credentials credentials = new Credentials();
    private final S3 s3 = new S3();
    private final Region region = new Region();

    @Getter
    public static final class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter
    public static final class S3 {
        private String bucket;
    }

    @Getter
    public static final class Region {
        private String statik;
    }
}
