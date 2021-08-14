package com.engineersbox.expandedfusion.register.resolver;

import com.engineersbox.expandedfusion.register.contexts.ProviderModule;
import com.engineersbox.expandedfusion.register.handler.BlockClientEventHandler;
import com.engineersbox.expandedfusion.register.handler.ItemClientEventHandler;
import com.engineersbox.expandedfusion.register.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.register.provider.block.BlockProviderRegistrationResolver;
import com.engineersbox.expandedfusion.register.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.register.resolver.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.register.resolver.event.broker.EventBroker;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;

@Singleton
public class JITRegistrationResolver implements JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class.getName());

    private final Injector injector;
    private final EventBroker eventBroker;
    private BlockProviderRegistrationResolver blockProviderResolver;

    JITRegistrationResolver(final Injector injector,
                            final EventBroker eventBroker) {
        this.injector = injector;
        this.eventBroker = eventBroker;
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

    @Override
    public void publishEvent(final ModLifecycleEvent event) {
        this.eventBroker.publishEvent(event);
    }

    public static class Builder {

        private Logger logger;
        private String packageName;
        private Set<Class<? extends EventSubscriptionHandler>> subscriptionHandlers;

        public Builder() {
            this.packageName = PackageReflectionsModule.getCallerPackageName();
            this.subscriptionHandlers = new HashSet<>();
        }

        public Builder withLogger(final Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder withPackageName(final String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder withDefaultEventHandlers() {
            Collections.addAll(
                this.subscriptionHandlers,
                BlockClientEventHandler.class
            );
            return this;
        }

        @SafeVarargs
        public final <T extends EventSubscriptionHandler> Builder withCustomEventHandlers(final Class<T> ...eventConsumers) {
            Collections.addAll(this.subscriptionHandlers, eventConsumers);
            return this;
        }

        public JITRegistrationResolver build() {
            final EventBroker eventBroker = new EventBroker();
            this.subscriptionHandlers.forEach((final Class<? extends EventSubscriptionHandler> consumer) -> {
                try {
                    final boolean invalidClassConfiguration = ReflectionUtils.withClassModifier(Modifier.ABSTRACT)
                            .or(Class::isInterface)
                            .test(consumer);
                    if (invalidClassConfiguration) {
                        throw new RuntimeException("Event subscription handler must be be an interface or declared abstract"); // TODO: Implement this exception
                    }
                    final EventSubscriptionHandler consumerInstance = consumer.newInstance();
                    eventBroker.addConsumer(consumerInstance);
                } catch (final InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e); // TODO: Implement an exception for this
                }
            });
            return new JITRegistrationResolver(
                Guice.createInjector(
                    new ProviderModule(),
                    new GroupingModule(),
                    new PackageReflectionsModule()
                        .withLogger(this.logger)
                        .withPackageName(this.packageName)
                        .build()
                ),
                eventBroker
            );
        }
    }
}
