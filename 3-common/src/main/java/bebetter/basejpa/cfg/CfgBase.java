package bebetter.basejpa.cfg;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
//@EnableCaching(proxyTargetClass = true)
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@ComponentScan("code")
//不能加 @EnableWebMvc 会导致CfgWeb中得拦截器失效.恢复Long到前台js精度丢失问题
public class CfgBase {
}
