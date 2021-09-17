package com.engineersbox.expandedfusion.core.dist;

import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import com.engineersbox.expandedfusion.core.dist.exception.DistRestrictionException;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DistProxyAspect {

    @Around(
            value = "execution(* com.engineersbox.expandedfusion..*(..)) && @annotation(distBound)",
            argNames = "joinPoint,distBound"
    )
    public Object aroundDistRestrictedCall(final ProceedingJoinPoint joinPoint,
                                           final DistBound distBound) throws Throwable {
        if (FMLEnvironment.dist == distBound.value()) {
            return joinPoint.proceed();
        }
        if (distBound.throwError()) {
            throw new DistRestrictionException(
                    FMLEnvironment.dist.name(),
                    distBound.value().name()
            );
        }
        return null;
    }

}
