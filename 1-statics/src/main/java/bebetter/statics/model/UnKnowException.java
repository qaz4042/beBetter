package bebetter.statics.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnKnowException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public UnKnowException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public UnKnowException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public UnKnowException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public UnKnowException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
}
