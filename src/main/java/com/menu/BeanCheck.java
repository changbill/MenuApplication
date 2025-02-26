package com.menu;

import com.menu.file.config.AwsCredentialsProperties;
import com.menu.global.config.properties.AppProperties;
import com.menu.global.config.properties.CorsProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class,
        CorsProperties.class,
        AwsCredentialsProperties.class
})
public class BeanCheck {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.menu");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}
