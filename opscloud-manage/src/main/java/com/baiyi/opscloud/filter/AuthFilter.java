package com.baiyi.opscloud.filter;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.configuration.properties.WhiteConfigurationProperties;
import com.baiyi.opscloud.facade.audit.OperationalAuditFacade;
import com.baiyi.opscloud.facade.auth.UserAuthFacade;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

    private final OperationalAuditFacade operationalAuditFacade;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        if (!"options".equalsIgnoreCase(request.getMethod())) {
            final String resourceName = request.getServletPath();
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
            // 白名单-静态资源不拦截
            if (whiteConfig.getResources().stream().anyMatch(resourceName::endsWith)) {
                filterChain.doFilter(request, response);
                return;
            }
            try {
                final String token = request.getHeader(AUTHORIZATION);
                if (!StringUtils.isEmpty(token)) {
                    userAuthFacade.verifyUserHasResourcePermissionWithToken(token, resourceName);
                    operationalAuditFacade.save(resourceName, false);
                } else {
                    final String accessToken = request.getHeader(ACCESS_TOKEN);
                    userAuthFacade.verifyUserHasResourcePermissionWithAccessToken(accessToken, resourceName);
                    operationalAuditFacade.save(resourceName, true);
                }
                filterChain.doFilter(request, response);
            } catch (AuthenticationException ex) {
                response.setContentType("application/json;charset=UTF-8");
                setHeaders(request, response);
                response.getWriter().println(new HttpResult<>(ex));
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
