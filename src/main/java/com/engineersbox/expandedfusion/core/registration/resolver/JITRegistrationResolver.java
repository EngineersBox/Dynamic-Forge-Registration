package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.registration.contexts.ProviderModule;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.elements.block.BlockProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.elements.item.ItemProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShimModule;
import com.engineersbox.expandedfusion.core.registration.resolver.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.registration.resolver.event.broker.EventBroker;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

@Singleton
public class JITRegistrationResolver extends JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class.getName());

    private final Injector injector;
    private final EventBroker eventBroker;
    private final EnumMap<ResolverType, ? super RegistrationResolver> resolvers;

    JITRegistrationResolver(final Injector injector,
                            final EventBroker eventBroker) {
        this.injector = injector;
        this.eventBroker = eventBroker;
        this.resolvers = new EnumMap<>(ResolverType.class);
    }

    private static final List<Pair<ResolverType, Class<? extends RegistrationResolver>>> resolverPairings = ImmutableList.of(
            ImmutablePair.of(ResolverType.BLOCK, BlockProviderRegistrationResolver.class),
            ImmutablePair.of(ResolverType.ITEM, ItemProviderRegistrationResolver.class)
//            ImmutablePair.of(ResolverType.FLUID, FluidProviderRegistrationResolver.class),
//            ImmutablePair.of(ResolverType.RECIPE, RecipeProviderRegistrationResolver.class)
    );

    @Override
    public void instantiateResolvers() {
        LOGGER.debug("Instantiated BlockProviderRegistrationResolver");
        resolverPairings.forEach((final Pair<ResolverType, Class<? extends RegistrationResolver>> resolverPair) -> {
            if (this.resolvers.containsKey(resolverPair.getLeft())) {
                LOGGER.debug(String.format(
                        "%s already instantiated, skipping",
                        resolverPair.getRight().getName()
                ));
            } else {
                this.resolvers.put(
                        resolverPair.getLeft(),
                        this.injector.getInstance(resolverPair.getRight())
                );
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RegistrationResolver> void registerHandledElementsOfResolver(final ResolverType resolverType) {
        LOGGER.debug(String.format(
                "Invoked registration for %s resolver",
                resolverType
        ));
        final RegistrationResolver resolver = (T) this.resolvers.get(resolverType);
        if (resolver == null) {
            throw new RuntimeException(String.format(
                    "Attempted to register elements of resolver that has not been instantiated: %s",
                    resolverType
            )); // TODO: Implement and exception for this
        }
        LOGGER.debug(String.format(
                "Invoked registration for %s resolver",
                resolverType
        ));
        resolver.registerAll();
    }

    @Override
    public void registerAll() {
        // TODO: Uncomment this after other ResolverTypes have been implemented
        // Stream.of(ResolverType.values())
        Stream.of(ResolverType.BLOCK, ResolverType.ITEM)
                .forEach(this::registerHandledElementsOfResolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RegistrationResolver> T getRegistrationResolver(final ResolverType resolverType) {
        final T resolver = (T) this.resolvers.get(resolverType);
        if (resolver != null) {
            return resolver;
        }
        LOGGER.warn(String.format(
                "Resolver for %s does not exist, did you forget to call JITRegistrationResolver.instantiateResolvers()?",
                resolverType
        ));
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

        @SafeVarargs
        public final Builder withEventHandlers(final Class<? extends EventSubscriptionHandler> ...eventConsumers) {
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
                    new RegistryShimModule(),
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
