package bebetter.web;

import bebetter.statics.constant.C;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    //yml里的配置
    @Autowired
    private WebProperties beBetterWebProperties;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        //跨域cors拦截
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", beBetterWebProperties.getCors());
        return new FilterRegistrationBean<>(new CorsFilter(configurationSource));
    }

//    /**
//     * Gson 隐藏属性的条件
//     */
//    static final List<Function<FieldAttributes, Boolean>> HideMethods = Arrays.asList(
//            f -> f.getDeclaringClass().isAssignableFrom(PageImpl.class) && "pageable".equals(f.getName()),  //1.隐藏冗余PageImpl.pageable属性
//            f -> V.and(f.getAnnotation(GColumn.class), GColumn::hideUser)                                   //2.isAdmin!=true时,定制隐藏hideUser=true字段
//    );

    //不是后台 就过滤部分属性
    @Bean
    Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
//                .registerTypeAdapter(PageImpl.class, (JsonSerializer<LocalTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
//        if (!codeProperties.getIsAdmin()) {
//            gsonBuilder.addSerializationExclusionStrategy(new ExclusionStrategy() {
//                @Override
//                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
//                    return HideMethods.stream().map(fun -> fun.apply(fieldAttributes)).filter(Objects::nonNull).anyMatch(Boolean::booleanValue);
//                }
//
//                @Override
//                public boolean shouldSkipClass(Class<?> aClass) {
//                    return false;
//                }
//            });
//        }

        return gsonBuilder.create();
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Gson gson = gson();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof GsonHttpMessageConverter) {
                //用jackson处理js和对象互转
                ((GsonHttpMessageConverter) converter).setGson(gson);
                break;
            }
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态文件
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + SpringUtil.ENVIRONMENT.getProperty(C.SYS_FILE_SAVEPATH) + C.x_斜杠);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        interceptors().forEach(registry::addInterceptor);
    }

    public List<HandlerInterceptor> interceptors() {
        return Collections.emptyList();
    }
}
