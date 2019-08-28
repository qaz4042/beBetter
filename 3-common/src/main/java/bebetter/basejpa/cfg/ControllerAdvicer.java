
package bebetter.basejpa.cfg;

import bebetter.generate.annotation.GEntity;
import bebetter.statics.util.V;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@ConditionalOnProperty(value = "code.isAdmin", havingValue = "false", matchIfMissing = true)
@ControllerAdvice
public class ControllerAdvicer implements ResponseBodyAdvice {
//    @Value("#{code.isAdmin}")
//    Boolean isAdmin;

//    @ModelAttribute("userId")//username role
//    public Long registerUserInfo() {
//        return LoginUtil.getId();
//    }


//    @InitBinder
//    public void initBinder(ServletRequestDataBinder binder) {
//        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
//            @Override
//            public void setAsText(String text) {
//                if (C.k_空字符.equals(text)) {
//                    text = null;
//                }
//                this.setValue(text);
//            }
//        });
//    }


    @SuppressWarnings("unchecked")
    @Override
    @SneakyThrows
    public Object beforeBodyWrite(Object value, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        returnType.getExecutable().cla
        if (null != value) {
            Class<?> modelClass = getModelClass(returnType);
            if (value instanceof Page) {
                List<String> hideEntitys = hideColumns(modelClass);
                if (V.noEmpty(hideEntitys)) {
                    ((Page<?>) value).getContent().forEach(value1 -> dealOne(modelClass, value1, hideEntitys));
                }
            } else if (value instanceof Collection) {
                List<String> hideEntitys = hideColumns(modelClass);
                if (V.noEmpty(hideEntitys)) {
                    ((Collection) value).forEach(value1 -> dealOne(modelClass, value1, hideEntitys));
                }
            } else if (value instanceof Map) {
                List<String> hideEntitys = hideColumns(modelClass);
                if (V.noEmpty(hideEntitys)) {
                    ((Map) value).values().forEach(value1 -> dealOne(modelClass, value1, hideEntitys));
                }
            } else {
                List<String> hideEntitys = hideColumns(modelClass);
                if (V.noEmpty(hideEntitys)) {
                    dealOne(modelClass, value, hideEntitys);
                }
            }
        }
        return value;
    }

    private Class<?> getModelClass(MethodParameter returnType) throws NoSuchFieldException, IllegalAccessException {
        Field this$0 = returnType.getClass().getDeclaredField("this$0");
        this$0.setAccessible(true);
        Class<?> controllerClass = ((ServletInvocableHandlerMethod) this$0.get(returnType)).getBeanType();
        return ClassUtil.getTypeArgument(controllerClass);
    }

    private List<String> hideColumns(Class clazz) {
        if (null != clazz) {
            GEntity anno = (GEntity) clazz.getAnnotation(GEntity.class);
            if (null != anno) {
                String[] columns = anno.userHideColumns();
                return Arrays.stream(columns).filter(s -> !"".equals(s)).collect(Collectors.toList());
            }
        }
        //noinspection unchecked
        return Collections.EMPTY_LIST;
    }

    private void dealOne(Class<?> modelClass, Object value, List<String> hideEntitys) {
        if (null != value && value.getClass().equals(modelClass)) {
            hideEntitys.forEach(prop -> BeanUtil.setFieldValue(value, prop, null));
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }
}
