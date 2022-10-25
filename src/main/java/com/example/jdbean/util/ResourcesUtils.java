package com.example.jdbean.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiu
 * @create 2022-10-22 15:18
 */
@Component
@Slf4j
public class ResourcesUtils {

    public List<String> readFromClassPath(String fileName) {
        List<String> data = new ArrayList<>();
        try {
            String filePath = System.getProperty("user.dir") + "/" + fileName;
            log.info(filePath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                // 跳过 #注释行
                if (line.charAt(0) == '#') {continue;}
                data.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            log.info("读取文件失败, filePath:{}", fileName, e);
        }

        return data;
    }
}
