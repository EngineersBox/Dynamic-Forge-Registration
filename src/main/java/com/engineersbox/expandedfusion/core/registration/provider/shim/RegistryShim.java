package com.engineersbox.expandedfusion.core.registration.provider.shim;

import net.minecraft.util.ResourceLocation;

public abstract class RegistryShim<T> {

    public String modID;

    public ResourceLocation getId(String path) {
        return new ResourceLocation(this.modID, path);
    }

}
