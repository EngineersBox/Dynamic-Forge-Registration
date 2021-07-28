package com.engineersbox.expandedfusion.core.sync;

import com.engineersbox.expandedfusion.core.functional.ThrowsBiFunction;
import com.engineersbox.expandedfusion.core.functional.ThrowsTriConsumer;
import net.minecraft.nbt.CompoundNBT;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

public class FieldPrimitiveActions<T> {
    public final BiFunction<CompoundNBT, String, T> tagsGet;
    public final ThrowsTriConsumer<CompoundNBT, String> tagsPut;
    public final ThrowsBiFunction<Field, Object, T> fieldGet;
    public final ThrowsTriConsumer<Field, Object> fieldSet;

    public FieldPrimitiveActions(final BiFunction<CompoundNBT, String, T> tagsGet,
                                 final ThrowsTriConsumer<CompoundNBT, String> tagsPut,
                                 final ThrowsBiFunction<Field, Object, T> fieldGet,
                                 final ThrowsTriConsumer<Field, Object> fieldSet) {
        this.tagsGet = tagsGet;
        this.tagsPut = tagsPut;
        this.fieldGet = fieldGet;
        this.fieldSet = fieldSet;
    }
}
