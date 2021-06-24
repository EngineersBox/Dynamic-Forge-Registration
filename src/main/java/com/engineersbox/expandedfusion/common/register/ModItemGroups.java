package com.engineersbox.expandedfusion.common.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ModItemGroups {
    public static final ItemGroup CREATIVE_TAB_ITEM_GROUP = new ModItemGroup(ExpandedFusion.MODID, () -> new ItemStack(Blocks.NOBIUM_TITANIUM_COIL.get()));

    public static final class ModItemGroup extends ItemGroup {

        @Nonnull
        private final Supplier<ItemStack> iconSupplier;

        public ModItemGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier) {
            super(name);
            this.iconSupplier = iconSupplier;
        }

        @Override
        @Nonnull
        public ItemStack createIcon() {
            return iconSupplier.get();
        }

    }
}
