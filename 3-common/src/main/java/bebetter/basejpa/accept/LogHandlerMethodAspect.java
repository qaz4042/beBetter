package bebetter.basejpa.accept;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面拦截类
 */
@Aspect
@Component
public class LogHandlerMethodAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private UserLoginLogService userLoginLogService;

    @Pointcut("@annotation(bebetter.basejpa.accept.LogAnnotation)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        // 保存日志
        saveUserLoginLog(point, time);

        return result;
    }

    private void saveUserLoginLog(ProceedingJoinPoint joinPoint, Long time) {
        // todo 可以在这里更新ip及保存登录日志
    }
}
