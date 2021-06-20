package net.minecraft.loot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.loot.conditions.ILootCondition;

public class ConditionArraySerializer {
   public static final ConditionArraySerializer field_235679_a_ = new ConditionArraySerializer();
   private final Gson gson = LootSerializers.func_237386_a_().create();

   public final JsonElement serialize(ILootCondition[] conditions) {
      return this.gson.toJsonTree(conditions);
   }
}