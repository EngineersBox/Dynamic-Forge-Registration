package com.engineersbox.expandedfusion.core.util;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializer<T> {
    T read(final CompoundNBT tags);

    void write(final CompoundNBT tags, final T obj);
}