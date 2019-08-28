package bebetter.basejpa.cfg.sub;

import bebetter.statics.model.KnowException;
import bebetter.basejpa.model.vo.R;
import bebetter.statics.util.StrUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

/**
 * 异常拦截
 */
@ControllerAdvice
public class ExceptionsHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 很明显,能掌控/提示性的Exception
     */
    @ExceptionHandler({KnowException.class, DuplicateKeyException.class, AuthorizationException.class, BindException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public R catch_knowException(Exception e) {
        Integer code = R.ERROR;
        String message = e.getMessage();
        if (e instanceof KnowException) {
            code = ((KnowException) e).getCode();
        }
        if (e instanceof DuplicateKeyException) {
            message = "数据库中已存在该记录(唯一标识重复).";
        }
        if (e instanceof AuthorizationException) {
            message = "未登录/登录失效";
        }
        if (e instanceof BindException) {
            message = ((BindException) e).getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        }
        logger.info("KnowException|{}", message);
        return new R(code, message);
    }

    /**
     * 不可估量的Exception
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)//统一不抛错误 500
    public R catch_unKnowException(Exception e) {
        logger.error(e.getMessage(), e);
        String message = e.getMessage();
        message = StrUtil.isContainsChinese(message) ? message : StrUtil.getHead(message, 50);   //中文全展示,纯英文只展示部分信息
        return R.error(message);
    }

}
