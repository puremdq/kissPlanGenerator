package com.aojiaoo.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties properties = null;

    /**
     * 得到全局Properties
     *
     * @return 得到全局Properties
     */
    public static Properties getProperties() {
        if (PropertiesUtil.properties != null) {
            return PropertiesUtil.properties;
        }
        PropertiesUtil.properties = new Properties();
        return PropertiesUtil.properties;
    }


    /**
     * 用全局的 properties 加载 新的文件
     *
     * @param path 要加载的文件的地址
     */
    public static void load(String path) {
        loadProperties(PropertiesUtil.getProperties(), path);
    }


    /**
     * 创建一个新的Properties对象 并加载properties 文件
     *
     * @return 创建一个新的Properties对象  并加载新的的 文件
     */
    public static Properties getProperties(String path) {
        Properties currentProperties = new Properties();
        loadProperties(currentProperties, path);
        return currentProperties;
    }


    /**
     * @param currentProperties 要加载进入的 Properties
     * @param path              要加载的文件 可以是classpath
     * @return 是否加载成功
     */
    public static Boolean loadProperties(Properties currentProperties, String path) {

        if (currentProperties == null) {
            return false;
        }

        if (!path.endsWith(".properties")) {
            return false;
        }
        path = FileUtils.getFilePathByClasspathOrSelf(path);
        try {
            currentProperties.load(new FileInputStream(path));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 得到全局Properties 的某个值
     *
     * @return 得到全局Properties
     */
    public static String get(String key) {
        return PropertiesUtil.getProperties().getProperty(key);
    }

    public static void set(String key, String value) {
        PropertiesUtil.getProperties().setProperty(key, value);
    }

    /**
     * 清空全局Properties
     *
     * @return 清空全局Properties
     */
    public static void clearProperties() {
        PropertiesUtil.properties = null;
    }
}
