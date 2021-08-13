package com.engineersbox.expandedfusion.register.resolver;

import com.engineersbox.expandedfusion.register.contexts.ProviderModule;
import com.engineersbox.expandedfusion.register.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.register.provider.block.BlockProviderRegistrationResolver;
import com.engineersbox.expandedfusion.register.provider.grouping.GroupingModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JITRegistrationResolver implements JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class.getName());

    private final Injector injector;
    private BlockProviderRegistrationResolver blockProviderResolver;

    JITRegistrationResolver(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public void instantiateResolvers() {
        LOGGER.debug("Instantiated BlockProviderRegistrationResolver");
        this.blockProviderResolver = this.injector.getInstance(BlockProviderRegistrationResolver.class);
    }

    @Override
    public void registerBlocks() {
        LOGGER.debug("Invoked registration for Blocks and associated TileEntity, Container and Screen declarations");
        this.blockProviderResolver.registerAll();
    }

    @Override
    public void registerItems() {}

    @Override
    public void registerFluids() {}

    @Override
    public void registerAll() {
        this.registerBlocks();
        this.registerItems();
        this.registerFluids();
    }

    @Override
    public RegistrationResolver getBlockRegistrationResolver() {
        return this.blockProviderResolver;
    }

    @Override
    public RegistrationResolver getItemRegistrationResolver() {
        return null;
    }

    @Override
    public RegistrationResolver getFluidRegistrationResolver() {
        return null;
    }

    public static class Builder {

        private Logger logger;
        private String packageName;

        public Builder() {
            this.packageName = PackageReflectionsModule.getCallerPackageName();
        }

        public Builder withLogger(final Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder withPackageName(final String packageName) {
            this.packageName = packageName;
            return this;
        }

        public JITRegistrationResolver build() {
            return new JITRegistrationResolver(
                Guice.createInjector(
                    new ProviderModule(),
                    new GroupingModule(),
                    new PackageReflectionsModule()
                        .withLogger(this.logger)
                        .withPackageName(this.packageName)
                        .build()
                )
            );
        }
    }
}
