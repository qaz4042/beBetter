package bebetter.basejpa.model.vo;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Controller接口返回对象
 * 推荐返回 ResponseEntity 就可以设置contentType:json
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class R extends LinkedHashMap<String, Object> {
    public static final Integer OK = HttpStatus.OK.value();
    public static final Integer ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public R() {
        put("code", OK);
    }

    public R(Integer code, String msg) {
        put("code", code);
        put("msg", msg);
    }

    public static R ok() {
        return ok(null);
    }

    public static R ok(String msg) {
        return new R(OK, msg);
    }

    public static R error(String msg) {
        return new R(ERROR, msg);
    }

    public static R error(Integer code, String msg) {
        return new R(code, msg);
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
