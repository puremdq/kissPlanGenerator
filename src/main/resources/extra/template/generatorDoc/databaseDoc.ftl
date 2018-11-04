<#if tableMap??>
<#list tableMap.keySet() as tableName>
### 表${tableName}
字段|类型|大小|是否可为空|默认值|备注
:---:|:---:|:---:|:---:|:---:|:---:
    <#list tableMap.get(tableName) as columnMap>
${columnMap.col}|${columnMap.databaseType}|${columnMap.colSize}|${columnMap.nullAble}|${StringUtils.trimToEmpty(columnMap.colDef)}|${columnMap.remarks}
    </#list>

</#list>
</#if>



