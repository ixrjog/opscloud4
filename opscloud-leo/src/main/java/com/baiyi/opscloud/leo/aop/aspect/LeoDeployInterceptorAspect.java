package com.baiyi.opscloud.leo.aop.aspect;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.leo.aop.annotation.LeoDeployInterceptor;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.baiyi.opscloud.leo.helper.LeoDeployPassCheck;
import com.baiyi.opscloud.leo.interceptor.LeoExecuteJobInterceptorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
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
 * @Date 2022/12/29 13:29
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LeoDeployInterceptorAspect {

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private final LeoExecuteJobInterceptorHandler executeJobInterceptorHandler;

    private final LeoDeployPassCheck leoDeployPassCheck;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.leo.aop.annotation.LeoDeployInterceptor)")
    public void annotationPoint() {
    }

    @Around("@annotation(leoDeployInterceptor)")
    public Object around(ProceedingJoinPoint joinPoint, LeoDeployInterceptor leoDeployInterceptor) throws Throwable {
        //获取切面方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法的形参名称
        String[] params = discoverer.getParameterNames(method);
        //获取方法的实际参数值
        Object[] arguments = joinPoint.getArgs();

        //设置解析SpEL所需的数据上下文
        EvaluationContext context = new StandardEvaluationContext();
        IntStream.range(0, Objects.requireNonNull(params).length).forEach(len -> context.setVariable(params[len], arguments[len]));

        int jobId = parseJobId(leoDeployInterceptor, context);
        int buildId = parseBuildId(leoDeployInterceptor, context);
        int assetId = parseAssetId(leoDeployInterceptor, context);

        // 并发校验
        if (leoDeployInterceptor.lock()) {
            executeJobInterceptorHandler.tryLockWithDeploy(jobId, assetId);
        }
        if (executeJobInterceptorHandler.isAdmin(SessionHolder.getUsername())) {
            log.debug("管理员操作，跳过验证");
        } else {
            // 权限校验
            executeJobInterceptorHandler.verifyAuthorization(jobId);
            // 工单校验
            if (!leoDeployPassCheck.checkPass(buildId)) {
                String deployType = parseDeployType(leoDeployInterceptor, context);
                if (DeployTypeConstants.ROLLING.name().equalsIgnoreCase(deployType)) {
                    // 规则校验
                    executeJobInterceptorHandler.verifyRule(jobId);
                }
            }
        }

        return joinPoint.proceed();
    }

    private String parseDeployType(LeoDeployInterceptor leoDeployInterceptor, EvaluationContext context) {
        // deployType Expression 解析表达式并获取SpEL的值
        Expression deployTypeExpression = expressionParser.parseExpression(leoDeployInterceptor.deployTypeSpEL());
        Object deployTypeParam = deployTypeExpression.getValue(context);
        if (deployTypeParam instanceof String deployType) {
            return deployType;
        } else {
            throw new LeoJobException("参数DeployType类型不正确！");
        }
    }

    private int parseJobId(LeoDeployInterceptor leoDeployInterceptor, EvaluationContext context) {
        // jobId Expression 解析表达式并获取SpEL的值
        Expression jobIdExpression = expressionParser.parseExpression(leoDeployInterceptor.jobIdSpEL());
        Object jobIdParam = jobIdExpression.getValue(context);

        if (jobIdParam instanceof Integer) {
            int jobId = (Integer) jobIdParam;
            if (IdUtil.isEmpty(jobId)) {
                throw new LeoJobException("任务ID不存在！");
            }
            return jobId;
        } else {
            throw new LeoJobException("任务ID类型不正确！");
        }
    }

    private int parseAssetId(LeoDeployInterceptor leoDeployInterceptor, EvaluationContext context) {
        // jobId Expression 解析表达式并获取SpEL的值
        Expression jobIdExpression = expressionParser.parseExpression(leoDeployInterceptor.deploymentAssetIdSpEL());
        Object assetIdParam = jobIdExpression.getValue(context);

        if (assetIdParam instanceof Integer) {
            int assetId = (int) assetIdParam;
            if (IdUtil.isEmpty(assetId)) {
                throw new LeoJobException("部署无状态ID不存在！");
            }
            return assetId;
        } else {
            throw new LeoJobException("部署无状态ID类型不正确！");
        }
    }

    private int parseBuildId(LeoDeployInterceptor leoDeployInterceptor, EvaluationContext context) {
        Expression buildIdExpression = expressionParser.parseExpression(leoDeployInterceptor.buildIdSpEL());
        Object buildIdParam = buildIdExpression.getValue(context);
        if (buildIdParam instanceof Integer) {
            return (int) buildIdParam;
        }
        return 0;
    }

}