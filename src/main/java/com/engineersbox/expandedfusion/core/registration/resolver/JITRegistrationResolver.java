package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.*;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import com.engineersbox.expandedfusion.core.event.manager.BrokerManager;
import com.engineersbox.expandedfusion.core.event.manager.DistEvent;
import com.engineersbox.expandedfusion.core.event.manager.Manager;
import com.engineersbox.expandedfusion.core.registration.contexts.ProviderModule;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.ResolverBuilderException;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.UninstantiatedElementResolver;
import com.engineersbox.expandedfusion.core.registration.handler.data.recipe.CraftingClientEventHandler;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.data.recipe.CraftingRecipeRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.data.recipe.RecipeSerializerRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.block.BlockProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.fluid.FluidProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.item.ItemProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShimModule;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Stream;

@Singleton
public class JITRegistrationResolver extends JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class.getName());

    private final Injector injector;
    private final Manager<DistEvent> brokerManager;
    private final EnumMap<ResolverType, ? super RegistrationResolver> resolvers;

    JITRegistrationResolver(final Injector injector,
                            final Manager<DistEvent> brokerManager) {
        this.injector = injector;
        this.brokerManager = brokerManager;
        this.resolvers = new EnumMap<>(ResolverType.class);
        Registration.register(this);
        this.setupEventPublishing();
    }

    private static final List<Pair<ResolverType, Class<? extends RegistrationResolver>>> resolverPairings = ImmutableList.of(
            ImmutablePair.of(ResolverType.BLOCK, BlockProviderRegistrationResolver.class),
            ImmutablePair.of(ResolverType.ITEM, ItemProviderRegistrationResolver.class),
            ImmutablePair.of(ResolverType.FLUID, FluidProviderRegistrationResolver.class),
            ImmutablePair.of(ResolverType.RECIPE_SERIALIZER, RecipeSerializerRegistrationResolver.class),
            ImmutablePair.of(ResolverType.RECIPE_INLINE_DECLARATION, CraftingRecipeRegistrationResolver.class)
    );

    @Override
    public void instantiateResolvers() {
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
                LOGGER.debug("Instantiated {}", resolverPair.getRight().getName());
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
        resolver.registerAll();
    }

    @Override
    public void registerAll() {
        // TODO: Uncomment this after other ResolverTypes have been implemented
//         Stream.of(ResolverType.values())
        Stream.of(ResolverType.BLOCK, ResolverType.ITEM, ResolverType.FLUID, ResolverType.RECIPE_SERIALIZER, ResolverType.RECIPE_INLINE_DECLARATION)
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

    private void setupEventPublishing() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishClientEvent);
        } else if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishServerEvent);
        }
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishCommonEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishGatherDataEvent);
    }

    @Override
    public void publishCommonEvent(final ModLifecycleEvent event) {
        this.brokerManager.publishEvent(DistEvent.COMMON, event);
    }

    @Override
    public void publishGatherDataEvent(final GatherDataEvent event) {
        this.brokerManager.publishEvent(DistEvent.DATA, event);
    }

    @Override
    public void publishClientEvent(final ModLifecycleEvent event) {
        this.brokerManager.publishEvent(DistEvent.CLIENT, event);
    }

    @Override
    public void publishServerEvent(final ServerLifecycleEvent event) {
        this.brokerManager.publishEvent(DistEvent.SERVER, event);
    }

    public static class Builder {

        private Logger logger;
        private String packageName;
        private String modId;

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

        public Builder withModId(final String modId) {
            this.modId = modId;
            return this;
        }

        private void addSubscriptionHandler(final EventBroker eventBroker, final Class<? extends EventSubscriptionHandler> consumer) {
            try {
                final boolean invalidClassConfiguration = ReflectionUtils.withClassModifier(Modifier.ABSTRACT)
                        .or(Class::isInterface)
                        .test(consumer);
                if (invalidClassConfiguration) {
                    throw new ResolverBuilderException("Event subscription handler must not be an interface or declared abstract");
                }
                final EventSubscriptionHandler consumerInstance = consumer.newInstance();
                eventBroker.addConsumer(consumerInstance);
            } catch (final InstantiationException | IllegalAccessException e) {
                throw new ResolverBuilderException(String.format(
                        "Could not instantiate EventSubscriptionHandler %s",
                        consumer.getName()
                ), e);
            }
        }

        @SuppressWarnings("unchecked")
        private EventBroker createBrokerAndReflectivelyAddHandlers(final Class<? extends Annotation> distAnnotation) {
            final EventBroker eventBroker = new EventBroker();
            if ((FMLEnvironment.dist == Dist.CLIENT && ServerEventHandler.class.isAssignableFrom(distAnnotation))
                || (FMLEnvironment.dist == Dist.DEDICATED_SERVER && ClientEventHandler.class.isAssignableFrom(distAnnotation))) {
                return eventBroker;
            }
            final ConfigurationBuilder configBuilder = new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(this.packageName))
                    .setScanners(
                            new TypeElementsScanner(),
                            new SubTypesScanner(),
                            new TypeAnnotationsScanner()
                    );

            Reflections reflections = new Reflections(configBuilder);
            final Stream<Class<? extends EventSubscriptionHandler>> nonInternalClasses = reflections.getTypesAnnotatedWith(distAnnotation)
                    .stream()
                    // Classes marked with @InternalEventHandler should not be used outside this lib
                    .filter((final Class<?> clazz) -> !clazz.isAnnotationPresent(InternalEventHandler.class))
                    .filter(EventSubscriptionHandler.class::isAssignableFrom)
                    .map((final Class<?> consumer) -> (Class<? extends EventSubscriptionHandler>) consumer);

            reflections = new Reflections(configBuilder.setUrls(ClasspathHelper.forPackage("com.engineersbox.expandedfusion.core")));
            final Stream<Class<? extends EventSubscriptionHandler>> internalClasses = reflections.getTypesAnnotatedWith(distAnnotation)
                    .stream()
                    .filter((final Class<?> clazz) -> clazz.isAnnotationPresent(InternalEventHandler.class))
                    .filter(EventSubscriptionHandler.class::isAssignableFrom)
                    .map((final Class<?> consumer) -> (Class<? extends EventSubscriptionHandler>) consumer);
            Stream.concat(nonInternalClasses, internalClasses).forEach((final Class<? extends EventSubscriptionHandler> consumer) -> addSubscriptionHandler(eventBroker, consumer));
            return eventBroker;
        }

        private BrokerManager<DistEvent> createBrokerManager() {
            final EventBroker dataEventBroker = createBrokerAndReflectivelyAddHandlers(DataEventHandler.class);
            addSubscriptionHandler(dataEventBroker, CraftingClientEventHandler.class);
            final BrokerManager<DistEvent> eventBrokerManager = new BrokerManager<>(DistEvent.class);
            eventBrokerManager.putMapping(
                    DistEvent.COMMON,
                    createBrokerAndReflectivelyAddHandlers(CommonEventHandler.class)
            );
            eventBrokerManager.putMapping(
                    DistEvent.CLIENT,
                    createBrokerAndReflectivelyAddHandlers(ClientEventHandler.class)
            );
            eventBrokerManager.putMapping(
                    DistEvent.SERVER,
                    createBrokerAndReflectivelyAddHandlers(ServerEventHandler.class)
            );
            eventBrokerManager.putMapping(
                    DistEvent.DATA,
                    dataEventBroker
            );

            return eventBrokerManager;
        }

        public JITRegistrationResolver build() {
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
                createBrokerManager()
            );
        }
    }
}
