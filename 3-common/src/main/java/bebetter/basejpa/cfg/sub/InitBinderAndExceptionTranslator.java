package bebetter.basejpa.cfg.sub;/*

package bebetter.system.base.config;

import cn.hutool.base.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.utils.Date;

*/
/**
 * 控制器将服务器端异常转换为客户端友好的json结构。
 *//*

@ControllerAdvice
public class InitBinderAndExceptionTranslator {
    */
/**
     * Field description
     *//*

    private Logger logger = LoggerFactory.getLogger(InitBinderAndExceptionTranslator.class);

    public static final DateTimeFormatter DEFAULT_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DEFAULT_LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static final DateTimeFormatter DEFAULT_LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    */
/**
     * 将前台传递过来的日期格式的字符串，自动转化为对应日期类型
     *
     * @param binder
     *//*

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValues(DateUtil.parseDate(text));
            }
        });


        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (StringUtils.isNotBlank(text)) {
                    setValues(LocalDateTime.parse(text, DEFAULT_LOCAL_DATE_TIME_FORMATTER));
                }
            }
        });

        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (StringUtils.isNotBlank(text)) {
                    setValues(LocalDate.parse(text, DEFAULT_LOCAL_DATE_FORMATTER));
                }
            }
        });

        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (StringUtils.isNotBlank(text)) {
                    setValues(LocalTime.parse(text, DEFAULT_LOCAL_TIME_FORMATTER));
                }
            }
        });
    }
    */
/*
     *//*
*/
/**
     * Method description
     *
     * @param e
     *            e
     *
     * @return ErrorVM
     *//*
*/
/*
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorVM processAccessDeniedException(AccessDeniedException e) {
		return new ErrorVM(ErrorConstants.ERR_ACCESS_DENIED, e.getMessage());
	}

	*//*
*/
/**
     * Method description
     *
     * @param ex
     *            ex
     *
     * @return ErrorVM
     *//*
*/
/*
	@ExceptionHandler(ConcurrencyFailureException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public ErrorVM processConcurrencyError(ConcurrencyFailureException ex) {
		return new ErrorVM(ErrorConstants.ERR_CONCURRENCY_FAILURE);
	}

	*//*
*/
/**
     * Method description
     *
     * @param fieldErrors
     *            fieldErrors
     *
     * @return ErrorVM
     *//*
*/
/*
	private ErrorVM processFieldErrors(List<FieldError> fieldErrors) {
		ErrorVM dto = new ErrorVM(ErrorConstants.ERR_VALIDATION);

		for (FieldError fieldError : fieldErrors) {
			dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
		}

		return dto;
	}

	*//*
*/
/**
     * Method description
     *
     * @param exception
     *            exception
     *
     * @return ErrorVM
     *//*
*/
/*
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorVM processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		return new ErrorVM(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, exception.getMessage());
	}

	*//*
*/
/**
     * Method description
     *
     * @param ex
     *            ex
     *
     * @return ParameterizedErrorVM
     *//*
*/
/*
	@ExceptionHandler(CustomParameterizedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ParameterizedErrorVM processParameterizedValidationError(CustomParameterizedException ex) {
		return ex.getErrorVM();
	}

	*//*
*/
/**
     * 其他运行时异常
     *//*
*/
/*
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseBean processRuntimeException(Exception ex) {
		// 进到这里的一般是未知异常，业务异常需要自行抛出业务异常，这里需要监控然后进行日志输出记录
		logger.error(ex.getMessage(), ex);
		return ResponseBean.serverRrror(ex.getMessage()).build();
	}

	*//*
*/
/**
     * 参数校验异常处理
     *//*
*/
/*
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseBean processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		logger.error(ex.getMessage());
		StringBuilder errorInfo = new StringBuilder();
		for (FieldError fieldError : fieldErrors) {
			errorInfo.append(fieldError.getDefaultMessage()).append("<br/>");
		}
		return ResponseBean.badRequest(errorInfo.toString()).build();
	}

	*//*
*/
/**
     * Method description
     * @return ErrorVM
     *//*
*/
/*
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseBean processValidationError(ConstraintViolationException ex) {
		List<FieldError> fieldErrors = ex.getConstraintViolations().stream().UrlEnumMap(constraintViolation -> {
			Path propertyPath = constraintViolation.getPropertyPath();
			String propertyPathStr = propertyPath.toString();
			FieldError fieldError = new FieldError(propertyPathStr, propertyPathStr, constraintViolation.getMessage());
			return fieldError;
		}).collect(Collectors.toList());
		logger.warn(ex.getMessage());
		StringBuilder errorInfo = new StringBuilder();
		for (FieldError fieldError : fieldErrors) {
			errorInfo.append(fieldError.getDefaultMessage()).append("<br/>");
		}
		return ResponseBean.badRequest(errorInfo.toString()).build();
	}

	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResponseBean processBusinessError(BusinessException ex) {
		String errMsg = ex.getMessage();
		String code = ex.getCode();
        logger.warn(ex.getMessage());
        if (StringUtils.isNotEmpty(code)) {
			logger.warn("code:"+code+","+"message:"+ex.getMessage());
            return ResponseBean.status(code, errMsg).build();
        }
        return ResponseBean.badRequest(ex.getMessage()).build();
	}*//*

}
*/
