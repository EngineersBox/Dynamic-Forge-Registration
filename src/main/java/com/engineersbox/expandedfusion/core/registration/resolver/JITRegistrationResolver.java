package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.classifier.baked.BakedInClassifierModule;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.*;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import com.engineersbox.expandedfusion.core.event.manager.BrokerManager;
import com.engineersbox.expandedfusion.core.event.manager.DistEvent;
import com.engineersbox.expandedfusion.core.event.manager.Manager;
import com.engineersbox.expandedfusion.core.reflection.InstanceMethodInjector;
import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.reflection.annotation.TargetedInjection;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.ProviderModule;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.ResolverBuilderException;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.UninstantiatedElementResolver;
import com.engineersbox.expandedfusion.core.registration.handler.data.recipe.CraftingClientEventHandler;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.data.recipe.CraftingRecipeRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.data.recipe.RecipeSerializerRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.BlockProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.FluidProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.element.ItemProviderRegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShimModule;
import com.google.common.collect.ImmutableList;
import com.google.inject.*;
import com.google.inject.name.Named;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class JITRegistrationResolver extends JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class.getName());

    private final Injector injector;
    private final Manager<DistEvent> brokerManager;
    private final EnumMap<ResolverPhase, ? super RegistrationResolver> resolvers;

    JITRegistrationResolver(final Injector injector,
                            final Manager<DistEvent> brokerManager) {
        this.injector = injector;
        this.brokerManager = brokerManager;
        this.resolvers = new EnumMap<>(ResolverPhase.class);
        Registration.register(this);
        this.setupEventPublishing();
    }

    @Inject
    @TargetedInjection
    public Set<Pair<ResolverPhase, Class<? extends RegistrationResolver>>> retrieveResolvers(@Named("packageReflections") final Reflections reflections) {
        final Set<Class<? extends RegistrationResolver>> resolverClasses = ReflectionClassFilter.filterClassesBySuperType(
                RegistrationResolver.class,
                reflections.getTypesAnnotatedWith(RegistrationPhaseHandler.class)
        );
        return resolverClasses.stream().<Pair<ResolverPhase, Class<? extends RegistrationResolver>>>map((final Class<? extends RegistrationResolver> handler) -> {
            final RegistrationPhaseHandler annotation = handler.getAnnotation(RegistrationPhaseHandler.class);
            return ImmutablePair.of(annotation.value(), handler);
        }).collect(Collectors.toSet());
    }

    @Override
    public void instantiateResolvers() {
        final Set<Pair<ResolverPhase, Class<? extends RegistrationResolver>>> resolverPairings = new InstanceMethodInjector<>(
                this,
                "retrieveResolvers"
        ).invokeMethod(this.injector);
        resolverPairings.forEach((final Pair<ResolverPhase, Class<? extends RegistrationResolver>> resolverPair) -> {
            if (this.resolvers.containsKey(resolverPair.getLeft())) {
                LOGGER.debug(
                        "Resolver of type {} already bound to class {}, skipping",
                        resolverPair.getLeft(),
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
    public <T extends RegistrationResolver> void registerHandledElementsOfResolver(final ResolverPhase resolverPhase) {
        LOGGER.debug("Invoked registration for {} resolver", resolverPhase);
        final RegistrationResolver resolver = (T) this.resolvers.get(resolverPhase);
        if (resolver == null) {
            throw new UninstantiatedElementResolver(resolverPhase);
        }
        resolver.registerAll();
    }

    @Override
    public void registerAll() {
        // TODO: Refactor to Stream.of(ResolverType.values()) after other ResolverTypes have been implemented
        Stream.of(
                ResolverPhase.BLOCK,
                ResolverPhase.ITEM,
                ResolverPhase.FLUID,
                ResolverPhase.RECIPE_SERIALIZER,
                ResolverPhase.RECIPE_INLINE_DECLARATION,
                ResolverPhase.ANONYMOUS_ELEMENT
        ).forEach(this::registerHandledElementsOfResolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RegistrationResolver> T getRegistrationResolver(final ResolverPhase resolverPhase) {
        final T resolver = (T) this.resolvers.get(resolverPhase);
        if (resolver != null) {
            return resolver;
        }
        LOGGER.warn("Resolver for {} does not exist, did you forget to call JITRegistrationResolver.instantiateResolvers() or mark your resolver with @RegistrationPhaseHandler?", resolverPhase);
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

        private void addSubscriptionHandler(final EventBroker eventBroker, final Class<? extends EventSubscriptionHandler> consumer, final Injector injector) {
            try {
                final boolean invalidClassConfiguration = ReflectionUtils.withClassModifier(Modifier.ABSTRACT)
                        .or(Class::isInterface)
                        .test(consumer);
                if (invalidClassConfiguration) {
                    throw new ResolverBuilderException("Event subscription handler must not be an interface or declared abstract");
                }
                final EventSubscriptionHandler consumerInstance = injector.getInstance(consumer);
                eventBroker.addConsumer(consumerInstance);
            } catch (final ConfigurationException | ProvisionException e) {
                throw new ResolverBuilderException(String.format(
                        "Could not instantiate EventSubscriptionHandler %s",
                        consumer.getName()
                ), e);
            }
        }

        @SuppressWarnings("unchecked")
        private EventBroker createBrokerAndReflectivelyAddHandlers(final Class<? extends Annotation> distAnnotation, final Injector injector) {
            final EventBroker eventBroker = new EventBroker();
            if ((FMLEnvironment.dist == Dist.CLIENT && ServerEventHandler.class.isAssignableFrom(distAnnotation))
                || (FMLEnvironment.dist == Dist.DEDICATED_SERVER && ClientEventHandler.class.isAssignableFrom(distAnnotation))) {
                return eventBroker;
            }
            final ConfigurationBuilder configBuilder = new ConfigurationBuilder()
                    .setUrls(this.packageName != null ? ClasspathHelper.forPackage(this.packageName) : ClasspathHelper.forJavaClassPath())
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
            Stream.concat(nonInternalClasses, internalClasses).forEach((final Class<? extends EventSubscriptionHandler> consumer) -> addSubscriptionHandler(eventBroker, consumer, injector));
            return eventBroker;
        }

        private BrokerManager<DistEvent> createBrokerManager(final Injector injector) {
            final BrokerManager<DistEvent> eventBrokerManager = new BrokerManager<>(DistEvent.class);
            eventBrokerManager.putMapping(
                    DistEvent.COMMON,
                    createBrokerAndReflectivelyAddHandlers(CommonEventHandler.class, injector)
            );
            eventBrokerManager.putMapping(
                    DistEvent.CLIENT,
                    createBrokerAndReflectivelyAddHandlers(ClientEventHandler.class, injector)
            );
            eventBrokerManager.putMapping(
                    DistEvent.SERVER,
                    createBrokerAndReflectivelyAddHandlers(ServerEventHandler.class, injector)
            );
            eventBrokerManager.putMapping(
                    DistEvent.DATA,
                    createBrokerAndReflectivelyAddHandlers(DataEventHandler.class, injector)
            );

            return eventBrokerManager;
        }

        public JITRegistrationResolver build() {
            final Injector injector = Guice.createInjector(
                    new ProviderModule(),
                    new GroupingModule(),
                    new RegistryShimModule(),
                    new BakedInClassifierModule(),
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
            );
            return new JITRegistrationResolver(
                    injector,
                    createBrokerManager(injector)
            );
        }
    }
}
