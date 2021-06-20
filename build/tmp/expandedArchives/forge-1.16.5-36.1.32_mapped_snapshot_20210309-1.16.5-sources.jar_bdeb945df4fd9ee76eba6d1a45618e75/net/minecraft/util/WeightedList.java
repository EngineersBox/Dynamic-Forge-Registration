package net.minecraft.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class WeightedList<U> {
   protected final List<WeightedList.Entry<U>> weightedEntries;
   private final Random random = new Random();

   public WeightedList() {
      this(Lists.newArrayList());
   }

   private WeightedList(List<WeightedList.Entry<U>> weightedEntries) {
      this.weightedEntries = Lists.newArrayList(weightedEntries);
   }

   public static <U> Codec<WeightedList<U>> getCodec(Codec<U> elementCodec) {
      return WeightedList.Entry.<U>getEntryCodec(elementCodec).listOf().xmap(WeightedList::new, (weightedList) -> {
         return weightedList.weightedEntries;
      });
   }

   public WeightedList<U> addWeighted(U value, int weight) {
      this.weightedEntries.add(new WeightedList.Entry(value, weight));
      return this;
   }

   public WeightedList<U> randomizeWithWeight() {
      return this.randomizeWithWeight(this.random);
   }

   public WeightedList<U> randomizeWithWeight(Random rand) {
      this.weightedEntries.forEach((entry) -> {
         entry.generateChance(rand.nextFloat());
      });
      this.weightedEntries.sort(Comparator.comparingDouble((weightedEntry) -> {
         return weightedEntry.getChance();
      }));
      return this;
   }

   public boolean isEmpty() {
      return this.weightedEntries.isEmpty();
   }

   public Stream<U> getValueStream() {
      return this.weightedEntries.stream().map(WeightedList.Entry::getValue);
   }

   public U getRandomValue(Random rand) {
      return this.randomizeWithWeight(rand).getValueStream().findFirst().orElseThrow(RuntimeException::new);
   }

   public String toString() {
      return "WeightedList[" + this.weightedEntries + "]";
   }

   public static class Entry<T> {
      private final T value;
      private final int weight;
      private double chance;

      private Entry(T value, int weight) {
         this.weight = weight;
         this.value = value;
      }

      private double getChance() {
         return this.chance;
      }

      private void generateChance(float base) {
         this.chance = -Math.pow((double)base, (double)(1.0F / (float)this.weight));
      }

      public T getValue() {
         return this.value;
      }

      public String toString() {
         return "" + this.weight + ":" + this.value;
      }

      public static <E> Codec<WeightedList.Entry<E>> getEntryCodec(final Codec<E> elementCodec) {
         return new Codec<WeightedList.Entry<E>>() {
            public <T> DataResult<Pair<WeightedList.Entry<E>, T>> decode(DynamicOps<T> p_decode_1_, T p_decode_2_) {
               Dynamic<T> dynamic = new Dynamic<>(p_decode_1_, p_decode_2_);
               return dynamic.get("data").flatMap(elementCodec::parse).map((value) -> {
                  return new WeightedList.Entry(value, dynamic.get("weight").asInt(1));
               }).map((weightedEntry) -> {
                  return Pair.of(weightedEntry, p_decode_1_.empty());
               });
            }

            public <T> DataResult<T> encode(WeightedList.Entry<E> p_encode_1_, DynamicOps<T> p_encode_2_, T p_encode_3_) {
               return p_encode_2_.mapBuilder().add("weight", p_encode_2_.createInt(p_encode_1_.weight)).add("data", elementCodec.encodeStart(p_encode_2_, p_encode_1_.value)).build(p_encode_3_);
            }
         };
      }
   }
}