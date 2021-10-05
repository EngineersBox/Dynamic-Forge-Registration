package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.classifier.baked.BakedInClassifierModule;
import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.*;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ServerEventHandler;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import com.engineersbox.expandedfusion.core.event.manager.BrokerManager;
import com.engineersbox.expandedfusion.core.event.manager.DistEvent;
import com.engineersbox.expandedfusion.core.event.manager.Manager;
import com.engineersbox.expandedfusion.core.reflection.InstanceMethodInjector;
import com.engineersbox.expandedfusion.core.reflection.PackageInspector;
import com.engineersbox.expandedfusion.core.reflection.ProxyUtils;
import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.reflection.annotation.TargetedInjection;
import com.engineersbox.expandedfusion.core.reflection.vfs.ModJarVfsUrlType;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.ProviderModule;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistrationModule;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.ResolverBuilderException;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.UninstantiatedElementResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.GroupingModule;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryServiceModule;
import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JITRegistrationResolver extends JITResolver {

    private static final Logger LOGGER = LogManager.getLogger(JITRegistrationResolver.class);
    private static final String INTERNAL_CORE_PACKAGE = "com.engineersbox.expandedfusion.core";

    private final Injector injector;
    private final Manager<DistEvent> brokerManager;
    private final EnumMap<ResolverPhase, Set<RegistrationResolver>> resolvers;

    JITRegistrationResolver(final Injector injector,
                            final Manager<DistEvent> brokerManager) {
        this.injector = injector;
        this.brokerManager = brokerManager;
        this.resolvers = new EnumMap<>(ResolverPhase.class);
        this.injector.getInstance(Registration.class).register(this);
        this.setupEventPublishing();
    }

    @Inject
    @TargetedInjection
    public Map<ResolverPhase, Set<Class<? extends RegistrationResolver>>> retrieveResolvers(@Named("packageReflections") final Reflections externalReflections) {
        final Reflections internalReflections = new Reflections(new ConfigurationBuilder()
                .setUrls(new ModJarVfsUrlType().filterOutFromURLs(ClasspathHelper.forPackage(JITRegistrationResolver.INTERNAL_CORE_PACKAGE)))
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
        Stream.of(ResolverPhase.values()).forEach(this::registerHandledElementsOfResolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RegistrationResolver> T getRegistrationResolver(final ResolverPhase resolverPhase) {
        final T resolver = (T) this.resolvers.get(resolverPhase);
        if (resolver != null) {
            return resolver;
        }
        LOGGER.warn("Resolver(s) for {} phase does not exist, did you forget to call JITRegistrationResolver.instantiateResolvers() or mark your resolver with @RegistrationPhaseHandler?", resolverPhase);
        return null;
    }

    private void setupEventPublishing() {
        configureClientEventListeners();
        configureServerEventListeners();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishBoundedCommonEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishBoundedGatherDataEvent);
        MinecraftForge.EVENT_BUS.addListener(this::publishCommonEvent);
    }

    @DistBound(Dist.CLIENT)
    private void configureClientEventListeners() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishBoundedClientEvent);
        MinecraftForge.EVENT_BUS.addListener(this::publishClientEvent);
    }

    @DistBound(Dist.DEDICATED_SERVER)
    private void configureServerEventListeners() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::publishBoundedServerEvent);
        MinecraftForge.EVENT_BUS.addListener(this::publishServerEvent);
    }

    @Override
    public <E extends Event> void publishCommonEvent(final E event) {
        this.brokerManager.publishEvent(DistEvent.COMMON, event);
    }

    @Override
    public <E extends Event> void publishGatherDataEvent(final E event) {
        this.brokerManager.publishEvent(DistEvent.DATA, event);
    }

    @DistBound(Dist.CLIENT)
    @Override
    public <E extends Event> void publishClientEvent(final E event) {
        this.brokerManager.publishEvent(DistEvent.CLIENT, event);
    }

    @DistBound(Dist.DEDICATED_SERVER)
    @Override
    public <E extends Event> void publishServerEvent(final E event) {
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

        @DistBound(Dist.DEDICATED_SERVER)
        @SafeVarargs
        private final Set<Class<? extends Annotation>> filterServerEventHandlers(final Class<? extends Annotation>... distAnnotations) {
            return Stream.of(distAnnotations)
                    .filter((final Class<? extends Annotation> distAnnotation) -> !ClientEventHandler.class.isAssignableFrom(distAnnotation))
                    .collect(Collectors.toSet());
        }

        @DistBound(Dist.CLIENT)
        @SafeVarargs
        private final Set<Class<? extends Annotation>> filterClientEventHandlers(final Class<? extends Annotation>... distAnnotations) {
            return Stream.of(distAnnotations)
                    .filter((final Class<? extends Annotation> distAnnotation) -> !ServerEventHandler.class.isAssignableFrom(distAnnotation))
                    .collect(Collectors.toSet());
        }

        @SuppressWarnings("java:S2112")
        @SafeVarargs
        private final EventBroker createBrokerAndReflectivelyAddHandlers(final Injector injector, final Class<? extends Annotation>... distAnnotations) {
            final EventBroker eventBroker = new EventBroker();
            final Set<Class<? extends Annotation>> serverHandlers = filterServerEventHandlers(distAnnotations);
            final Set<Class<? extends Annotation>> clientHandlers = filterClientEventHandlers(distAnnotations);
            final Set<Class<? extends Annotation>> filteredAnnotations = new HashSet<>();
            // Null handling here is for AspectJ @Around default null return when not in required dist
            if ((serverHandlers == null || serverHandlers.isEmpty()) && (clientHandlers == null || clientHandlers.isEmpty())) {
                return eventBroker;
            }
            if (serverHandlers != null) {
                filteredAnnotations.addAll(serverHandlers);
            }
            if (clientHandlers != null) {
                filteredAnnotations.addAll(clientHandlers);
            }
            final Set<URL> modJarVfsTypeFilteredURLs = new ModJarVfsUrlType().filterOutFromURLs(
                    this.packageName != null
                            ? ClasspathHelper.forPackage(this.packageName)
                            : ClasspathHelper.forJavaClassPath()
            );
            final ConfigurationBuilder configBuilder = new ConfigurationBuilder()
                    .setUrls(modJarVfsTypeFilteredURLs)
                    .setScanners(
                            new TypeElementsScanner(),
                            new SubTypesScanner(),
                            new TypeAnnotationsScanner()
                    );

            final Reflections externalReflections = new Reflections(configBuilder);
            final Stream<Class<? extends EventSubscriptionHandler>> nonInternalClasses = filterEventSubscriptionHandlers(
                    externalReflections,
                    filteredAnnotations,
                    (final Class<?> clazz) -> !clazz.isAnnotationPresent(InternalEventHandler.class)
            );

            modJarVfsTypeFilteredURLs.clear();
            modJarVfsTypeFilteredURLs.addAll(new ModJarVfsUrlType().filterOutFromURLs(ClasspathHelper.forPackage(JITRegistrationResolver.INTERNAL_CORE_PACKAGE)));
            final Reflections internalReflections = new Reflections(configBuilder.setUrls(modJarVfsTypeFilteredURLs));
            final Stream<Class<? extends EventSubscriptionHandler>> internalClasses = filterEventSubscriptionHandlers(
                    internalReflections,
                    filteredAnnotations,
                    (final Class<?> clazz) -> clazz.isAnnotationPresent(InternalEventHandler.class)
            );
            Stream.concat(nonInternalClasses, internalClasses).forEach((final Class<? extends EventSubscriptionHandler> consumer) -> addSubscriptionHandler(eventBroker, consumer, injector));
            return eventBroker;
        }

        @SuppressWarnings("unchecked")
        private Stream<Class<? extends EventSubscriptionHandler>> filterEventSubscriptionHandlers(final Reflections reflections,
                                                                                                  final Set<Class<? extends Annotation>> filteredAnnotations,
                                                                                                  final Predicate<Class<?>> handlerPredicate) {
            return filteredAnnotations.stream()
                    .map(reflections::getTypesAnnotatedWith)
                    .flatMap(Set::stream)
                    .filter(handlerPredicate)
                    .filter(EventSubscriptionHandler.class::isAssignableFrom)
                    .map(Class.class::cast);
        }

        private BrokerManager<DistEvent> createBrokerManager(final Injector injector) {
            final BrokerManager<DistEvent> eventBrokerManager = new BrokerManager<>(DistEvent.class);
            Stream.of(DistEvent.values()).forEach((final DistEvent distEvent) -> eventBrokerManager.putMapping(
                    distEvent,
                    createBrokerAndReflectivelyAddHandlers(injector, distEvent.getEventHandlerAnnotation())
            ));

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
                     * Class#loadClass() to avoid potentially causing issues with annotation presence and metadata
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
                LOGGER.warn("Mod ID was not provided via JITRegistrationResolver.Builder.withModId(String), attempting to determine it via reflection");
                this.modId = resolveModIdFromCallerPackage();
                if (this.modId == null) {
                    throw new ResolverBuilderException("Mod ID was not provided and could not be determined reflectively. Please provide it with JITRegistrationResolver.Builder.withModId(String)");
                }
            }
            final Injector injector = Guice.createInjector(
                    new RegistrationModule(modId),
                    new ProviderModule(),
                    new GroupingModule(),
                    new RegistryServiceModule(),
                    new BakedInClassifierModule(),
                    new PackageReflectionsModule()
                            .withPackageName(this.packageName)
                            .build(),
                    new AbstractModule() {
                        @Override
                        protected void configure() {
                            bind(String.class)
                                    .annotatedWith(Names.named("modId"))
                                    .toInstance(modId);
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
