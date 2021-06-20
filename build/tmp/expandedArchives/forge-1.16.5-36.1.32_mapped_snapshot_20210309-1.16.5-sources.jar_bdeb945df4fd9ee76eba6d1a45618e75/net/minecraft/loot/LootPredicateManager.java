package net.minecraft.loot;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.LootConditionManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootPredicateManager extends JsonReloadListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Gson GSON = LootSerializers.func_237386_a_().create();
   private Map<ResourceLocation, ILootCondition> idConditionMap = ImmutableMap.of();

   public LootPredicateManager() {
      super(GSON, "predicates");
   }

   @Nullable
   public ILootCondition getCondition(ResourceLocation id) {
      return this.idConditionMap.get(id);
   }

   protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
      Builder<ResourceLocation, ILootCondition> builder = ImmutableMap.builder();
      objectIn.forEach((id, element) -> {
         try {
            if (element.isJsonArray()) {
               ILootCondition[] ailootcondition = GSON.fromJson(element, ILootCondition[].class);
               builder.put(id, new LootPredicateManager.AndCombiner(ailootcondition));
            } else {
               ILootCondition ilootcondition = GSON.fromJson(element, ILootCondition.class);
               builder.put(id, ilootcondition);
            }
         } catch (Exception exception) {
            LOGGER.error("Couldn't parse loot table {}", id, exception);
         }

      });
      Map<ResourceLocation, ILootCondition> map = builder.build();
      ValidationTracker validationtracker = new ValidationTracker(LootParameterSets.GENERIC, map::get, (p_227518_0_) -> {
         return null;
      });
      map.forEach((id, condition) -> {
         condition.func_225580_a_(validationtracker.func_227535_b_("{" + id + "}", id));
      });
      validationtracker.getProblems().forEach((p_227516_0_, p_227516_1_) -> {
         LOGGER.warn("Found validation problem in " + p_227516_0_ + ": " + p_227516_1_);
      });
      this.idConditionMap = map;
   }

   public Set<ResourceLocation> getConditionIds() {
      return Collections.unmodifiableSet(this.idConditionMap.keySet());
   }

   static class AndCombiner implements ILootCondition {
      private final ILootCondition[] conditions;
      private final Predicate<LootContext> contextPredicate;

      private AndCombiner(ILootCondition[] conditions) {
         this.conditions = conditions;
         this.contextPredicate = LootConditionManager.and(conditions);
      }

      public final boolean test(LootContext p_test_1_) {
         return this.contextPredicate.test(p_test_1_);
      }

      public void func_225580_a_(ValidationTracker p_225580_1_) {
         ILootCondition.super.func_225580_a_(p_225580_1_);

         for(int i = 0; i < this.conditions.length; ++i) {
            this.conditions[i].func_225580_a_(p_225580_1_.func_227534_b_(".term[" + i + "]"));
         }

      }

      public LootConditionType getConditionType() {
         throw new UnsupportedOperationException();
      }
   }
}