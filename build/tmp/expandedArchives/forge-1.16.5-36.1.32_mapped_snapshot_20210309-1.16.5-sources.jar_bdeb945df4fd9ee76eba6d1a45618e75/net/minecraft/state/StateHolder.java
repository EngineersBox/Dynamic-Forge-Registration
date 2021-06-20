package net.minecraft.state;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public abstract class StateHolder<O, S> {
   private static final Function<Entry<Property<?>, Comparable<?>>, String> printableFunction = new Function<Entry<Property<?>, Comparable<?>>, String>() {
      public String apply(@Nullable Entry<Property<?>, Comparable<?>> p_apply_1_) {
         if (p_apply_1_ == null) {
            return "<NULL>";
         } else {
            Property<?> property = p_apply_1_.getKey();
            return property.getName() + "=" + this.func_235905_a_(property, p_apply_1_.getValue());
         }
      }

      private <T extends Comparable<T>> String func_235905_a_(Property<T> property, Comparable<?> comparable) {
         return property.getName((T)comparable);
      }
   };
   protected final O instance;
   private final ImmutableMap<Property<?>, Comparable<?>> properties;
   private Table<Property<?>, Comparable<?>, S> propertyToStateTable;
   protected final MapCodec<S> mapCodec;

   protected StateHolder(O instance, ImmutableMap<Property<?>, Comparable<?>> properties, MapCodec<S> mapCodec) {
      this.instance = instance;
      this.properties = properties;
      this.mapCodec = mapCodec;
   }

   public <T extends Comparable<T>> S cycleValue(Property<T> property) {
      return this.with(property, getNextValue(property.getAllowedValues(), this.get(property)));
   }

   protected static <T> T getNextValue(Collection<T> allowedValues, T p_235898_1_) {
      Iterator<T> iterator = allowedValues.iterator();

      while(iterator.hasNext()) {
         if (iterator.next().equals(p_235898_1_)) {
            if (iterator.hasNext()) {
               return iterator.next();
            }

            return allowedValues.iterator().next();
         }
      }

      return iterator.next();
   }

   public String toString() {
      StringBuilder stringbuilder = new StringBuilder();
      stringbuilder.append(this.instance);
      if (!this.getValues().isEmpty()) {
         stringbuilder.append('[');
         stringbuilder.append(this.getValues().entrySet().stream().map(printableFunction).collect(Collectors.joining(",")));
         stringbuilder.append(']');
      }

      return stringbuilder.toString();
   }

   /**
    * Gets an unmodifiable collection of all possible properties.
    */
   public Collection<Property<?>> getProperties() {
      return Collections.unmodifiableCollection(this.properties.keySet());
   }

   public <T extends Comparable<T>> boolean hasProperty(Property<T> property) {
      return this.properties.containsKey(property);
   }

   /**
    * Get the value of the given Property for this BlockState
    */
   public <T extends Comparable<T>> T get(Property<T> property) {
      Comparable<?> comparable = this.properties.get(property);
      if (comparable == null) {
         throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.instance);
      } else {
         return property.getValueClass().cast(comparable);
      }
   }

   public <T extends Comparable<T>> Optional<T> func_235903_d_(Property<T> property) {
      Comparable<?> comparable = this.properties.get(property);
      return comparable == null ? Optional.empty() : Optional.of(property.getValueClass().cast(comparable));
   }

   public <T extends Comparable<T>, V extends T> S with(Property<T> property, V value) {
      Comparable<?> comparable = this.properties.get(property);
      if (comparable == null) {
         throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.instance);
      } else if (comparable == value) {
         return (S)this;
      } else {
         S s = this.propertyToStateTable.get(property, value);
         if (s == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this.instance + ", it is not an allowed value");
         } else {
            return s;
         }
      }
   }

   public void func_235899_a_(Map<Map<Property<?>, Comparable<?>>, S> p_235899_1_) {
      if (this.propertyToStateTable != null) {
         throw new IllegalStateException();
      } else {
         Table<Property<?>, Comparable<?>, S> table = HashBasedTable.create();

         for(Entry<Property<?>, Comparable<?>> entry : this.properties.entrySet()) {
            Property<?> property = entry.getKey();

            for(Comparable<?> comparable : property.getAllowedValues()) {
               if (comparable != entry.getValue()) {
                  table.put(property, comparable, p_235899_1_.get(this.func_235902_b_(property, comparable)));
               }
            }
         }

         this.propertyToStateTable = (Table<Property<?>, Comparable<?>, S>)(table.isEmpty() ? table : ArrayTable.create(table));
      }
   }

   private Map<Property<?>, Comparable<?>> func_235902_b_(Property<?> property, Comparable<?> comparable) {
      Map<Property<?>, Comparable<?>> map = Maps.newHashMap(this.properties);
      map.put(property, comparable);
      return map;
   }

   public ImmutableMap<Property<?>, Comparable<?>> getValues() {
      return this.properties;
   }

   protected static <O, S extends StateHolder<O, S>> Codec<S> func_235897_a_(Codec<O> codec, Function<O, S> p_235897_1_) {
      return codec.dispatch("Name", (holder) -> {
         return holder.instance;
      }, (property) -> {
         S s = p_235897_1_.apply(property);
         return s.getValues().isEmpty() ? Codec.unit(s) : s.mapCodec.fieldOf("Properties").codec();
      });
   }
}