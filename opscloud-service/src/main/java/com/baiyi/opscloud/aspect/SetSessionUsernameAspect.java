package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.SetSessionUsername;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2023/5/8 09:19
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(1)
public class SetSessionUsernameAspect {

    private final UserService userService;

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.SetSessionUsername)")
    public void annotationPoint() {
    }

    @Before("@annotation(setSessionUsername)")
    public void doBefore(JoinPoint joinPoint, SetSessionUsername setSessionUsername) throws Throwable {
        if (!setSessionUsername.force()) {
            if (StringUtils.isNotBlank(SessionHolder.getUsername())) {
                return;
            }
        }

        String username = getUsernameWithSpEL(joinPoint, setSessionUsername);
        User user = userService.getByUsername(username);
        if (user == null) {
            log.warn("设置当前会话但用户 {} 不存在!", username);
        } else {
            SessionHolder.setUsername(user.getUsername());
            SessionHolder.setUserId(user.getId());
        }
    }

    private String getUsernameWithSpEL(JoinPoint joinPoint, SetSessionUsername setSessionUsername) {
        //获取切面方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法的形参名称
        String[] params = discoverer.getParameterNames(method);
        //获取方法的实际参数值
        Object[] arguments = joinPoint.getArgs();
        //设置解析SpEL所需的数据上下文
        EvaluationContext context = new StandardEvaluationContext();
        IntStream.range(0, Objects.requireNonNull(params).length).forEachOrdered(len -> context.setVariable(params[len], arguments[len]));
        //解析表达式并获取SpEL的值
        Expression expression = expressionParser.parseExpression(setSessionUsername.usernameSpEL());
        Object usernameParam = expression.getValue(context);

        if (usernameParam instanceof String username) {
            if (StringUtils.isNotBlank(username)) {
                return username;
            } else {
                throw new OCException("用户名不存在！");
            }
        } else {
            throw new OCException("指定的用户名参数类型不正确！");
        }
    }

}