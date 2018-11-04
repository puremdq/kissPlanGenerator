package com.aojiaoo.extra.generator;

import com.aojiaoo.extra.generator.freemarker.FreemakerFactory;
import com.aojiaoo.util.DbInfoUtil;
import com.aojiaoo.util.FileUtils;
import com.aojiaoo.util.PropertiesUtil;
import com.aojiaoo.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class GeneratorDoc {

    public static void startGeneratorDoc() throws Exception {

        Properties generatorProperties = PropertiesUtil.getProperties("classpath:extra/generatorDoc.properties");
        Properties jdbcProperties = PropertiesUtil.getProperties("classpath:jdbc.properties");
        Configuration cfg = getConfiguration(generatorProperties);
        String tables = generatorProperties.getProperty("generator.table");
        Map<String, List<Map<String, String>>> resMap = DbInfoUtil.getTableInfo(jdbcProperties, tables);
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("tableMap", resMap);
        String fileName = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "数据库文档.md";
        String filePath = FileUtils.getFilePathByClasspathOrSelf(generatorProperties.getProperty("generatorDoc.path"));
        filePath = StringUtils.isBlank(filePath) ? FileSystemView.getFileSystemView().getHomeDirectory().getPath() : filePath;
        Template template = cfg.getTemplate("databaseDoc.ftl");

        template.process(templateMap, new FileWriter(FileUtils.spliceFilePath(filePath, fileName)));
        System.out.println("成功生成文件到:" + filePath);

    }


    private static Configuration getConfiguration(Properties properties) {
        Configuration cfg = FreemakerFactory.getConfiguration();
        try {
            FreemakerFactory.setStaticPacker(cfg, "StringUtils", StringUtils.class.getName());
            cfg.setDirectoryForTemplateLoading(new File(FileUtils.getFilePathByClasspathOrSelf(properties.getProperty("generatorDoc.templatePath"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cfg;
    }

    public static void main(String[] args) throws Exception {
        startGeneratorDoc();
    }


}
