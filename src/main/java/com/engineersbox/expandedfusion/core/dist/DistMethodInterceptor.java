package com.engineersbox.expandedfusion.core.dist;

import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DistMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final DistBound distBound = invocation.getMethod().getAnnotation(DistBound.class);
        if (FMLEnvironment.dist == distBound.value()) {
            return invocation.proceed();
        }
        if (distBound.throwError()) {
            throw new RuntimeException(String.format(
                    "Invoked @DistBound method for unsupported dist: %s != %s",
                    FMLEnvironment.dist.name(),
                    distBound.value().name()
            )); // TODO: Implement an exception for this
        }
        return null;
    }

}
