package net.minecraft.loot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConditionArrayParser {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ResourceLocation id;
   private final LootPredicateManager predicateManager;
   private final Gson gson = LootSerializers.func_237386_a_().create();

   public ConditionArrayParser(ResourceLocation id, LootPredicateManager predicateManager) {
      this.id = id;
      this.predicateManager = predicateManager;
   }

   public final ILootCondition[] deserializeConditions(JsonArray json, String p_234050_2_, LootParameterSet parameterSet) {
      ILootCondition[] ailootcondition = this.gson.fromJson(json, ILootCondition[].class);
      ValidationTracker validationtracker = new ValidationTracker(parameterSet, this.predicateManager::getCondition, (p_234052_0_) -> {
         return null;
      });

      for(ILootCondition ilootcondition : ailootcondition) {
         ilootcondition.func_225580_a_(validationtracker);
         validationtracker.getProblems().forEach((p_234051_1_, p_234051_2_) -> {
            LOGGER.warn("Found validation problem in advancement trigger {}/{}: {}", p_234050_2_, p_234051_1_, p_234051_2_);
         });
      }

      return ailootcondition;
   }

   public ResourceLocation getId() {
      return this.id;
   }
}