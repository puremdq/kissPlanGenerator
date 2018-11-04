<#assign idLength=0/>
<#assign entityFullName="${entityPackage}.${entityName}"/>
<#assign idAndTypeStrList><#list columnList as columnMap> <#if columnMap.isId=='true'>@Param("${StringUtils.underlineToCamelCase(columnMap.col)}") ${columnMap.javaType} ${StringUtils.underlineToCamelCase(columnMap.col)},<#assign idLength=idLength+1/></#if></#list></#assign>
<#assign idAndTypeStrList="${StringUtils.trimToEmpty(idAndTypeStrList)}"/>
package ${mapperPackage};

<#if (idLength>0)>
import org.apache.ibatis.annotations.Param;
</#if>
<#if StringUtils.isNotBlank(baseMapperFullName)>
import ${baseMapperFullName};
</#if>
import ${entityFullName};

<#if StringUtils.isNotBlank(annotationFullName)>
import ${annotationFullName};
import org.apache.ibatis.annotations.Param;
@${StringUtils.substringAfterLast(annotationFullName,".")}
</#if>
public interface ${mapperName} <#if StringUtils.isNotBlank(baseMapperFullName)>extends ${StringUtils.substringAfterLast(baseMapperFullName,".")}<${entityName}></#if> {

<#if (idLength>0)>
    ${entityName} selectByPrimaryKey(${idAndTypeStrList?substring(0,(idAndTypeStrList?length)-1)});
</#if>

}