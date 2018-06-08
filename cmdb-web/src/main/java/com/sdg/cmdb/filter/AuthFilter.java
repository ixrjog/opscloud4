package com.sdg.cmdb.filter;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zxxiao on 16/9/22.
 */
@Service
public class AuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Resource
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("options")) {
            String resourceName = request.getServletPath();

            //单页不拦截页面,只拦截协议请求
            if (resourceName.equalsIgnoreCase("/dashboard")) {
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

            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                token = request.getParameter("token");
            }

            BusinessWrapper<Boolean> wrapper = authService.checkUserHasResourceAuthorize(token, resourceName);
            if (!wrapper.isSuccess()) {
                HttpResult result = new HttpResult(wrapper.getCode(), wrapper.getMsg());

                setHeaders(request, response);

                response.setContentType("application/json;UTF-8");
                response.getWriter().println(JSON.toJSONString(result));
            } else {
                String username = authService.getUserByToken(token);
                if (!StringUtils.isEmpty(username)) {
                    SessionUtils.setUsername(username);
                    SessionUtils.setToken(token);
                }
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
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
