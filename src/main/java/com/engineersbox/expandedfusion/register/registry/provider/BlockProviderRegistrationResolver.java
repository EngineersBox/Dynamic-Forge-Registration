package com.engineersbox.expandedfusion.register.registry.provider;

import com.engineersbox.expandedfusion.register.registry.BlockRegistryObject;
import com.engineersbox.expandedfusion.register.registry.annotation.BlockProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.ContainerProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.TileEntityProvider;
import com.engineersbox.expandedfusion.register.registry.exception.DuplicateBlockComponentBinding;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockProviderRegistrationResolver extends RegistrationResolver {

    static class BlockImplGrouping {
        private Class<? extends TileEntity> tileEntity;
        private Class<? extends Block> block;
        private Class<? extends Container> container;

        public Class<? extends TileEntity> getTileEntity() {
            return tileEntity;
        }

        public void setTileEntity(final Class<? extends TileEntity> tileEntity) throws DuplicateBlockComponentBinding {
            if (this.tileEntity != null) {
                throw new DuplicateBlockComponentBinding(this.tileEntity, tileEntity);
            }
            this.tileEntity = tileEntity;
        }

        public Class<? extends Block> getBlock() {
            return block;
        }

        public void setBlock(final Class<? extends Block> block) throws DuplicateBlockComponentBinding {
            if (this.block != null) {
                throw new DuplicateBlockComponentBinding(this.block, block);
            }
            this.block = block;
        }

        public Class<? extends Container> getContainer() {
            return container;
        }

        public void setContainer(final Class<? extends Container> container) throws DuplicateBlockComponentBinding {
            if (this.container != null) {
                throw new DuplicateBlockComponentBinding(this.container, container);
            }
            this.container = container;
        }
    }

    private final Reflections reflections;
    private final Map<String, BlockImplGrouping> blockImplClassGroupings;

    private static final Map<String, BlockRegistryObject<?>> blockRegistry = new HashMap<>();

    @Inject
    public BlockProviderRegistrationResolver(@Named("packageReflections") final Reflections reflections) throws DuplicateBlockComponentBinding {
        this.reflections = reflections;
        this.blockImplClassGroupings = collectAnnotatedBlockResources();
    }

    private Map<String, BlockImplGrouping> collectAnnotatedBlockResources() throws DuplicateBlockComponentBinding {
        final Map<String, BlockImplGrouping> classGroupings = new HashMap<>();
        final Set<Class<? extends Block>> blockProviderAnnotatedClasses = super.filterClassesBySuperType(
                Block.class,
                this.reflections.getTypesAnnotatedWith(BlockProvider.class)
        );
        for (final Class<? extends Block> c : blockProviderAnnotatedClasses) {
            final BlockProvider annotation = c.getAnnotation(BlockProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(classGroupings, annotation.name(), c);
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
            addIfNotExists(classGroupings, annotation.name(), c);
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
            addIfNotExists(classGroupings, annotation.name(), c);
        }
        return classGroupings;
    }

    @SuppressWarnings("unchecked")
    private void addIfNotExists(final Map<String, BlockImplGrouping> groupings, final String name, final Class<?> toAdd) throws DuplicateBlockComponentBinding {
        BlockImplGrouping blockImplGrouping = groupings.get(name);
        if (blockImplGrouping == null) {
            blockImplGrouping = new BlockImplGrouping();
        }
        if (Block.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setBlock((Class<? extends Block>) toAdd);
        } else if (TileEntity.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setTileEntity((Class<? extends TileEntity>) toAdd);
        } else if (Container.class.isAssignableFrom(toAdd)) {
            blockImplGrouping.setContainer((Class<? extends Container>) toAdd);
        }
        groupings.put(name, blockImplGrouping);
    }

    private void registerProviderAnnotatedBlock(final String blockName, final BlockImplGrouping annotatedClasses) {

    }
}
