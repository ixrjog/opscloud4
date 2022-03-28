package com.baiyi.opscloud.filter;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.common.util.GitlabTokenUtil;
import com.baiyi.opscloud.config.properties.WhiteConfigurationProperties;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserAuthFacade userAuthFacade;

    private final WhiteConfigurationProperties whiteConfig;

    /**
     * 前端框架 token 名称
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * API 令牌
     */
    public static final String ACCESS_TOKEN = "AccessToken";

    public static final String GITLAB_TOKEN = "X-Gitlab-Token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        if (!"options".equalsIgnoreCase(request.getMethod())) {
            String resourceName = request.getServletPath();
            // 单页不拦截页面,只拦截协议请求
            if ("/dashboard".equalsIgnoreCase(resourceName)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 跳过白名单
            if (checkWhitelist(resourceName)) {
                filterChain.doFilter(request, response);
                return;
            }
            //静态资源不拦截
            if (Stream.of(".js", ".md", ".css", ".woff", ".otf", ".eot", ".ttf", ".svg", ".jpg", ".png", ".html").anyMatch(resourceName::endsWith)) {
                filterChain.doFilter(request, response);
                return;
            }
            // GitlabSystemHooks鉴权
            try {
                String gitlabToken = request.getHeader(GITLAB_TOKEN);
                if (!StringUtils.isEmpty(gitlabToken))
                    GitlabTokenUtil.setToken(gitlabToken);
            } catch (Exception ignored) {
            }
            try {
                final String token = request.getHeader(AUTHORIZATION);
                if (!StringUtils.isEmpty(token)) {
                    userAuthFacade.tryUserHasResourceAuthorize(token, resourceName);
                } else {
                    final String accessToken = request.getHeader(ACCESS_TOKEN);
                    userAuthFacade.tryUserHasResourceAuthorizeByAccessToken(accessToken, resourceName);
                }
                filterChain.doFilter(request, response);
            } catch (AuthRuntimeException ex) {
                response.setContentType(APPLICATION_JSON_UTF8_VALUE);
                setHeaders(request, response);
                HttpResult result = new HttpResult(ex);
                response.getWriter().println(result);
            }
        } else {
            setHeaders(request, response);
            filterChain.doFilter(request, response);
        }
    }

    private boolean checkWhitelist(String resourceName) {
        return whiteConfig.getUrls().stream().anyMatch(resource -> resourceName.indexOf(resource) == 0);
    }

    private void setHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        response.addHeader("Access-Control-Allow-Origin", origin);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "*");
        // 30 min
        // response.addHeader("Access-Control-Max-Age", "1800");
    }

}
