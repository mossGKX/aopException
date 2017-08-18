package com.ucsmy.aos.api.common.exception.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author ucs_gaokx
 * @date 2017-4-13
 * @version V1.0
 * @ClassName: ArroundControllerAspect
 * @Description: 处理所有controller类中方法的环绕通知，什么事情都不做，就是执行方法，为了和WebExceptionAspect配合
 *               切面定义和 WebExceptionAspect 一致，并且优先级要高于WebExceptionAspect,用于解决通过HttpServletResponse
 *               输出的一些问题:如response不能reset，getWriter方法调用引发的一些异常
 *
 *
 */
@Slf4j
@Aspect
@Order(1)
@Component("com.ucsmy.aos.api.common.exception.aspect.ArroundControllerAspect")
public class ArroundControllerAspect {
    /**
     *
     * runControllerMethod
     *
     * @Title: logTheController
     * @Description: 定义一个切面，指向所有的controller类中的所有方法
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void runControllerMethod() {
        // Nothing to clean up
    }

    /**
     *
     * runControllerMethod
     * @Title: runControllerMethod
     * @Description: 什么都不处理，仅仅是增加一个环绕通知.
     * 这里必须返回一个object，把处理过程交回给controller。否则controller中的@ResponseBody不会起作用了
     * @param joinPoint
     */
    @Around("runControllerMethod()")
    private Object runControllerMethod(ProceedingJoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("runControllerMethod(ProceedingJoinPoint joinPoint={}) - start", joinPoint); //$NON-NLS-1$
        }
        Object rtnObject = null;
        try {
            rtnObject = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.toString());
        }

        if (log.isDebugEnabled()) {
            log.debug("runControllerMethod(ProceedingJoinPoint joinPoint={}) - end", joinPoint); //$NON-NLS-1$
        }
        return rtnObject;
    }
}

