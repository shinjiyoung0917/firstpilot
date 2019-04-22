package com.example.firstpilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

<<<<<<< HEAD
@SpringBootApplication
=======
import com.example.firstpilot.util.FileUploadProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
/*@EnableConfigurationProperties({
        FileUploadProperties.class
})*/

>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
public class FirstpilotApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstpilotApplication.class, args);
    }
<<<<<<< HEAD

=======
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
}
