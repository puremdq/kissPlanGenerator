package com.aojiaoo.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {


    //实现类似mysql的find_in_set
    public static Boolean findInSet(String str, String strList) {
        if (isBlank(str) || isBlank(strList)) {
            return false;
        }
        for (String s : strList.split(",")) {
            if (StringUtils.equals(str, s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰命名法工具
     *
     * @return underlineToCamelCase(" hello_world ") == "helloWorld"
     * underlineToCapitalizeCamelCase("hello_world") == "HelloWorld"
     * camelCaseToUnderLineCase("helloWorld") = "hello_world"
     */
    public static String underlineToCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return underlineToCamelCase(" hello_world ") == "helloWorld"
     * underlineToCapitalizeCamelCase("hello_world") == "HelloWorld"
     * camelCaseToUnderLineCase("helloWorld") = "hello_world"
     */
    public static String underlineToCapitalizeCamelCase(String s) {
        s = underlineToCamelCase(s);
        if (s == null) {
            return null;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return underlineToCamelCase(" hello_world ") == "helloWorld"
     * underlineToCapitalizeCamelCase("hello_world") == "HelloWorld"
     * camelCaseToUnderLineCase("helloWorld") = "hello_world"
     */
    public static String camelCaseToUnderLineCase(String s) {

        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append("_");
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }




}
