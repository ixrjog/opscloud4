package com.baiyi.opscloud.filter;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.HttpResult;
import com.baiyi.opscloud.facade.AuthBaseFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthFilter extends OncePerRequestFilter {

    @Resource
    private AuthBaseFacade authFacade;

    /**
     * 前端框架 token 名称
     */
    public static final String TOKEN = "x-token";

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars",
            "/ws/xterm"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!request.getMethod().equalsIgnoreCase("options")) {
            String resourceName = request.getServletPath();
            // 单页不拦截页面,只拦截协议请求
            if (resourceName.equalsIgnoreCase("/dashboard")) {
                filterChain.doFilter(request, response);
                return;
            }
            // 跳过白名单
            if (checkWhitelist(resourceName)) {
                filterChain.doFilter(request, response);
                return;
            }
            //静态资源不拦截
            if (resourceName.endsWith(".js")
                    || resourceName.endsWith(".css")
                    || resourceName.endsWith(".woff")
                    || resourceName.endsWith(".otf")
                    || resourceName.endsWith(".eot")
                    || resourceName.endsWith(".ttf")
                    || resourceName.endsWith(".svg")
                    || resourceName.endsWith(".jpg")
                    || resourceName.endsWith(".png")
                    || resourceName.endsWith(".html")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = request.getHeader(TOKEN);
            if (StringUtils.isEmpty(token))
                token = request.getParameter(TOKEN);
            // 鉴权
            BusinessWrapper<Boolean> wrapper = authFacade.checkUserHasResourceAuthorize(token, resourceName);
            if (!wrapper.isSuccess()) {
                HttpResult result = new HttpResult(wrapper.getCode(), wrapper.getDesc());
                setHeaders(request, response);
                response.setContentType("application/json;UTF-8");
                response.getWriter().println(JSON.toJSONString(result));
            } else {
                String username = authFacade.getUserByToken(token);
                if (!StringUtils.isEmpty(username)) {
                    SessionUtils.setUsername(username);
                    SessionUtils.setToken(token);
                }
                filterChain.doFilter(request, response);
            }
        } else {
            setHeaders(request, response);
            response.setContentType("application/json;UTF-8");
            filterChain.doFilter(request, response);
        }
    }

    private boolean checkWhitelist(String resourceName) {
        for (String resource : AUTH_WHITELIST)
            if (resourceName.indexOf(resource) == 0)
                return true;
        return false;
    }

    public void setHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.addHeader("Access-Control-Allow-Origin", origin);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Max-Age", "1800");//30 min
    }
}
