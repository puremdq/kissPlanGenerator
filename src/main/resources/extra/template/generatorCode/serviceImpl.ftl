package ${servicePackage}.impl;

import javax.annotation.Resource;
import ${servicePackage}.I${serviceName};
import ${entityFullName};
<#if StringUtils.isNotBlank(baseImplServiceFullName)>
import ${baseImplServiceFullName};
import ${mapperFullName};
</#if>

import org.springframework.stereotype.Service;

@Service
public class ${serviceName}Impl<#if StringUtils.isNotBlank(baseServiceFullName)> extends ${StringUtils.substringAfterLast(baseImplServiceFullName,".")}<${StringUtils.substringAfterLast(mapperFullName,".")},${StringUtils.substringAfterLast(entityFullName,".")}></#if> implements I${serviceName}{

    @Resource
    private ${StringUtils.substringAfterLast(mapperFullName,".")} ${StringUtils.substringAfterLast(mapperFullName,".")?uncap_first};

}
