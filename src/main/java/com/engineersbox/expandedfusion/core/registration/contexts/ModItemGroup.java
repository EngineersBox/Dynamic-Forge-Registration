package com.engineersbox.expandedfusion.core.registration.contexts;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public final class ModItemGroup extends ItemGroup {

    @Nonnull
    private final Supplier<ItemStack> iconSupplier;
    private final TranslationTextComponent nameMapping;

    public ModItemGroup(@Nonnull final String name,
                        @Nonnull final String nameMapping,
                        @Nonnull final Supplier<ItemStack> iconSupplier) {
        super(name);
        this.nameMapping = new TranslationTextComponent(nameMapping);
        this.iconSupplier = iconSupplier;
    }

    @Override
    public ITextComponent getGroupName() {
        return this.nameMapping;
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return iconSupplier.get();
    }

}
