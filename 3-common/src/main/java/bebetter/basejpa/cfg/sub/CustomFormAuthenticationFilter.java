package bebetter.basejpa.cfg.sub;


import cn.hutool.json.JSONUtil;
import bebetter.basejpa.model.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (HttpMethod.OPTIONS.name().equals(((HttpServletRequest) request).getMethod())) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    //没登录/没权限时
    @Override
    protected boolean onAccessDenied(ServletRequest req, ServletResponse resp) throws IOException {
        boolean logined = SecurityUtils.getSubject().isAuthenticated();
        int status = logined ? HttpServletResponse.SC_FORBIDDEN/*// 403 */ : HttpServletResponse.SC_UNAUTHORIZED/*// 401 */;
        String info = logined ? "您暂无该权限." : "您未登录,或者登陆已过期.";

        log.info("请求鉴权失败{}|失败原因:{}", status, info);

        HttpServletResponse resp1 = (HttpServletResponse) resp;
        resp1.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("Origin"));
        resp1.setHeader("Access-Control-Allow-Credentials", "true");
        //resp1.setStatus(status);
        resp1.setCharacterEncoding("UTF-8");
        PrintWriter out = resp1.getWriter();
        out.write(JSONUtil.toJsonStr(R.error(status, info)));
        out.flush();
        out.close();
        return false;
    }
}
