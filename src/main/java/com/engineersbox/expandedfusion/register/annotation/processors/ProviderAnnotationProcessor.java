package com.engineersbox.expandedfusion.register.annotation.processors;

import com.engineersbox.expandedfusion.register.annotation.block.ContainerProvider;
import com.engineersbox.expandedfusion.register.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.annotation.block.TileEntityProvider;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@SupportedAnnotationTypes({
    ProviderAnnotationProcessor.BLOCK_PROVIDER_PACKAGE + "BlockProvider",
    ProviderAnnotationProcessor.BLOCK_PROVIDER_PACKAGE + "BlockContainerProvider",
    ProviderAnnotationProcessor.BLOCK_PROVIDER_PACKAGE + "BlockTileEntityProvider",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ProviderAnnotationProcessor extends AbstractProcessor {

    public static final String BLOCK_PROVIDER_PACKAGE = "com.engineersbox.expandedfusion.register.annotation.block.";

    private final Map<String, Consumer<Element>> providerAnnotationHandlers = ImmutableMap.of(
        BlockProvider.class.getName(), this::processBlockProvider,
        ContainerProvider.class.getName(), this::processBlockContainerProvider,
        TileEntityProvider.class.getName(), this::processBlockTileEntityProvider
    );

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
                           final RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.WARNING,
                "Just a test print"
        );
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
        switch (blockProvider.type()) {
            case BASE:
                this.handleBaseBlockProvider(annotatedElement, blockProvider);
                return;
            case TILE_ENTITY:
                this.handleTileEntityBlockProvider(annotatedElement);
        }
    }

    private <T extends Element> void handleBaseBlockProvider(final T annotatedElement, final BlockProvider blockProvider) {
        if (this.isSuperTypeOf((TypeElement) annotatedElement, Block.class)) {
            if (blockProvider.properties().length > 0) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Use of @BlockProvider annotation with BlockImplType.BASE extending net.minecraft.block.Block and providing 1 or more @BaseBlockProperties will be registered with provided properties and conflict with properties provided in constructor via super(...) call"
                );
            }
            return;
        }
        if (blockProvider.properties().length < 1) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Use of @BlockProvider annotation with BlockImplType.BASE either extend net.minecraft.block.Block or provide 1 or more @BaseBlockProperties via the properties field"
            );
        }
    }

    private <T extends Element> void handleTileEntityBlockProvider(final T annotatedElement) {
        if (!this.isSuperTypeOf((TypeElement) annotatedElement,Block.class)) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Use of @BlockProvider annotation with BlockImplType.TILE_ENTITY must be on class extending net.minecraft.block.Block"
            );
        }
    }

    private <T extends Element> void processBlockContainerProvider(final T annotatedElement) {

    }

    private <T extends Element> void processBlockTileEntityProvider(final T annotatedElement) {

    }

    private boolean isSuperTypeOf(final TypeElement element, final Class<?> superType) {
        final TypeMirror mirror = element.getSuperclass();
        String superClassName = ((DeclaredType) mirror).asElement().getSimpleName().toString();
        return superClassName.equals(superType.getTypeName());
    }
}
