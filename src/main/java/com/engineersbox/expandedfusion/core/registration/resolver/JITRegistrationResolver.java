package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.registration.contexts.ProviderModule;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.ResolverBuilderException;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.UninstantiatedElementResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.data.recipe.RecipeSerializerRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.block.BlockProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.fluid.FluidProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.item.ItemProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShimModule;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
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
            ImmutablePair.of(ResolverType.ITEM, ItemProviderRegistrationResolver.class),
            ImmutablePair.of(ResolverType.FLUID, FluidProviderRegistrationResolver.class),
            ImmutablePair.of(ResolverType.RECIPE_SERIALIZER, RecipeSerializerRegistrationResolver.class)
    );

    @Override
    public void instantiateResolvers() {
        LOGGER.debug("Instantiated BlockProviderRegistrationResolver");
        resolverPairings.forEach((final Pair<ResolverType, Class<? extends RegistrationResolver>> resolverPair) -> {
            if (this.resolvers.containsKey(resolverPair.getLeft())) {
                LOGGER.debug(
                        "{} already instantiated, skipping",
                        resolverPair.getRight().getName()
                );
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
        LOGGER.debug("Invoked registration for {} resolver", resolverType);
        final RegistrationResolver resolver = (T) this.resolvers.get(resolverType);
        if (resolver == null) {
            throw new UninstantiatedElementResolver(resolverType);
        }
        LOGGER.debug("Invoked registration for {} resolver", resolverType);
        resolver.registerAll();
    }

    @Override
    public void registerAll() {
        // TODO: Uncomment this after other ResolverTypes have been implemented
        // Stream.of(ResolverType.values())
        Stream.of(ResolverType.BLOCK, ResolverType.ITEM, ResolverType.FLUID)
                .forEach(this::registerHandledElementsOfResolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RegistrationResolver> T getRegistrationResolver(final ResolverType resolverType) {
        final T resolver = (T) this.resolvers.get(resolverType);
        if (resolver != null) {
            return resolver;
        }
        LOGGER.warn("Resolver for {} does not exist, did you forget to call JITRegistrationResolver.instantiateResolvers()?", resolverType);
        return null;
    }


    @Override
    public void publishEvent(final ModLifecycleEvent event) {
        this.eventBroker.publishEvent(event);
    }

    public static class Builder {

        private Logger logger;
        private String packageName;
        private String modId;
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

        public Builder withModId(final String modId) {
            this.modId = modId;
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
                        throw new ResolverBuilderException("Event subscription handler must be be an interface or declared abstract");
                    }
                    final EventSubscriptionHandler consumerInstance = consumer.newInstance();
                    eventBroker.addConsumer(consumerInstance);
                } catch (final InstantiationException | IllegalAccessException e) {
                    throw new ResolverBuilderException(String.format(
                            "Could not instantiate EventSubscriptionHandler %s",
                            consumer.getName()
                    ), e);
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
                        .build(),
                    new AbstractModule() {
                        @Override
                        protected void configure() {
                            if (modId != null) {
                                bind(String.class)
                                        .annotatedWith(Names.named("modId"))
                                        .toInstance(modId);
                            }
                        }
                    }
                ),
                eventBroker
            );
        }
    }
}
