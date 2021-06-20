package net.minecraft.state;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class Property<T extends Comparable<T>> {
   private final Class<T> valueClass;
   private final String name;
   private Integer hash;
   private final Codec<T> propertyCodec = Codec.STRING.comapFlatMap((p_235919_1_) -> {
      return this.parseValue(p_235919_1_).map(DataResult::success).orElseGet(() -> {
         return DataResult.error("Unable to read property: " + this + " with value: " + p_235919_1_);
      });
   }, this::getName);
   private final Codec<Property.ValuePair<T>> propertyValuePairCodec = this.propertyCodec.xmap(this::getPairWithValue, Property.ValuePair::getValue);

   protected Property(String name, Class<T> valueClass) {
      this.valueClass = valueClass;
      this.name = name;
   }

   public Property.ValuePair<T> getPairWithValue(T value) {
      return new Property.ValuePair<>(this, value);
   }

   public Property.ValuePair<T> getValuePair(StateHolder<?, ?> holder) {
      return new Property.ValuePair<>(this, holder.get(this));
   }

   public Stream<Property.ValuePair<T>> func_241491_c_() {
      return this.getAllowedValues().stream().map(this::getPairWithValue);
   }

   public Codec<Property.ValuePair<T>> getPropertyValuePairCodec() {
      return this.propertyValuePairCodec;
   }

   public String getName() {
      return this.name;
   }

   /**
    * The class of the values of this property
    */
   public Class<T> getValueClass() {
      return this.valueClass;
   }

   public abstract Collection<T> getAllowedValues();

   /**
    * Get the name for the given value.
    */
   public abstract String getName(T value);

   public abstract Optional<T> parseValue(String value);

   public String toString() {
      return MoreObjects.toStringHelper(this).add("name", this.name).add("clazz", this.valueClass).add("values", this.getAllowedValues()).toString();
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof Property)) {
         return false;
      } else {
         Property<?> property = (Property)p_equals_1_;
         return this.valueClass.equals(property.valueClass) && this.name.equals(property.name);
      }
   }

   public final int hashCode() {
      if (this.hash == null) {
         this.hash = this.computeHashCode();
      }

      return this.hash;
   }

   public int computeHashCode() {
      return 31 * this.valueClass.hashCode() + this.name.hashCode();
   }

   public static final class ValuePair<T extends Comparable<T>> {
      private final Property<T> property;
      private final T value;

      private ValuePair(Property<T> property, T value) {
         if (!property.getAllowedValues().contains(value)) {
            throw new IllegalArgumentException("Value " + value + " does not belong to property " + property);
         } else {
            this.property = property;
            this.value = value;
         }
      }

      public Property<T> getProperty() {
         return this.property;
      }

      public T getValue() {
         return this.value;
      }

      public String toString() {
         return this.property.getName() + "=" + this.property.getName(this.value);
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (!(p_equals_1_ instanceof Property.ValuePair)) {
            return false;
         } else {
            Property.ValuePair<?> valuepair = (Property.ValuePair)p_equals_1_;
            return this.property == valuepair.property && this.value.equals(valuepair.value);
         }
      }

      public int hashCode() {
         int i = this.property.hashCode();
         return 31 * i + this.value.hashCode();
      }
   }
}