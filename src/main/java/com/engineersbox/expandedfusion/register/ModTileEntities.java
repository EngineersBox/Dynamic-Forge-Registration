package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.common.block.IBlockProvider;
import com.engineersbox.expandedfusion.elements.block.fusionControlComputer.FusionControlComputerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntities {

    public static TileEntityType<FusionControlComputerTileEntity> FUSION_CONTROL_COMPUTER;

    private ModTileEntities() {}

    public static void registerAll(final RegistryEvent.Register<TileEntityType<?>> event) {
        FUSION_CONTROL_COMPUTER = register("fusion_control_computer", FusionControlComputerTileEntity::new, ModBlocks.FUSION_CONTROL_COMPUTER);
    }

    private static <T extends TileEntity> TileEntityType<T> register(final String name, final Supplier<T> tileFactory, final IBlockProvider block) {
        return register(name, tileFactory, block.asBlock());
    }

    private static <T extends TileEntity> TileEntityType<T> register(final String name, final Supplier<T> tileFactory, final Block... blocks) {
        final TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        return register(name, type);
    }

    private static <T extends TileEntity> TileEntityType<T> register(final String name, final TileEntityType<T> type) {
        if (type.getRegistryName() == null) {
            type.setRegistryName(ExpandedFusion.getId(name));
        }
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }

}
