package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.classifier.baked.BakedInClassifierModule;
import com.engineersbox.expandedfusion.core.dist.DistInterceptorModule;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.*;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import com.engineersbox.expandedfusion.core.event.manager.BrokerManager;
import com.engineersbox.expandedfusion.core.event.manager.DistEvent;
import com.engineersbox.expandedfusion.core.event.manager.Manager;
import com.engineersbox.expandedfusion.core.reflection.InstanceMethodInjector;
import com.engineersbox.expandedfusion.core.reflection.PackageInspector;
import com.engineersbox.expandedfusion.core.reflection.ProxyUtils;
import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.reflection.annotation.TargetedInjection;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.ProviderModule;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.ResolverBuilderException;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.UninstantiatedElementResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShimModule;
import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
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
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JITRegistrationResolver extends JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class.getName());
    private static final String INTERNAL_CORE_PACKAGE = "com.engineersbox.expandedfusion.core";

    private final Injector injector;
    private final Manager<DistEvent> brokerManager;
    private final EnumMap<ResolverPhase, Set<RegistrationResolver>> resolvers;

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
    public Map<ResolverPhase, Set<Class<? extends RegistrationResolver>>> retrieveResolvers(@Named("packageReflections") final Reflections externalReflections) {
        final Reflections internalReflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(JITRegistrationResolver.INTERNAL_CORE_PACKAGE))
                .setScanners(
                        new TypeElementsScanner(),
                        new SubTypesScanner(),
                        new TypeAnnotationsScanner()
                )
        );
        final Set<Class<? extends RegistrationResolver>> internalResolverClasses = ReflectionClassFilter.filterClassesBySuperType(
                RegistrationResolver.class,
                internalReflections.getTypesAnnotatedWith(RegistrationPhaseHandler.class)
        );
        LOGGER.debug("Found {} internal resolvers", internalResolverClasses.size());
        final Set<Class<? extends RegistrationResolver>> externalResolverClasses = ReflectionClassFilter.filterClassesBySuperType(
                RegistrationResolver.class,
                externalReflections.getTypesAnnotatedWith(RegistrationPhaseHandler.class)
        );
        LOGGER.debug("Found {} external resolvers", externalResolverClasses.size());
        final Map<ResolverPhase, Set<Class<? extends RegistrationResolver>>> phaseResolvers = new EnumMap<>(ResolverPhase.class);
        Stream.concat(
                internalResolverClasses.stream(),
                externalResolverClasses.stream()
        ).forEach((final Class<? extends RegistrationResolver> handler) -> {
            final RegistrationPhaseHandler annotation = handler.getAnnotation(RegistrationPhaseHandler.class);
            Set<Class<? extends RegistrationResolver>> phaseMapping = phaseResolvers.get(annotation.value());
            if (phaseMapping == null) {
                phaseMapping = new HashSet<>();
            }
            phaseMapping.add(handler);
            phaseResolvers.put(annotation.value(), phaseMapping);
        });
        return phaseResolvers;
    }

    @Override
    public void instantiateResolvers() {
        final Map<ResolverPhase, Set<Class<? extends RegistrationResolver>>> resolverPairings = new InstanceMethodInjector<>(
                this,
                "retrieveResolvers"
        ).invokeMethod(this.injector);
        resolverPairings.forEach((final ResolverPhase phase, Set<Class<? extends RegistrationResolver>> handlers) -> {
            Set<RegistrationResolver> phaseMapping = this.resolvers.get(phase);
            if (phaseMapping == null) {
                phaseMapping = new HashSet<>();
            }
            for (final Class<? extends RegistrationResolver> handler : handlers) {
                phaseMapping.add(this.injector.getInstance(handler));
                LOGGER.debug("[Phase: {}] Instantiated resolver {}", phase.name(), handler.getName());
            }
            this.resolvers.put(phase, phaseMapping);
        });
    }

    @Override
    public void registerHandledElementsOfResolver(final ResolverPhase resolverPhase) {
        LOGGER.debug("Invoked registration for {} resolver", resolverPhase);
        final Set<? extends RegistrationResolver> handlers = this.resolvers.get(resolverPhase);
        if (handlers == null) {
            throw new UninstantiatedElementResolver(resolverPhase);
        }
        handlers.forEach(RegistrationResolver::registerAll);
    }

    @Override
    public void registerAll() {
        Stream.of(
                ResolverPhase.TAGS,
                ResolverPhase.ANONYMOUS_ELEMENT,
                ResolverPhase.BLOCK,
                ResolverPhase.ITEM,
                ResolverPhase.FLUID,
                ResolverPhase.RECIPE_SERIALIZER,
                ResolverPhase.RECIPE_INLINE_DECLARATION
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

        private String packageName;
        private String modId;

        public Builder() {
            this.modId = resolveModIdFromCallerPackage();
            final List<Class<?>> topLevelClasses = PackageInspector.getCallerTopLevelClasses(this.getClass().getPackage().getName());
            if (topLevelClasses.isEmpty()) {
                this.packageName = PackageInspector.getCallerPackageName(this.getClass().getPackage().getName());
            } else {
                this.packageName = topLevelClasses.get(0).getPackage().getName();
            }
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

            reflections = new Reflections(configBuilder.setUrls(ClasspathHelper.forPackage(JITRegistrationResolver.INTERNAL_CORE_PACKAGE)));
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

        @SuppressWarnings("java:S1872")
        private String resolveModIdFromCallerPackage() {
            final List<Class<?>> topLevelClasses = PackageInspector.getCallerTopLevelClasses(this.getClass().getPackage().getName());
            if (topLevelClasses.isEmpty()) {
                return null;
            }
            final List<String> availableModIds = topLevelClasses.stream()
                    // Use of class name comparison here is deliberate as these are proxies, not actual classes
                    .map((final Class<?> clazz) -> Stream.of(clazz.getDeclaredAnnotations()).filter((final Annotation annotation) ->
                            Mod.class.getName().equals(annotation.annotationType().getName())).findFirst())
                    .filter(Optional::isPresent)
                    /* The classes in topLevelClasses are actually loaded as proxies at runtime with
                     * Class$loadClass() to avoid potentially causing issues with annotation presence and metadata
                     * retention with the internal GenericDeclaration class used to invoke
                     * GenericDeclaration.super.isAnnotationPresent(Class<?>). As such the class metadata is
                     * wrapped with java.lang.reflect.Proxy, so we need to reflectively unwrap this in order to make
                     * the underlying annotations available.
                     *
                     * The reason we don't just cast the result of Proxy.getInvocationHandler(annotation) to
                     * AnnotationInvocationHandler is that it has different access modifiers depending on which
                     * distribution of JDK is used. For example, in Oracle OpenJDK is has public access, but in
                     * AdoptOpenJDK is has package private access.
                     */
                    .map((final Optional<Annotation> annotation) -> ProxyUtils.getProxiedAnnotationValue(annotation.get(), "value", String.class))
                    .distinct()
                    .collect(Collectors.toList());
            if (availableModIds.isEmpty()) {
                return null;
            } else if (availableModIds.size() > 1) {
                LOGGER.warn("Multiple @Mod annotations present in package [{}], defaulting to first available", String.join(", ", availableModIds));
            }
            LOGGER.debug("Resolved mod ID as: {}", availableModIds.get(0));
            return availableModIds.get(0);
        }

        public JITRegistrationResolver build() {
            if (this.modId == null) {
                LOGGER.debug("Mod ID was not provided via JITRegistrationResolver.Builder.withModId(String), attempting to determine it via reflection");
                this.modId = resolveModIdFromCallerPackage();
                if (this.modId == null) {
                    throw new ResolverBuilderException("Mod ID was not provided and could not reflectively determine a mod ID. Please provide it with JITRegistrationResolver.Builder.withModId(String)");
                }
            }
            Registration.setModId(modId);
            final Injector injector = Guice.createInjector(
                    new ProviderModule(),
                    new GroupingModule(),
                    new RegistryShimModule(),
                    new BakedInClassifierModule(),
                    new DistInterceptorModule(),
                    new PackageReflectionsModule()
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
