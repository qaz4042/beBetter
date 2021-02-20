package bebetter.web;

import bebetter.statics.constant.C;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

/**
 * 读取yml系统级参数
 */
@Component
@ConfigurationProperties(prefix = C.SYS_PACKAGE + ".web")
@Data
public class WebProperties {
    CorsConfiguration cors;
}
