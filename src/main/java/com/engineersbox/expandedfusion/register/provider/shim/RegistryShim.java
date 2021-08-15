package com.engineersbox.expandedfusion.register.provider.shim;

import com.engineersbox.expandedfusion.register.registry.RegistryObjectWrapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class RegistryShim<T> {//<T extends IForgeRegistryEntry<? super T>, E extends RegistryObjectWrapper<? extends T>> {

    public String modID;

    public ResourceLocation getId(String path) {
        return new ResourceLocation(this.modID, path);
    }

//    public abstract E register(final String name, final Supplier<? extends T> entry);

//    public abstract E register(final String name, final Supplier<? extends T> entry, final Function<E, Supplier<? extends Item>> item);
}
