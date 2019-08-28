package bebetter.basejpa.util;

import bebetter.basejpa.enums.ISysRole;
import bebetter.statics.model.KnowException;
import bebetter.statics.util.V;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
@Slf4j
public class LoginUtil {

    /**
     * 是否已登录
     */
    public static boolean isLogined() {
        return null != getId();// || EnumSysRoleBase.游客.equals(getRole);
    }

    /**
     * 是否已登录
     */
    public static <T> void doUserId(Consumer<T> useIdFun) {
        T userId = getId();
        if (null != userId) {
            useIdFun.accept(userId);
        }
    }


    /**
     * 当前user/admin/agent(代理)的id
     */
    public static <ID> ID getId(HttpServletRequest request) {
        ID id = getIdNoThrow(request);
        if (null == id) {
            log.warn("请先登录|{}|{}", request.getRequestURI(), request.getParameterMap());
            throw new KnowException("请先登录.");
        }
        return id;
    }

    public static <ID> ID getIdNoThrow(HttpServletRequest request) {
        return V.and(getSession(request), session -> (ID) session.getAttribute(id));
    }

    /**
     * 当前user/admin/agent(代理)的id
     * 性能比较差
     */
    public static <T> T getId() {
        return V.and(getSession(getRequest()), session -> (T) session.getAttribute(id));
    }

    public static <T> T getUsername() {
        return V.and(getSession(getRequest()), session -> (T) session.getAttribute(username));
    }

    public static <T extends ISysRole> T getRole() {
        return V.and(getSession(getRequest()), session -> (T) session.getAttribute(role));
    }

    private static HttpSession getSession(HttpServletRequest request) {
        return V.and(request, HttpServletRequest::getSession);
    }

    private static HttpServletRequest getRequest() {
        return V.and((ServletRequestAttributes) RequestContextHolder.getRequestAttributes(), ServletRequestAttributes::getRequest);
    }

    private static final String id = "id";
    private static final String username = "username";
    private static final String role = "role";
}
