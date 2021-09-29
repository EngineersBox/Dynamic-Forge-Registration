package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block;

import com.engineersbox.expandedfusion.core.classifier.ImplementationClassifier;
import com.engineersbox.expandedfusion.core.classifier.MultiClassImplementationClassifier;
import com.engineersbox.expandedfusion.core.classifier.exception.GroupingClassificationException;
import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;
import com.engineersbox.expandedfusion.core.registration.exception.MisconfiguredProviderException;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.block.DuplicateBlockComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.reflections.Reflections;

import java.util.Set;

public class BlockImplClassGrouping extends ImplClassGroupings<BlockImplGrouping> implements IImplClassGroupings<BlockImplGrouping> {

    private final Reflections reflections;
    final MultiClassImplementationClassifier<BlockImplGrouping> classifier;

    @Inject
    public BlockImplClassGrouping(@Named("packageReflections") final Reflections reflections,
                                  final ImplementationClassifier<BlockImplGrouping> classifier) {
        this.reflections = reflections;
        this.classifier = (MultiClassImplementationClassifier<BlockImplGrouping>) classifier;
    }

    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends Block>> blockProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                Block.class,
                this.reflections.getTypesAnnotatedWith(BlockProvider.class)
        );
        for (final Class<? extends Block> c : blockProviderAnnotatedClasses) {
            final BlockProvider annotation = c.getAnnotation(BlockProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
        final Set<Class<? extends TileEntity>> tileEntityProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                TileEntity.class,
                this.reflections.getTypesAnnotatedWith(TileEntityProvider.class)
        );
        for (final Class<? extends TileEntity> c : tileEntityProviderAnnotatedClasses) {
            final TileEntityProvider annotation = c.getAnnotation(TileEntityProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
        final Set<Class<? extends Container>> containerProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                Container.class,
                this.reflections.getTypesAnnotatedWith(ContainerProvider.class)
        );
        for (final Class<? extends Container> c : containerProviderAnnotatedClasses) {
            final ContainerProvider annotation = c.getAnnotation(ContainerProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
        addDistDependentProviders();
        try {
            classGroupings.values().forEach((final BlockImplGrouping group) -> {
                final String classification = this.classifier.testGrouping(group);
                group.setClassification(BlockImplType.valueOf(classification));
            });
        } catch (final GroupingClassificationException e) {
            throw new MisconfiguredProviderException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    @DistBound(Dist.CLIENT)
    private void addDistDependentProviders() {
        final Set<Class<? extends ContainerScreen>> screenProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                ContainerScreen.class,
                this.reflections.getTypesAnnotatedWith(ScreenProvider.class)
        );
        for (final Class<? extends ContainerScreen> c : screenProviderAnnotatedClasses) {
            final ScreenProvider annotation = c.getAnnotation(ScreenProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
        final Set<Class<? extends TileEntityRenderer>> rendererProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                TileEntityRenderer.class,
                this.reflections.getTypesAnnotatedWith(RendererProvider.class)
        );
        for (final Class<? extends TileEntityRenderer> c : rendererProviderAnnotatedClasses) {
            final RendererProvider annotation = c.getAnnotation(RendererProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIfNotExists(final String name, final Class<?> toAdd) throws DuplicateBlockComponentBinding {
        BlockImplGrouping blockImplGrouping = this.classGroupings.get(name);
        if (blockImplGrouping == null) {
            blockImplGrouping = new BlockImplGrouping();
        }
        if (Block.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setBlock((Class<? extends Block>) toAdd);
        } else if (TileEntity.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setTileEntity((Class<? extends TileEntity>) toAdd);
        } else if (Container.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setContainer((Class<? extends Container>) toAdd);
        } else if (FMLEnvironment.dist == Dist.CLIENT) {
            if (ContainerScreen.class.isAssignableFrom(toAdd)) {
                blockImplGrouping.setScreen((Class<? extends ContainerScreen<? extends Container>>) toAdd);
            } else if (TileEntityRenderer.class.isAssignableFrom(toAdd)) {
                blockImplGrouping.setRenderer((Class<? extends TileEntityRenderer<? extends TileEntity>>) toAdd);
            }
        }
        this.classGroupings.put(name, blockImplGrouping);
    }
}
