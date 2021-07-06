package com.engineersbox.expandedfusion.core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Currently just leaning on JEI for this...
 */
public final class ColorGetter {
    private static final Cache<ResourceLocation, Integer> FLUID_COLORS = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private ColorGetter() {}

    public static int getColor(final Fluid fluid) {
        try {
            return FLUID_COLORS.get(NameUtils.from(fluid), () -> getFluidColor(fluid));
        } catch (ExecutionException e) {
            // Do nothing
        }

        return getFluidColor(fluid);
    }

    private static int getFluidColor(final Fluid fluid) {
        if (fluid == Fluids.WATER) {
            return 0x0094FF;
        }
        if (ModList.get().isLoaded("jei")) {
            final TextureAtlasSprite[] sprites = ForgeHooksClient.getFluidSprites(Minecraft.getInstance().world, BlockPos.ZERO, fluid.getDefaultState());
            if (sprites.length > 0) {
                return getColor(sprites[0]);
            }
        }
        return 0xFFFFFF;
    }

    public static int getColor(final TextureAtlasSprite sprite) {
        if (ModList.get().isLoaded("jei")) {
            final List<Integer> colors = mezz.jei.color.ColorGetter.INSTANCE.getColors(sprite, 0xFFFFFF, 1);
            return colors.isEmpty() ? 0xFFFFFF : colors.get(0);
        }
        return 0xFFFFFF;
    }
}