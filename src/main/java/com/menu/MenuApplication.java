package com.menu;

import com.menu.global.config.properties.CorsProperties;
import com.menu.global.config.properties.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class,
        CorsProperties.class
})
public class MenuApplication {

    public static void main(String[] args) {
        SpringApplication.run(MenuApplication.class, args);
    }

}
