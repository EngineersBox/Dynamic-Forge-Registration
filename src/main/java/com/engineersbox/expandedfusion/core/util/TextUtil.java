package com.engineersbox.expandedfusion.core.util;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TextUtil {
    private static final String ENERGY_FORMAT = "%,d";

    public static IFormattableTextComponent translate(final String prefix, final String suffix, final Object... params) {
        final String key = String.format("%s.%s.%s", prefix, ExpandedFusion.MOD_ID, suffix);
        return new TranslationTextComponent(key, params);
    }

    public static IFormattableTextComponent energy(final int amount) {
        final String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energy", s1);
    }

    public static IFormattableTextComponent energyPerTick(final int amount) {
        final String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energyPerTick", s1);
    }

    public static IFormattableTextComponent energyWithMax(final int amount, final int max) {
        final String s1 = String.format(ENERGY_FORMAT, amount);
        final String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", "energyWithMax", s1, s2);
    }

    public static IFormattableTextComponent fluidWithMax(final IFluidHandler fluidHandler, final int tank) {
        final FluidStack stack = fluidHandler.getFluidInTank(tank);
        return fluidWithMax(stack, fluidHandler.getTankCapacity(tank));
    }

    public static IFormattableTextComponent fluidWithMax(final FluidStack stack, final int tankCapacity) {
        final ITextComponent fluidName = stack.getDisplayName();
        final String s1 = String.format(ENERGY_FORMAT, stack.getAmount());
        final String s2 = String.format(ENERGY_FORMAT, tankCapacity);
        return translate("misc", "fluidWithMax", fluidName, s1, s2);
    }
}
