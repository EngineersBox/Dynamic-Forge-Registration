package com.engineersbox.expandedfusion.core.registration.provider.shim;

import net.minecraft.util.ResourceLocation;

public abstract class RegistryShim<T> {//<T extends IForgeRegistryEntry<? super T>, E extends RegistryObjectWrapper<? extends T>> {

    public String modID;

    public ResourceLocation getId(String path) {
        return new ResourceLocation(this.modID, path);
    }

//    public abstract E register(final String name, final Supplier<? extends T> entry);

//    public abstract E register(final String name, final Supplier<? extends T> entry, final Function<E, Supplier<? extends Item>> item);
}
