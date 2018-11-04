package com.aojiaoo.extra.generator;

import com.aojiaoo.extra.generator.freemarker.FreemakerFactory;
import com.aojiaoo.util.DbInfoUtil;
import com.aojiaoo.util.FileUtils;
import com.aojiaoo.util.PropertiesUtil;
import com.aojiaoo.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GeneratorCode {

    public static void startGeneratorCode() throws Exception {

        Properties generatorProperties = PropertiesUtil.getProperties("classpath:extra/generatorCode.properties");
        Configuration cfg = getConfiguration(generatorProperties);
        String tables = generatorProperties.getProperty("generatorCode.table");

        for (int i = 0; i < tables.split(",").length; i++) {
            Map<String, List<Map<String, String>>> resMap = DbInfoUtil.getTableInfo(PropertiesUtil.getProperties("classpath:jdbc.properties"), tables.split(",")[i]);
            for (String tableName : resMap.keySet()) {
                String currentEntityName = GeneratorCode.generatorEntity(tableName, resMap.get(tableName), cfg, generatorProperties);
                GeneratorCode.generatorMapper(tableName, currentEntityName, resMap.get(tableName), cfg, generatorProperties);
                GeneratorCode.generatorMapping(tableName, currentEntityName, resMap.get(tableName), cfg, generatorProperties);
                GeneratorCode.generatorService(currentEntityName, resMap.get(tableName), cfg, generatorProperties);
//                GeneratorCode.generatorController(currentEntityName, cfg, generatorProperties);
            }
        }
    }

    /**
     * @param tableName  表名
     * @param columnList //[{col=id, jdbcType=INTEGER, remarks=, javaType=Integer}, {col=permission_name, jdbcType=VARCHAR, remarks=权限标示名称, javaType=String}]
     * @return entityName 生成的实体名称
     */
    public static String generatorEntity(String tableName, List<Map<String, String>> columnList, Configuration cfg, Properties properties) throws Exception {

        String entityName = GeneratorCode.getEntityNameByTableName(tableName, properties);
        String entityPackage = properties.getProperty("generatorCode.entityPackage");

        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("tableName", tableName);
        templateMap.put("entityPackage", entityPackage);
        templateMap.put("entityName", entityName);
        templateMap.put("baseEntityFullName", properties.getProperty("generatorCode.baseEntity"));
        templateMap.put("columnList", columnList);

        String filePath = FileUtils.spliceFilePath(getSourceRootPath(properties), StringUtils.replaceAll(entityPackage, "\\.", "/"));
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        filePath = FileUtils.spliceFilePath(filePath, entityName + ".java");
        Template template = cfg.getTemplate("entity.ftl");
        template.process(templateMap, new FileWriter(filePath));
        return entityName;
    }


    public static void generatorMapper(String tableName, String entityName, List<Map<String, String>> columnList, Configuration cfg, Properties properties) throws Exception {

        Map<String, Object> templateMap = new HashMap<>();
        String mapperPackage = properties.getProperty("generatorCode.mapperPackage");
        String mapperName = entityName + "Mapper";

        templateMap.put("tableName", tableName);
        templateMap.put("entityPackage", properties.getProperty("generatorCode.entityPackage"));
        templateMap.put("entityName", entityName);

        templateMap.put("baseMapperFullName", properties.getProperty("generatorCode.baseMapper"));
        templateMap.put("mapperPackage", mapperPackage);
        templateMap.put("mapperName", mapperName);
        templateMap.put("annotationFullName", properties.getProperty("generatorCode.mapper.annotation"));
        templateMap.put("columnList", columnList);

        String filePath = FileUtils.spliceFilePath(getSourceRootPath(properties), StringUtils.replaceAll(mapperPackage, "\\.", "/"), mapperName + ".java");
        Template template = cfg.getTemplate("mapper.ftl");
        template.process(templateMap, new FileWriter(filePath));
    }

    public static void generatorMapping(String tableName, String entityName, List<Map<String, String>> columnList, Configuration cfg, Properties properties) throws Exception {

        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("mapperFullName", properties.getProperty("generatorCode.mapperPackage") + "." + entityName + "Mapper");
        templateMap.put("tableName", tableName);
        templateMap.put("baseEntityFullName", properties.getProperty("generatorCode.baseEntity"));
        templateMap.put("columnList", columnList);

        templateMap.put("entityName", entityName);
        templateMap.put("entityPackage", properties.getProperty("generatorCode.entityPackage"));

        String mappingPackage = properties.getProperty("generatorCode.mappingPackage");
        String mappingName = entityName + "Mapping1";
        String filePath = FileUtils.spliceFilePath(getMappingResourceRootPath(properties), StringUtils.replaceAll(mappingPackage, "\\.", "/"));
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        filePath = FileUtils.spliceFilePath(filePath, mappingName + ".xml");
        Template template = cfg.getTemplate("mapping.ftl");
        template.process(templateMap, new FileWriter(filePath));
    }

    public static void generatorService(String entityName, List<Map<String, String>> columnList, Configuration cfg, Properties properties) throws Exception {

        String servicePackage = properties.getProperty("generatorCode.servicePackage");
        String serviceName = entityName + "Service";
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("servicePackage", servicePackage);
        templateMap.put("baseServiceFullName", properties.getProperty("generatorCode.baseService"));
        templateMap.put("baseImplServiceFullName", properties.getProperty("generatorCode.baseImplService"));
        templateMap.put("entityFullName", properties.getProperty("generatorCode.entityPackage") + "." + entityName);
        templateMap.put("mapperFullName", properties.getProperty("generatorCode.mapperPackage") + "." + entityName + "Mapper");
        templateMap.put("serviceName", serviceName);
        templateMap.put("columnList", columnList);

        String filePath = FileUtils.spliceFilePath(getSourceRootPath(properties), StringUtils.replaceAll(servicePackage, "\\.", "/"), "I" + serviceName + ".java");
        Template template = cfg.getTemplate("service.ftl");
        template.process(templateMap, new FileWriter(filePath));


        filePath = FileUtils.spliceFilePath(getSourceRootPath(properties), StringUtils.replaceAll(servicePackage, "\\.", "/"), "impl", serviceName + "Impl.java");

        template = cfg.getTemplate("serviceImpl.ftl");
        template.process(templateMap, new FileWriter(filePath));
    }

    public static void generatorController(String entityName, Configuration cfg, Properties properties) throws Exception {

        String controllerPackage = properties.getProperty("generatorCode.controllerPackage");
        String controllerName = entityName + "Controller";
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("controllerPackage", controllerPackage);
        templateMap.put("baseControllerFullName", properties.getProperty("generatorCode.baseController"));
        templateMap.put("serviceFullName", properties.getProperty("generatorCode.servicePackage") + "." + entityName + "Service");
        templateMap.put("entityName", entityName);
        templateMap.put("controllerName", controllerName);
        String filePath = FileUtils.spliceFilePath(getSourceRootPath(properties), StringUtils.replaceAll(controllerPackage, "\\.", "/"), controllerName + ".java");

        Template template = cfg.getTemplate("controller.ftl");
        template.process(templateMap, new FileWriter(filePath));
    }


    private static String getEntityNameByTableName(String tableName, Properties properties) {

        if (StringUtils.isBlank(tableName)) {
            return "";
        }

        String tablePrefix = "";
        if (StringUtils.isNotBlank(properties.getProperty("generatorCode.table." + tableName + ".prefix"))) {
            tablePrefix = properties.getProperty("generatorCode.table." + tableName + ".prefix");
        } else if (StringUtils.isNotBlank(properties.getProperty("generatorCode.table.prefix"))) {
            tablePrefix = properties.getProperty("generatorCode.table.prefix");
        }

        if (StringUtils.isNotBlank(tablePrefix) && !tablePrefix.endsWith("_")) {
            tablePrefix = tablePrefix + "_";
        }

        return StringUtils.underlineToCapitalizeCamelCase(tableName.substring(tablePrefix.length()));
    }

    private static String getSourceRootPath(Properties properties) {
        String projectPath = properties.getProperty("generatorCode.projectPath");
        projectPath = StringUtils.isNotBlank(projectPath) ? projectPath : FileUtils.getProjectPath();
        String sourceRoot = properties.getProperty("generatorCode.sourceRoot");
        return FileUtils.spliceFilePath(projectPath, sourceRoot);
    }

    private static String getMappingResourceRootPath(Properties properties) {
        String projectPath = properties.getProperty("generatorCode.projectPath");
        projectPath = StringUtils.isNotBlank(projectPath) ? projectPath : FileUtils.getProjectPath();
        String mappingResourceRoot = properties.getProperty("generatorCode.mappingResourceRoot");
        return FileUtils.spliceFilePath(projectPath, mappingResourceRoot);
    }

    private static Configuration getConfiguration(Properties properties) {
        Configuration cfg = FreemakerFactory.getConfiguration();
        try {
            FreemakerFactory.setStaticPacker(cfg, "StringUtils", StringUtils.class.getName());
            cfg.setDirectoryForTemplateLoading(new File(FileUtils.getFilePathByClasspathOrSelf(properties.getProperty("generatorCode.templatePath"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cfg;
    }

    public static void main(String[] args) throws Exception {
        startGeneratorCode();
    }


}
