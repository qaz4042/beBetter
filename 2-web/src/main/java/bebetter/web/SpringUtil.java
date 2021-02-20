package bebetter.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    public static Environment ENVIRONMENT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
        SpringUtil.ENVIRONMENT = SpringUtil.getBean(Environment.class);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> c) {
        return applicationContext.getBean(name, c);
    }

    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }
}
