package com.engineersbox.expandedfusion.core.sync;

import com.engineersbox.expandedfusion.core.sync.annotation.SyncVariable;
import com.engineersbox.expandedfusion.core.util.NBTSerializer;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundNBT;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SyncHandler {
    private static final Map<Class<?>, NBTSerializer<?>> SERIALIZERS = new HashMap<>();
    private static final Map<Class<?>, FieldPrimitiveActions<?>> FIELD_TAG_PRIMITIVE_ACTIONS = ImmutableMap.<Class<?>, FieldPrimitiveActions<?>>builder()
            .put(int.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getInt, (c, o, v) -> c.putInt(o, (int) v),
                    Field::getInt, (f, o, v) -> f.setInt(o, (int) v)
            )).put(float.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getFloat, (c, o, v) -> c.putFloat(o, (float) v),
                    Field::getFloat, (f, o, v) -> f.setFloat(o, (float) v)
            )).put(String.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getString, (c, o, v) -> c.putString(o, (String) v),
                    Field::get, Field::set
            )).put(boolean.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getBoolean, (c, o, v) -> c.putBoolean(o, (boolean) v),
                    Field::getBoolean, (f, o, v) -> f.setBoolean(o, (boolean) v)
            )).put(double.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getDouble, (c, o, v) -> c.putDouble(o, (double) v),
                    Field::getDouble, (f, o, v) -> f.setDouble(o, (double) v)
            )).put(long.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getLong, (c, o, v) -> c.putLong(o, (long) v),
                    Field::getLong, (f, o, v) -> f.setLong(o, (long) v)
            )).put(short.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getShort, (c, o, v) -> c.putShort(o, (short) v),
                    Field::getShort, (f, o, v) -> f.setShort(o, (short) v)
            )).put(byte.class, new FieldPrimitiveActions<>(
                    CompoundNBT::getByte, (c, o, v) -> c.putByte(o, (byte) v),
                    Field::getByte, (f, o, v) -> f.setByte(o, (byte) v)
            )).build();

    public static <T> void registerSerializer(final Class<T> clazz, final Function<CompoundNBT, T> reader, BiConsumer<CompoundNBT, T> writer) {
        SERIALIZERS.put(clazz, new NBTSerializer<T>() {
            @Override
            public T read(CompoundNBT tags) {
                return reader.apply(tags);
            }

            @Override
            public void write(CompoundNBT tags, T obj) {
                writer.accept(tags, obj);
            }
        });
    }

    public static void readSyncVars(final Object obj, final CompoundNBT tags) {
        Arrays.stream(obj.getClass().getDeclaredFields()).forEach((field) -> Arrays.stream(field.getDeclaredAnnotations())
            .filter((annotation) -> annotation instanceof SyncVariable)
            .forEach((annotation) -> {
                final SyncVariable sync = (SyncVariable) annotation;

                try {
                    // Set fields accessible if necessary.
                    if (!field.isAccessible())
                        field.setAccessible(true);
                    final String name = sync.name();

                    final FieldPrimitiveActions<?> actions = FIELD_TAG_PRIMITIVE_ACTIONS.get(field.getType());
                    if (actions != null) {
                        actions.fieldSet.apply(field, obj, actions.tagsGet.apply(tags, name));
                        return;
                    }
                    if (SERIALIZERS.containsKey(field.getType())) {
                        final NBTSerializer<?> serializer = SERIALIZERS.get(field.getType());
                        final CompoundNBT compound = tags.getCompound(name);
                        field.set(obj, serializer.read(compound));
                        return;
                    }
                    throw new IllegalArgumentException("Non-primitive field type, cannot write to NBT: " + field.getType());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static CompoundNBT writeSyncVars(final Object obj, final CompoundNBT tags, final SyncVariable.Type syncType) {
        Arrays.stream(obj.getClass().getDeclaredFields()).forEach((field) -> Arrays.stream(field.getDeclaredAnnotations())
            .filter((annotation) -> {
                if (!(annotation instanceof SyncVariable)) {
                    return false;
                }
                final SyncVariable sync = (SyncVariable) annotation;
                return syncType == SyncVariable.Type.WRITE || !sync.onWrite() || syncType == SyncVariable.Type.PACKET || !sync.onPacket();
            })
            .forEach((annotation) -> {
                final SyncVariable sync = (SyncVariable) annotation;
                try {
                    // Set fields accessible if necessary.
                    if (!field.isAccessible())
                        field.setAccessible(true);
                    final String name = sync.name();

                    final FieldPrimitiveActions<?> actions = FIELD_TAG_PRIMITIVE_ACTIONS.get(field.getType());
                    if (actions != null) {
                        actions.tagsPut.apply(tags, name, actions.fieldGet.apply(field, obj));
                        return;
                    }
                    if (SERIALIZERS.containsKey(field.getType())) {
                        final CompoundNBT compound = new CompoundNBT();
                        final NBTSerializer serializer = SERIALIZERS.get(field.getType());
                        serializer.write(compound, field.get(obj));
                        tags.put(name, compound);
                        return;
                    }
                    throw new IllegalArgumentException("Non-primitive field type, cannot write to NBT: " + field.getType());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }));
        return tags;
    }
}
