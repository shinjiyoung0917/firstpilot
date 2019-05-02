package com.example.firstpilot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.mail.username=test@test.com"})
public class FirstpilotApplicationTests {

    @Test
    public void contextLoads() {
    }

}
