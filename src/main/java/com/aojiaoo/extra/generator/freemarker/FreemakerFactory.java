package com.aojiaoo.extra.generator.freemarker;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;

import java.util.Map;

public class FreemakerFactory {

    private final static Version free_marker_version = Configuration.VERSION_2_3_28;
    private static Configuration cfg = null;
    private static ObjectWrapper objectWrapper = null;

    /*不能实例化*/
    private FreemakerFactory() {
    }

    /**
     * 得到 free_marker 的Configuration
     *
     * @return Configuration
     */
    public static Configuration getConfiguration() {
        if (cfg == null) {
            try {
                cfg = new Configuration(FreemakerFactory.free_marker_version);
                cfg.setObjectWrapper(FreemakerFactory.getObjectWrapper());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cfg;
    }

    /**
     * 得到带有共享变量的 freemaker Configurationn
     *
     * @return Configuration
     */
    public static Configuration getConfiguration(Map map) throws Exception {
        if (cfg == null) {
            cfg = new Configuration(FreemakerFactory.free_marker_version);
        }
        cfg.setSharedVaribles(map);
        return cfg;
    }


    /**
     * 解析静态类  将静态类放到 freemaker 的全局空间
     *
     * @param keyName       态类放到 freemaker 的全局空间 key
     * @param fullClassName 静态类的完全 限定名
     */
    public static void setStaticPacker(String keyName, String fullClassName) throws TemplateModelException {
        setStaticPacker(getConfiguration(), keyName, fullClassName);
    }


    /**
     * 解析静态类  将静态类放到 freemaker 的全局空间
     *
     * @param cfg           Configuration
     * @param keyName       态类放到 freemaker 的全局空间 key
     * @param fullClassName 静态类的完全 限定名
     */
    public static void setStaticPacker(Configuration cfg, String keyName, String fullClassName) throws TemplateModelException {
        TemplateHashModel fileStatics = null;
        BeansWrapper beansWrapper = (BeansWrapper) getObjectWrapper();
        fileStatics = (TemplateHashModel) beansWrapper.getStaticModels().get(fullClassName);
        cfg.setSharedVariable(keyName, fileStatics);
    }


    public static ObjectWrapper getObjectWrapper() {
        if (objectWrapper != null) {
            return objectWrapper;
        }
        objectWrapper = new BeansWrapper(free_marker_version);
        return objectWrapper;
    }

    /**
     * 指定 objectWrapper
     *
     * @param objectWrapper objectWrapper
     */
    public static void setObjectWrapper(ObjectWrapper objectWrapper) {
        FreemakerFactory.objectWrapper = objectWrapper;
    }

}
