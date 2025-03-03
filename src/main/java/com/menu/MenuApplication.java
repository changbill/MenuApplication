package com.menu;

import com.menu.file.config.AwsCredentialsProperties;
import com.menu.global.config.properties.CorsProperties;
import com.menu.global.config.properties.AppProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.menu", "com.menu.auth.config"})
@EnableConfigurationProperties({
        AppProperties.class,
        CorsProperties.class,
        AwsCredentialsProperties.class
})
public class MenuApplication {

    public static void main(String[] args) {
        SpringApplication.run(MenuApplication.class, args);
    }

}
