package ua.com.gfalcon.logger.loggabletype.cases.repeatedfieldnames;

import ua.com.gfalcon.logger.annotation.LoggableType;
import ua.com.gfalcon.logger.loggabletype.cases.BasePojo;

@LoggableType
public class RepatedFieldsParentPojo implements BasePojo
{
  @LoggableType.Property
  public String field1 = "POJO_A11_BASE_1_FIELD_1";
}
