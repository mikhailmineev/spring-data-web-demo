package ru.sbrf.sb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("dev")
@Slf4j
public class LoggingAspect {

    @Around("@annotation(ru.sbrf.sb.aspect.Logging)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        log.info("calling " + pjp.getSignature());
        Object retVal = pjp.proceed();
        log.info("called " + pjp.getSignature());
        return retVal;
    }
}
