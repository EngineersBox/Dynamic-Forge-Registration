package com.engineersbox.expandedfusion.core.dist;

import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DistProxyAspect {

    private static final Logger LOGGER = LogManager.getLogger(DistProxyAspect.class);

    @Around(
            value = "execution(* com.engineersbox.expandedfusion..*(..)) && @annotation(distBound)",
            argNames = "joinPoint,distBound"
    )
    public Object around(final ProceedingJoinPoint joinPoint, final DistBound distBound) throws Throwable {
        LOGGER.warn("Handled by @DistBound around advice");
        if (FMLEnvironment.dist == distBound.value()) {
            return joinPoint.proceed();
        }
        if (distBound.throwError()) {
            throw new RuntimeException(String.format(
                    "Invoked @DistBound annotated method for unsupported dist: %s != %s",
                    FMLEnvironment.dist.name(),
                    distBound.value().name()
            )); // TODO: Implement an exception for this
        }
        return null;
    }

    @Around("execution(* com.engineersbox.expandedfusion..*(..))")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.warn("Handled by capture all expandedfusion advice");
        return joinPoint.proceed();
    }
}
