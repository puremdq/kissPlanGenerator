<#assign idLength=0/>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperFullName}">

    <resultMap id="BaseResultMap" type="${entityPackage}.${entityName}">
        <#list columnList as columnMap>
            <#if columnMap.isId=='true'>
                <#assign idLength = idLength + 1/>
            </#if>
        </#list>
    </resultMap>

    <#assign columnStrList><#list columnList as columnMap>a.${columnMap.col} as ${StringUtils.underlineToCamelCase(columnMap.col)},</#list></#assign>
    <sql id="Base_Column_List">
        <#if columnStrList?? && (columnStrList?length>0)>
            ${columnStrList?substring(0,(columnStrList?length)-1)}
        <#else>
            *
        </#if>
    </sql>


    <#if (idLength>0)>
    <select id="selectByPrimaryKey" resultType="${entityPackage}.${entityName}">
        select
        <include refid="Base_Column_List"/>
        from ${tableName}
        <where>
        <#list columnList as columnMap>
            <#if columnMap.isId=='true'>
              and  ${columnMap.col} = ${r'#{'}${StringUtils.underlineToCamelCase(columnMap.col)},jdbcType=${columnMap.jdbcType}}
            </#if>
        </#list>
        </where>
    </select>
    </#if>



    <select id="findPage" resultType="${entityPackage}.${entityName}">
        select
        <include refid="Base_Column_List"/>
        from ${tableName} a
        <where>
           <#list columnList as columnMap>
               <if test="${StringUtils.underlineToCamelCase(columnMap.col)} != null">
                   and a.${columnMap.col} = ${r'#{'}${StringUtils.underlineToCamelCase(columnMap.col)},jdbcType=${columnMap.jdbcType}}
               </if>
           </#list>
        </where>
    </select>

</mapper>