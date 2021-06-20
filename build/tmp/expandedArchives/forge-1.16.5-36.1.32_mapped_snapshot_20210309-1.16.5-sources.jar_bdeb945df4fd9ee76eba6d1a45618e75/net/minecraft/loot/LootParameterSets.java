package net.minecraft.loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;

public class LootParameterSets {
   private static final BiMap<ResourceLocation, LootParameterSet> REGISTRY = HashBiMap.create();
   public static final LootParameterSet EMPTY = register("empty", (builder) -> {
   });
   public static final LootParameterSet CHEST = register("chest", (builder) -> {
      builder.required(LootParameters.ORIGIN).optional(LootParameters.THIS_ENTITY);
      builder.optional(LootParameters.KILLER_ENTITY); //Forge: Chest Minecarts can have killers.
   });
   public static final LootParameterSet COMMAND = register("command", (builder) -> {
      builder.required(LootParameters.ORIGIN).optional(LootParameters.THIS_ENTITY);
   });
   public static final LootParameterSet SELECTOR = register("selector", (builder) -> {
      builder.required(LootParameters.ORIGIN).required(LootParameters.THIS_ENTITY);
   });
   public static final LootParameterSet FISHING = register("fishing", (builder) -> {
      builder.required(LootParameters.ORIGIN).required(LootParameters.TOOL).optional(LootParameters.THIS_ENTITY);
      builder.optional(LootParameters.KILLER_ENTITY).optional(LootParameters.THIS_ENTITY); //Forge: Allow fisher, and bobber
   });
   public static final LootParameterSet ENTITY = register("entity", (builder) -> {
      builder.required(LootParameters.THIS_ENTITY).required(LootParameters.ORIGIN).required(LootParameters.DAMAGE_SOURCE).optional(LootParameters.KILLER_ENTITY).optional(LootParameters.DIRECT_KILLER_ENTITY).optional(LootParameters.LAST_DAMAGE_PLAYER);
   });
   public static final LootParameterSet GIFT = register("gift", (builder) -> {
      builder.required(LootParameters.ORIGIN).required(LootParameters.THIS_ENTITY);
   });
   public static final LootParameterSet BARTER = register("barter", (builder) -> {
      builder.required(LootParameters.THIS_ENTITY);
   });
   public static final LootParameterSet ADVANCEMENT = register("advancement_reward", (builder) -> {
      builder.required(LootParameters.THIS_ENTITY).required(LootParameters.ORIGIN);
   });
   public static final LootParameterSet ADVANCEMENT_ENTITY = register("advancement_entity", (builder) -> {
      builder.required(LootParameters.THIS_ENTITY).required(LootParameters.ORIGIN);
   });
   public static final LootParameterSet GENERIC = register("generic", (builder) -> {
      builder.required(LootParameters.THIS_ENTITY).required(LootParameters.LAST_DAMAGE_PLAYER).required(LootParameters.DAMAGE_SOURCE).required(LootParameters.KILLER_ENTITY).required(LootParameters.DIRECT_KILLER_ENTITY).required(LootParameters.ORIGIN).required(LootParameters.BLOCK_STATE).required(LootParameters.BLOCK_ENTITY).required(LootParameters.TOOL).required(LootParameters.EXPLOSION_RADIUS);
   });
   public static final LootParameterSet BLOCK = register("block", (builder) -> {
      builder.required(LootParameters.BLOCK_STATE).required(LootParameters.ORIGIN).required(LootParameters.TOOL).optional(LootParameters.THIS_ENTITY).optional(LootParameters.BLOCK_ENTITY).optional(LootParameters.EXPLOSION_RADIUS);
   });

   private static LootParameterSet register(String registryName, Consumer<LootParameterSet.Builder> builderConsumer) {
      LootParameterSet.Builder lootparameterset$builder = new LootParameterSet.Builder();
      builderConsumer.accept(lootparameterset$builder);
      LootParameterSet lootparameterset = lootparameterset$builder.build();
      ResourceLocation resourcelocation = new ResourceLocation(registryName);
      LootParameterSet lootparameterset1 = REGISTRY.put(resourcelocation, lootparameterset);
      if (lootparameterset1 != null) {
         throw new IllegalStateException("Loot table parameter set " + resourcelocation + " is already registered");
      } else {
         return lootparameterset;
      }
   }

   @Nullable
   public static LootParameterSet getValue(ResourceLocation registryName) {
      return REGISTRY.get(registryName);
   }

   @Nullable
   public static ResourceLocation getKey(LootParameterSet parameterSet) {
      return REGISTRY.inverse().get(parameterSet);
   }
}
