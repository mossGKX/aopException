package com.ucsmy.aos.api.common.exception.aspect;

import com.ucsmy.aos.api.common.constants.AosResult;
import com.ucsmy.aos.api.common.constants.Constants;
import com.ucsmy.aos.api.common.constants.ResultConst;
import com.ucsmy.aos.api.common.exception.ServiceException;
import com.ucsmy.aos.api.common.exception.utils.ExceptionUtils;
import com.ucsmy.aos.api.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * webControl异常切面
 * 默认spring aop不会拦截controller层，使用该类需要在spring公共配置文件中注入改bean，
 * 另外需要配置<aop:aspectj-autoproxy proxy-target-class="true"/>
 */
@Aspect
@Component
@Order(2)
@Slf4j
public class WebExceptionAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void webPointcut() {}

    /**
     * 拦截web层异常，记录异常日志，并返回友好信息到前端
     * 目前只拦截Exception，是否要拦截Error需再做考虑
     *
     * @param e 异常对象
     */
    @AfterThrowing(pointcut = "webPointcut()", throwing = "e")
    public void handleThrowing(Exception e) {
        //不需要再记录ServiceException，因为在service异常切面中已经记录过
        if (!(e instanceof ServiceException)) {
            log.error(ExceptionUtils.getExcTrace(e));
        }
        String errorMsg = StringUtils.isEmpty(e.getMessage()) ? "系统异常" : e.getMessage();
        writeContent(errorMsg);
    }

    /**
     * 将内容输出到浏览器
     *
     * @param content 输出内容
     */
    private void writeContent(String content) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/json;charset=UTF-8");
        response.setHeader("icop-content-type", "exception");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AosResult result = new AosResult();
        result.setRes(ResultConst.ERROR);
        result.setDes(content);
        writer.print(JsonUtils.formatObjectToJson(result));
        writer.flush();
        writer.close();
    }
}
