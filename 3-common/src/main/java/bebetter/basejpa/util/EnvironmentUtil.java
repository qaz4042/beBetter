package bebetter.basejpa.util;

import bebetter.basejpa.constant.C_System;
import org.springframework.core.env.Environment;

public class EnvironmentUtil {
    // properties/yml/xml  环境配置信息
    public static Environment ENVIRONMENT = SpringUtils.getBean(Environment.class);
    //根路径
    public static final String BasePath = ENVIRONMENT.getProperty(C_System.FileSavePath);

}
