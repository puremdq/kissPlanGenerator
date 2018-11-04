<#assign hasId=0/>
<#assign idValueIsNullCompareStr>
    <#list columnList as columnMap>
        <#if columnMap.isId=='true'>
            <#assign hasId=1/>
            <#if columnMap.javaType=='String'>
                StringUtils.isNotBlank(${StringUtils.underlineToCamelCase(columnMap.col)}) &&
            <#else>
                ${StringUtils.underlineToCamelCase(columnMap.col)}!=null &&
            </#if>
        </#if>
    </#list>
</#assign>
<#assign idValueIsNullCompareStr="${StringUtils.trimToEmpty(idValueIsNullCompareStr)}"/>
package ${entityPackage};
import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

<#if StringUtils.isNotBlank(baseEntityFullName)>
import ${baseEntityFullName};
</#if>
import lombok.Data;

@Data
@TableName("${tableName}")
public class ${entityName} <#if StringUtils.isNotBlank(baseEntityFullName)>extends ${StringUtils.substringAfterLast(baseEntityFullName,".")}<${entityName}></#if> {

    private static final long serialVersionUID = 1L;

    public ${entityName}() {
    }

<#if (hasId==1)>
    <#assign idAndTypeStrList><#list columnList as columnMap> <#if columnMap.isId=='true'>${columnMap.javaType} ${StringUtils.underlineToCamelCase(columnMap.col)},</#if></#list></#assign>
    <#assign idAndTypeStrList="${StringUtils.trimToEmpty(idAndTypeStrList)}"/>
    <#assign constructorStr>
    <#list columnList as columnMap>
        <#if columnMap.isId=='true'>
        this.${StringUtils.underlineToCamelCase(columnMap.col)}=${StringUtils.underlineToCamelCase(columnMap.col)};
        </#if>
    </#list>
    </#assign>
    <#assign constructorStr="${StringUtils.trimToEmpty(constructorStr)}"/>
    public ${entityName}(${idAndTypeStrList?substring(0,(idAndTypeStrList?length)-1)}) {
        ${StringUtils.trimToEmpty(constructorStr)}
    }
</#if>


<#list columnList as columnMap>
    <#if !(StringUtils.findInSet(columnMap.col,'create_by,create_date,update_by,update_date,del_flag,remarks'))>
        <#if columnMap.isId=='true'>
    @TableId(value = "${columnMap.col}", type = IdType.UUID)
        <#else>
    @TableField(value = "${columnMap.col}")
        </#if>
    private ${columnMap.javaType} ${StringUtils.underlineToCamelCase(columnMap.col)};       //${columnMap.remarks}

    </#if>
</#list>

    @Override
	protected Serializable pkVal() {
<#if (hasId==1)>
		return this.id;
<#else>
        return null;
</#if>
	}
}