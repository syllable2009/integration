package com.jxp.integration.test.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-01-15 16:18
 */

@Slf4j
public class CustomerClassLoader extends ClassLoader {

    private String mLibPath;

    public CustomerClassLoader(String path) {
        mLibPath = path;
    }

    public static void main(String[] args) {
        log.info("{}",CustomerClassLoader.class.getClassLoader().toString());
        log.info("{}",CustomerClassLoader.class.getClassLoader().getParent().toString());
        CustomerClassLoader customerClassLoader = new CustomerClassLoader("/");
        ClassLoader classLoader = customerClassLoader.getParent();
        log.info("{}-{}", classLoader.toString(), classLoader.getParent().toString());
    }

    private void test1() {
        String name = getClass().getName();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String fileName = getFileName(name);

        File file = new File(mLibPath, fileName);

        try {
            FileInputStream is = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();

            return defineClass(name, data, 0, data.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }

    //获取要加载 的class文件名
    private String getFileName(String name) {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return name + ".class";
        } else {
            return name.substring(index + 1) + ".class";
        }
    }
}
