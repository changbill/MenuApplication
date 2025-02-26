package com.menu;

import com.menu.file.config.AwsCredentialsProperties;
import com.menu.global.config.properties.CorsProperties;
import com.menu.global.config.properties.AppProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class,
        CorsProperties.class,
        AwsCredentialsProperties.class
})
public class MenuApplication {

    public static void main(String[] args) {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.load();

        // 애플리케이션 실행 전에 환경 변수로 설정
        System.setProperty("S3_ACCESS_KEY", dotenv.get("S3_ACCESS_KEY"));
        System.setProperty("S3_SECRET_KEY", dotenv.get("S3_SECRET_KEY"));
        System.setProperty("S3_BUCKET_NAME", dotenv.get("S3_BUCKET_NAME"));
        System.setProperty("S3_REGION_STATIC", dotenv.get("S3_REGION_STATIC"));

        SpringApplication.run(MenuApplication.class, args);
    }

}
