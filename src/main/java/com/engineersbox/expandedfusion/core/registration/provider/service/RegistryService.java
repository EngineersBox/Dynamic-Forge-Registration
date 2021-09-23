package com.engineersbox.expandedfusion.core.registration.provider.service;

import net.minecraft.util.ResourceLocation;

public abstract class RegistryService<T> {

    public String modID;

    public ResourceLocation getId(String path) {
        return new ResourceLocation(this.modID, path);
    }

}
