package ${servicePackage};

import ${baseServiceFullName};
import ${entityFullName};


public interface I${serviceName} extends ${StringUtils.substringAfterLast(baseServiceFullName,".")}<${StringUtils.substringAfterLast(entityFullName,".")}> {


}
