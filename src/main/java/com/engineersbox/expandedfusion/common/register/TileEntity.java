package com.engineersbox.expandedfusion.common.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntity {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ExpandedFusion.MODID);

}
