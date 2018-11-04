
package com.aojiaoo.util;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * <p>Description: 获取数据库基本信息的工具类</p>
 *
 * @author puremdq@gmail.com
 */
public class DbInfoUtil {
    private static Connection conn = null;

    /**
     * 根据数据库的连接参数，获取指定表的基本信息：字段名、字段类型、字段注释
     *
     * @param driver           数据库连接驱动
     * @param url              数据库连接url
     * @param user             数据库登陆用户名
     * @param pwd              数据库登陆密码
     * @param tableNamePattern 表名正则
     * @return Map集合
     */
    public static Map<String, List<Map<String, String>>> getTableInfo(String driver, String url, String user, String pwd, String tableNamePattern) {

        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        try {
            conn = getConnection(driver, url, user, pwd);
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, "%", tableNamePattern, new String[]{"TABLE"});

            while (resultSet.next()) {
                /*某个表*/
                List<Map<String, String>> currentTableList = new ArrayList<>();
                String tableName = resultSet.getString("TABLE_NAME");
                List<String> idList = getIdList(conn.getMetaData(), tableName);
                ResultSet rs = conn.getMetaData().getColumns(null, getSchema(conn), tableName.toUpperCase(), "%");
                while (rs.next()) {
                    /*某个字段*/
                    //System.out.println("字段名："+rs.getString("COLUMN_NAME")+"--字段注释："+rs.getString("REMARKS")+"--字段数据类型："+rs.getString("TYPE_NAME"));
                    Map<String, String> colMap = new HashMap<>();
                    colMap.put("col", rs.getString("COLUMN_NAME"));
                    colMap.put("remarks", StringUtils.trimToEmpty(rs.getString("REMARKS")));
                    colMap.put("jdbcType", getJdbcTypeName(rs.getInt("DATA_TYPE")));
                    colMap.put("javaType", getJavaTypeByJdbcType(rs.getInt("DATA_TYPE")));
                    colMap.put("colSize", rs.getString("COLUMN_SIZE"));
                    colMap.put("databaseType", rs.getString("TYPE_NAME"));
                    colMap.put("nullAble", "1".equals(rs.getString("NULLABLE")) ? "Y" : "N");
                    colMap.put("colDef", StringUtils.trimToEmpty(rs.getString("COLUMN_DEF")));
                    colMap.put("isId", (idList != null && idList.contains(rs.getString("COLUMN_NAME"))) + "");
                    currentTableList.add(colMap);
                }

                resultMap.put(tableName, currentTableList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultMap;
    }


    public static Map<String, List<Map<String, String>>> getTableInfo(Properties jdbcProperties, String tableNamePattern) {
        return getTableInfo(
                jdbcProperties.getProperty("jdbc.driver"),
                jdbcProperties.getProperty("jdbc.url"),
                jdbcProperties.getProperty("jdbc.username"),
                jdbcProperties.getProperty("jdbc.password"),
                tableNamePattern);
    }


    //获取连接
    private static Connection getConnection(String driver, String url, String user, String pwd) throws Exception {
        if (conn != null && !conn.isClosed()) {
            return conn;
        }
        try {
            Properties props = new Properties();
            props.put("remarksReporting", "true");
            props.put("user", user);
            props.put("password", pwd);
            Class.forName(driver);
            conn = DriverManager.getConnection(url, props);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }

    //其他数据库不需要这个方法 oracle和db2需要
    private static String getSchema(Connection currentConn) throws Exception {
        String schema = currentConn.getMetaData().getUserName();
        if ((schema == null) || (schema.length() == 0)) {
            throw new Exception("ORACLE数据库模式不允许为空");
        }

        return schema.toUpperCase();
    }


    public static String getJavaTypeByJdbcType(int jdbcType) {

        Map<Integer, String> javaTypeMap = new HashMap<>();
        javaTypeMap.put(Types.ARRAY, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.BIGINT, getSimpleClassName(Long.class.getName()));
        javaTypeMap.put(Types.BINARY, "byte[]"); //$NON-NLS-1$
        javaTypeMap.put(Types.BIT, getSimpleClassName(Boolean.class.getName()));
        javaTypeMap.put(Types.BLOB, "byte[]"); //$NON-NLS-1$
        javaTypeMap.put(Types.BOOLEAN, getSimpleClassName(Boolean.class.getName()));
        javaTypeMap.put(Types.CHAR, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.CLOB, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.DATALINK, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.DATE, getSimpleClassName(Date.class.getName()));
        javaTypeMap.put(Types.DECIMAL, getSimpleClassName(BigDecimal.class.getName()));
        javaTypeMap.put(Types.DISTINCT, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.DOUBLE, getSimpleClassName(Double.class.getName()));
        javaTypeMap.put(Types.FLOAT, getSimpleClassName(Double.class.getName()));
        javaTypeMap.put(Types.INTEGER, getSimpleClassName(Integer.class.getName()));
        javaTypeMap.put(Types.JAVA_OBJECT, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.LONGNVARCHAR, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.LONGVARBINARY, "byte[]"); //$NON-NLS-1$
        javaTypeMap.put(Types.LONGVARCHAR, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.NCHAR, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.NCLOB, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.NVARCHAR, getSimpleClassName(String.class.getName()));
        javaTypeMap.put(Types.NULL, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.NUMERIC, getSimpleClassName(BigDecimal.class.getName()));
        javaTypeMap.put(Types.OTHER, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.REAL, getSimpleClassName(Float.class.getName()));
        javaTypeMap.put(Types.REF, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.SMALLINT, getSimpleClassName(Short.class.getName()));
        javaTypeMap.put(Types.STRUCT, getSimpleClassName(Object.class.getName()));
        javaTypeMap.put(Types.TIME, getSimpleClassName(Date.class.getName()));
        javaTypeMap.put(Types.TIMESTAMP, getSimpleClassName(Date.class.getName()));
        javaTypeMap.put(Types.TINYINT, getSimpleClassName(Byte.class.getName()));
        javaTypeMap.put(Types.VARBINARY, "byte[]"); //$NON-NLS-1$
        javaTypeMap.put(Types.VARCHAR, getSimpleClassName(String.class.getName()));
        return javaTypeMap.get(jdbcType);
    }

    public static String getJdbcTypeName(int jdbcType) {

        Map<Integer, String> typeToName = new HashMap<>();
        typeToName.put(Types.ARRAY, "ARRAY"); //$NON-NLS-1$
        typeToName.put(Types.BIGINT, "BIGINT"); //$NON-NLS-1$
        typeToName.put(Types.BINARY, "BINARY"); //$NON-NLS-1$
        typeToName.put(Types.BIT, "BIT"); //$NON-NLS-1$
        typeToName.put(Types.BLOB, "BLOB"); //$NON-NLS-1$
        typeToName.put(Types.BOOLEAN, "BOOLEAN"); //$NON-NLS-1$
        typeToName.put(Types.CHAR, "CHAR"); //$NON-NLS-1$
        typeToName.put(Types.CLOB, "CLOB"); //$NON-NLS-1$
        typeToName.put(Types.DATALINK, "DATALINK"); //$NON-NLS-1$
        typeToName.put(Types.DATE, "DATE"); //$NON-NLS-1$
        typeToName.put(Types.DECIMAL, "DECIMAL"); //$NON-NLS-1$
        typeToName.put(Types.DISTINCT, "DISTINCT"); //$NON-NLS-1$
        typeToName.put(Types.DOUBLE, "DOUBLE"); //$NON-NLS-1$
        typeToName.put(Types.FLOAT, "FLOAT"); //$NON-NLS-1$
        typeToName.put(Types.INTEGER, "INTEGER"); //$NON-NLS-1$
        typeToName.put(Types.JAVA_OBJECT, "JAVA_OBJECT"); //$NON-NLS-1$
        typeToName.put(Types.LONGVARBINARY, "LONGVARBINARY"); //$NON-NLS-1$
        typeToName.put(Types.LONGVARCHAR, "LONGVARCHAR"); //$NON-NLS-1$
        typeToName.put(Types.NCHAR, "NCHAR"); //$NON-NLS-1$
        typeToName.put(Types.NCLOB, "NCLOB"); //$NON-NLS-1$
        typeToName.put(Types.NVARCHAR, "NVARCHAR"); //$NON-NLS-1$
        typeToName.put(Types.LONGNVARCHAR, "LONGNVARCHAR"); //$NON-NLS-1$
        typeToName.put(Types.NULL, "NULL"); //$NON-NLS-1$
        typeToName.put(Types.NUMERIC, "NUMERIC"); //$NON-NLS-1$
        typeToName.put(Types.OTHER, "OTHER"); //$NON-NLS-1$
        typeToName.put(Types.REAL, "REAL"); //$NON-NLS-1$
        typeToName.put(Types.REF, "REF"); //$NON-NLS-1$
        typeToName.put(Types.SMALLINT, "SMALLINT"); //$NON-NLS-1$
        typeToName.put(Types.STRUCT, "STRUCT"); //$NON-NLS-1$
        typeToName.put(Types.TIME, "TIME"); //$NON-NLS-1$
        typeToName.put(Types.TIMESTAMP, "TIMESTAMP"); //$NON-NLS-1$
        typeToName.put(Types.TINYINT, "TINYINT"); //$NON-NLS-1$
        typeToName.put(Types.VARBINARY, "VARBINARY"); //$NON-NLS-1$
        typeToName.put(Types.VARCHAR, "VARCHAR"); //$NON-NLS-1$

        String answer = typeToName.get(jdbcType);
        if (answer == null) {
            answer = "OTHER"; //$NON-NLS-1$
        }
        return answer;
    }

    private static String getSimpleClassName(String className) {
        if (StringUtils.startsWith(className, "java.lang.")) {
            return className.substring("java.lang.".length());
        } else {
            return className;
        }
    }


    private static List<String> getIdList(DatabaseMetaData dbMetaData, String tableName) throws Exception {

        List<String> stringList = new ArrayList<>();

        ResultSet primaryKeyResultSet = dbMetaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeyResultSet.next()) {
            stringList.add(primaryKeyResultSet.getString("COLUMN_NAME"));
        }
        return stringList;
    }

    public static void main(String[] args) {
        //mysql
        String table = "%";
        PropertiesUtil.load("classpath:jdbc.properties");
        Map<String, List<Map<String, String>>> map = getTableInfo(PropertiesUtil.get("jdbc.driver"), PropertiesUtil.get("jdbc.url"), PropertiesUtil.get("jdbc.username"), PropertiesUtil.get("jdbc.password"), table);
        System.out.println(map);
    }

}