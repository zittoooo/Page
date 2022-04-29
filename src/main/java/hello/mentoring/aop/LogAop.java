package hello.mentoring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Pointcut("execution(* hello.mentoring..*.*(..))")
    private void log() {}

    @Before("log()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.info(String.valueOf(method.getDeclaringClass()) + " " + method.getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg: args) {
            log.info("parameter value = {}", arg);
        }
    }
}
