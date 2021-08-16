package com.engineersbox.expandedfusion.core.registration.provider.grouping.block;

import com.engineersbox.expandedfusion.core.registration.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.block.ScreenProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.block.TileEntityProvider;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateBlockComponentBinding;
import com.engineersbox.expandedfusion.core.registration.exception.MisconfiguredProviderException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockImplClassGrouping extends ImplClassGroupings<BlockImplGrouping> {

    private final Reflections reflections;

    @Inject
    public BlockImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends Block>> blockProviderAnnotatedClasses = super.filterClassesBySuperType(
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
        final Set<Class<? extends TileEntity>> tileEntityProviderAnnotatedClasses = super.filterClassesBySuperType(
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
        final Set<Class<? extends Container>> containerProviderAnnotatedClasses = super.filterClassesBySuperType(
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
        final Set<Class<? extends ContainerScreen>> screenProviderAnnotatedClasses = super.filterClassesBySuperType(
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
        final Map<String, List<Class<? extends Annotation>>> missing = new HashMap<>();
        classGroupings.forEach((String name, BlockImplGrouping group) -> {
            List<Class<? extends Annotation>> requirements = group.hasRequirements(group.getBlockProviderAnnotation().type());
            if (requirements.size() > 0) {
                missing.put(name, requirements);
            }
        });
        if (!missing.isEmpty()) {
            throw new MisconfiguredProviderException(missing);
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
        } else if (ContainerScreen.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setScreen((Class<? extends ContainerScreen<? extends Container>>) toAdd);
        }
        this.classGroupings.put(name, blockImplGrouping);
    }
}
