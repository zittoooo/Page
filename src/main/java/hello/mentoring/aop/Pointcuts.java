package hello.mentoring.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* hello.mentoring..*.*(..))")
    public void log() {}

    @Pointcut("execution(* hello.mentoring.controller.*.*(..))")
    public void controller() {}

    @Pointcut("execution(* hello.mentoring.service.*.*(..))")
    public void service() {}

    @Pointcut("execution(* hello.mentoring.repository.*.*(..))")
    public void repository() {}
}
