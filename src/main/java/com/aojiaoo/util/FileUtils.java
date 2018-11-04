package com.aojiaoo.util;

import org.apache.commons.lang3.StringUtils;

public class FileUtils extends org.apache.commons.io.FileUtils {
    private FileUtils() {
    }

    /**
     * 拼接路径
     *
     * @param path1 路径1
     * @param path2 路径2
     * @return 拼接后的路径
     */
    public static String spliceFilePath(String path1, String path2) {
        path1 = StringUtils.trimToEmpty(path1);
        path2 = StringUtils.trimToEmpty(path2);

        path1 = path1.replaceAll("\\\\", "/");
        path2 = path2.replaceAll("\\\\", "/");

        if (!path1.endsWith("/")) {
            path1 = path1 + "/";
        }

        if (path2.startsWith("/")) {
            path2 = path2.substring(1);
        }
        return path1 + path2;
    }


    public static String spliceFilePath(String... pathArr) {
        if (pathArr.length < 2) {
            if (pathArr.length == 1) {
                return pathArr[0];
            } else {
                return "";
            }
        }

        String splicedFilePath = pathArr[0];

        for (int i = 1; i < pathArr.length; i++) {
            splicedFilePath = spliceFilePath(splicedFilePath, pathArr[i]);
        }
        return splicedFilePath;
    }


    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }


    /**
     * 得到该Resource的绝对路径  如果不符合要求(startsWith("classpath:")) 则原样返回
     *
     * @param classpathResource classpath 写法的路径 如 classpath:xxx.xml
     * @return 该Resource的绝对路径 如果不符合要求(startsWith("classpath:")) 则原样返回
     */
    public static String getFilePathByClasspathOrSelf(String classpathResource) {

        if (StringUtils.startsWith(classpathResource, "classpath:")) {
            return ClassLoader.getSystemResource(classpathResource.substring("classpath:".length())).getPath();
        } else {
            return classpathResource;
        }
    }
}
