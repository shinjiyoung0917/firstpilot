package com.example.firstpilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.example.firstpilot.util.FileUploadProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
/*@EnableConfigurationProperties({
        FileUploadProperties.class
})*/

public class FirstpilotApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstpilotApplication.class, args);
    }
}
