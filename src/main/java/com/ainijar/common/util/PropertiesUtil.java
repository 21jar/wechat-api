package com.ainijar.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {
    public static Properties readConfi(String path) {
        Properties pro = new Properties();
        try {
            pro.load(PropertiesUtil.class.getResourceAsStream(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pro;
    }

    public static String readJsonData(String fileName) {
        // 读取文件数据
        StringBuffer strbuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
//		File myFile = new File(fileName);
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            log.error("Can't Find " + fileName);
            return null;
        }
        try {
//			FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                strbuffer.append(str);
            }
        } catch (IOException e) {
            log.error("read file exception", e);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strbuffer.toString();
    }

}
