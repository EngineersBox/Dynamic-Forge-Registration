package com.engineersbox.expandedfusion.register.registry.annotation.processors;

import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockContainerProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockTileEntityProvider;
import com.engineersbox.expandedfusion.register.registry.provider.BlockImplType;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SupportedAnnotationTypes({
    ProviderAnnotationProcessor.BLOCK_PROVIDER_PACKAGE + "BlockProvider",
    ProviderAnnotationProcessor.BLOCK_PROVIDER_PACKAGE + "BlockContainerProvider",
    ProviderAnnotationProcessor.BLOCK_PROVIDER_PACKAGE + "BlockTileEntityProvider",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ProviderAnnotationProcessor extends AbstractProcessor {

    public static final String BLOCK_PROVIDER_PACKAGE = "com.engineersbox.expandedfusion.register.registry.annotation.block.";

    private final Map<String, Consumer<Element>> providerAnnotationHandlers = ImmutableMap.of(
        BlockProvider.class.getName(), this::processBlockProvider,
        BlockContainerProvider.class.getName(), this::processBlockContainerProvider,
        BlockTileEntityProvider.class.getName(), this::processBlockTileEntityProvider
    );

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {
        for (final TypeElement annotation : annotations) {
            final String currentAnnotationName = annotation.getSimpleName().toString();
            final Consumer<Element> handler = this.providerAnnotationHandlers.get(currentAnnotationName);
            if (handler == null) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "No such handler for annotation: " + currentAnnotationName
                );
                continue;
            }
            roundEnv.getElementsAnnotatedWith(annotation).forEach(handler);
        }
        return true;
    }

    private <T extends Element> void processBlockProvider(final T annotatedElement) {
        final BlockProvider blockProvider = annotatedElement.getAnnotation(BlockProvider.class);
    }

    private <T extends Element> void processBlockContainerProvider(final T annotatedElement) {

    }

    private <T extends Element> void processBlockTileEntityProvider(final T annotatedElement) {

    }
}
