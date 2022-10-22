package com.example.jdbean.util;

import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiu
 * @create 2022-10-22 15:18
 */
@Component
@Slf4j
public class ResourcesUtil {

    public List<String> readFromClassPath(String fileName) {
        List<String> data = new ArrayList<>();
        try {
            // 读取resource目录下的txt文件
            ClassPathResource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                // 跳过 #注释行
                if (line.charAt(0) == '#') {continue;}
                data.add(line);
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            log.info("读取文件失败, filePath:{}", fileName, e);
        }

        return data;
    }
}
