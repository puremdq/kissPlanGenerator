package ${controllerPackage};

<#if StringUtils.isNotBlank(baseControllerFullName)>
import ${StringUtils.substringBeforeLast(serviceFullName,".")}.I${StringUtils.substringAfterLast(serviceFullName,".")};
</#if>

import ${baseControllerFullName};
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/${entityName?uncap_first}")
public class ${controllerName} <#if StringUtils.isNotBlank(baseControllerFullName)>extends ${StringUtils.substringAfterLast(baseControllerFullName,".")}</#if> {

    @Autowired
    private I${StringUtils.substringAfterLast(serviceFullName,".")} ${StringUtils.substringAfterLast(serviceFullName,".")?uncap_first};


}
