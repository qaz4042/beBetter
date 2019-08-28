package bebetter.statics.model;

import bebetter.statics.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;

/**
 * 自定义异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KnowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public KnowException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public KnowException(String template, Object... param) {
        super(StrUtil.format(template, param));
        this.msg = StrUtil.format(template, param);
    }

    public KnowException(Throwable e, String msg) {
        super(msg, e);
        this.msg = msg;
    }

    public KnowException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public KnowException(Throwable e, String msg, int code) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
