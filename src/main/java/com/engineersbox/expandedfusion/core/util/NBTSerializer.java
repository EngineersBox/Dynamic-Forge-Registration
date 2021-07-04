package com.engineersbox.expandedfusion.core.util;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializer<T> {
    T read(CompoundNBT tags);

    void write(CompoundNBT tags, T obj);
}