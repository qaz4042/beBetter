package bebetter.basejpa.cfg;

import bebetter.basejpa.cfg.sub.CustomFormAuthenticationFilter;
import bebetter.basejpa.cfg.sub.IRealm;
import bebetter.statics.model.KnowException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * shiro 基于注解的配置
 * <p>
 * 实现IRealm,并注入spring@Component,[SecurityUtils.getSubject().login()在登录时,判断账号密码,加载用户基本信息,加载角色和权限]
 * <p>
 * 定制拦截
 * <p>
 * ----- 1. 需要先实现 IRealm 来登录/鉴权 -----
 * <p>
 * Shiro生命周期处理器
 * <p>
 * 自动创建代理
 * <p>
 * 开启shiro aop注解支持.
 * 使用代理方式;所以需要开启代码支持;否则@RequiresRoles等注解无法生效
 */

//@Configuration
@ConditionalOnClass(ShiroFilterFactoryBean.class)
public interface ICfgShiro {


    /**
     * 实现IRealm,并注入spring@Component,[SecurityUtils.getSubject().login()在登录时,判断账号密码,加载用户基本信息,加载角色和权限]
     */

    @SuppressWarnings("unused")
    Class<? extends IRealm> getIRealm();

    /**
     * 定制拦截
     */

    default LinkedHashMap<String, String> getFilterChainDefinitionMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(16);
        //authc:登录了(authed)才能访问;     anon:可随意访问
        // 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        map.put("/", "anon");
        map.put("/login/submit", "anon");
        map.put("/login/logout", "anon");
        map.put("/**", "authc");
        return map;
    }

    String Websocket_EndPoint = "/websocket";

    @Bean
    default FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setOrder(Integer.MAX_VALUE - 1);
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        //noinspection unchecked
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistrationBean;
    }

    /**
     * ----- 1. 需要先实现 IRealm 来登录/鉴权 -----
     */

    @Bean
    default SecurityManager securityManager(IRealm realm) {
        if (null == realm) {
            throw new KnowException("使用shiro前,请先实现IRealm,并注入spring容器(添加@Component).");
        }
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
//      realm.setCredentialsMatcher(null);  在AuthorizingRealm.doGetAuthenticationInfo中已经校验了账号密码,在这里设置为空不再无用的二次校验
        securityManager.setRealm(realm);
//      securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    default ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
        filters.put("authc", new CustomFormAuthenticationFilter());//将自定义 的FormAuthenticationFilter注入shiroFilter中
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //默认跳转到登陆页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        //登陆成功后的页面
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        //权限控制map
        LinkedHashMap<String, String> map = getFilterChainDefinitionMap();
        LinkedHashMap<String, String> mapNew = new LinkedHashMap<>(32);
        mapNew.put(Websocket_EndPoint + "/**", "anon");

        //spring-boot-admin-starter-server监控需要
        mapNew.put("/actuator" + "/**", "anon");
        mapNew.put("/applications" + "/**", "anon");
        mapNew.put("/instances" + "/**", "anon");
        mapNew.putAll(map);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(mapNew);
        return shiroFilterFactoryBean;
    }

/*
     哈希密码比较器。在myShiroRealm中作用参数使用
     登陆时会比较用户输入的密码，跟数据库密码配合盐值salt解密后是否一致。
    @Bean
    default HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用md5算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5( md5(""));
        return hashedCredentialsMatcher;
    }
/**
 * Shiro生命周期处理器
 */

    @Bean
    default LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * 自动创建代理
     */

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    default DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;否则@RequiresRoles等注解无法生效
     */

    @Bean
    @DependsOn("securityManager")
    default AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
