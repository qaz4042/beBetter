package bebetter.jpa.util;

import bebetter.statics.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取lambda/Supplier方法中的方法名
 * 引自 org.hswebframework.ezorm.core    https://docs.hsweb.io/
 */
public class MethodUtil {

    private static final Map<Class, String> cache = new ConcurrentHashMap<>();

    public static String getLambdaColumnName(Object column) {
        return cache.computeIfAbsent(column.getClass(), t -> {
            SerializedLambda lambda = SerializedLambda.of(column);
            String methodName = lambda.getMethodName();
            String propName = lambda.getMethodName();
            if (methodName.startsWith("get")) {
                propName = StrUtil.lowerFirst(methodName.substring(3));
            } else if (methodName.startsWith("is")) {
                propName = StrUtil.lowerFirst(methodName.substring(2));
            }
            return propName;
        });
    }
}
