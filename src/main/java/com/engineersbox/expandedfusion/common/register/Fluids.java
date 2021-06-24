package com.engineersbox.expandedfusion.common.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Fluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ExpandedFusion.MODID);

    public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/brown_mushroom_block");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/mushroom_stem");

    public static final RegistryObject<FlowingFluid> SUPERCRITICAL_HELIUM = FLUIDS.register("supercritical_helium", () -> new ForgeFlowingFluid.Source(makeProperties()));
    public static final RegistryObject<FlowingFluid> SUPERCRITICAL_HELIUM_FLOWING = FLUIDS.register("supercritical_helium_flowing", () -> new ForgeFlowingFluid.Flowing(makeProperties()));
    public static RegistryObject<FlowingFluidBlock> SUPERCRITICAL_HELIUM_BLOCK = Blocks.BLOCKS.register("supercritical_helium_block", () ->
            new FlowingFluidBlock(
                SUPERCRITICAL_HELIUM,
                Block.Properties.create(Material.WATER)
                    .doesNotBlockMovement()
                    .hardnessAndResistance(100.0F)
                    .noDrops()
            )
    );
    public static RegistryObject<Item> SUPERCRITICAL_HELIUM_BUCKET = Items.ITEMS.register("supercritical_helium_bucket", () ->
            new BucketItem(
                SUPERCRITICAL_HELIUM,
                new Item.Properties()
                    .containerItem(net.minecraft.item.Items.BUCKET)
                    .maxStackSize(1)
                    .group(ItemGroup.MISC)
            )
    );

    private static ForgeFlowingFluid.Properties makeProperties() {
        return new ForgeFlowingFluid.Properties(
                SUPERCRITICAL_HELIUM,
                SUPERCRITICAL_HELIUM_FLOWING,
                FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).color(0x3F1080FF)
        ).bucket(SUPERCRITICAL_HELIUM_BUCKET).block(SUPERCRITICAL_HELIUM_BLOCK);
    }

}
