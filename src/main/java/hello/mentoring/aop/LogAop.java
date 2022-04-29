package hello.mentoring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Before("hello.mentoring.aop.Pointcuts.log()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.info("[before] {}", joinPoint.getSignature());

        Object[] args = joinPoint.getArgs();
        for (Object arg: args) {
            log.info("parameter value = {}", arg);
        }
    }

    @AfterReturning(value="hello.mentoring.aop.Pointcuts.log()", returning = "result")
    public void doRetutn(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.info("[return] {}", joinPoint.getSignature());

        Object[] args = joinPoint.getArgs();
        for (Object arg: args) {
            log.info("parameter value = {}", arg);
        }
    }

    @AfterThrowing(value="hello.mentoring.aop.Pointcuts.log()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }



}
