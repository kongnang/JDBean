package com.example.jdbean;

import com.example.jdbean.util.ResourcesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdBeanApplicationTests {
    @Value("${jd.fileName}")
    private String FILE_NAME;

    @Autowired
    private ResourcesUtil resourcesUtil;

    @Test
    void contextLoads() {
        System.out.println(FILE_NAME);
        resourcesUtil.readFromClassPath(FILE_NAME);
    }

}
