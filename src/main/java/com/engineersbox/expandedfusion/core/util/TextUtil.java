package com.engineersbox.expandedfusion.core.util;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TextUtil {
    private static final String ENERGY_FORMAT = "%,d";

    public static IFormattableTextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, ExpandedFusion.MOD_ID, suffix);
        return new TranslationTextComponent(key, params);
    }

    public static IFormattableTextComponent energy(int amount) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energy", s1);
    }

    public static IFormattableTextComponent energyPerTick(int amount) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energyPerTick", s1);
    }

    public static IFormattableTextComponent energyWithMax(int amount, int max) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", "energyWithMax", s1, s2);
    }

    public static IFormattableTextComponent fluidWithMax(IFluidHandler fluidHandler, int tank) {
        FluidStack stack = fluidHandler.getFluidInTank(tank);
        return fluidWithMax(stack, fluidHandler.getTankCapacity(tank));
    }

    public static IFormattableTextComponent fluidWithMax(FluidStack stack, int tankCapacity) {
        ITextComponent fluidName = stack.getDisplayName();
        String s1 = String.format(ENERGY_FORMAT, stack.getAmount());
        String s2 = String.format(ENERGY_FORMAT, tankCapacity);
        return translate("misc", "fluidWithMax", fluidName, s1, s2);
    }
}
