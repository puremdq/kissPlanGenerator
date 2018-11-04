import com.aojiaoo.util.DbInfoUtil;
import com.aojiaoo.util.PropertiesUtil;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Test1 {

    @Test
    public void testGenerator() throws Exception {
        String tableName = "sys_user";
        Map<String, List<Map<String, String>>> tableInfo = DbInfoUtil.getTableInfo(PropertiesUtil.getProperties("classpath:jdbc.properties"), tableName);
        List<Map<String, String>> columnList = tableInfo.get(tableName);
        System.out.println(columnList);
//        GeneratorUtil.generatorMapping(tableName, columnList, properties);
//        GeneratorUtil.generatorMapper(tableName, columnList, properties);
//        GeneratorUtil.generatorService(tableName, properties);
//        GeneratorUtil.generatorEntity(tableName, columnList,properties);
//        GeneratorUtil.generatorMapping(tableName, columnList,properties);
    }

    @Test
    public void test2() throws Exception {

        int i=1111;
        String s = String.format("%03d", i);
        System.out.println(s);// print 01
    }
}
