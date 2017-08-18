package com.ucsmy.aos.api.common.exception.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 异常相关bean注册类
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.ucsmy.aos.api.common.exception.aspect")
public class ExceptionConfig {

}
