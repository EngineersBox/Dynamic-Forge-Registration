package net.minecraft.world.gen.feature.template;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKeyCodec;

public interface IStructureProcessorType<P extends StructureProcessor> {
   IStructureProcessorType<BlockIgnoreStructureProcessor> BLOCK_IGNORE = register("block_ignore", BlockIgnoreStructureProcessor.CODEC);
   IStructureProcessorType<IntegrityProcessor> BLOCK_ROT = register("block_rot", IntegrityProcessor.CODEC);
   IStructureProcessorType<GravityStructureProcessor> GRAVITY = register("gravity", GravityStructureProcessor.field_237081_a_);
   IStructureProcessorType<JigsawReplacementStructureProcessor> JIGSAW_REPLACEMENT = register("jigsaw_replacement", JigsawReplacementStructureProcessor.CODEC);
   IStructureProcessorType<RuleStructureProcessor> RULE = register("rule", RuleStructureProcessor.CODEC);
   IStructureProcessorType<NopProcessor> NOP = register("nop", NopProcessor.CODEC);
   IStructureProcessorType<BlockMosinessProcessor> BLOCK_AGE = register("block_age", BlockMosinessProcessor.CODEC);
   IStructureProcessorType<BlackStoneReplacementProcessor> BLACKSTONE_REPLACE = register("blackstone_replace", BlackStoneReplacementProcessor.CODEC);
   IStructureProcessorType<LavaSubmergingProcessor> LAVA_SUBMERGED_BLOCK = register("lava_submerged_block", LavaSubmergingProcessor.CODEC);
   Codec<StructureProcessor> CODEC = Registry.STRUCTURE_PROCESSOR.dispatch("processor_type", StructureProcessor::getType, IStructureProcessorType::codec);
   Codec<StructureProcessorList> PROCESSOR_CODEC = CODEC.listOf().xmap(StructureProcessorList::new, StructureProcessorList::func_242919_a);
   Codec<StructureProcessorList> PROCESSOR_LIST_OBJECT_CODEC = Codec.either(PROCESSOR_CODEC.fieldOf("processors").codec(), PROCESSOR_CODEC).xmap((either) -> {
      return either.map((processor) -> {
         return processor;
      }, (processor) -> {
         return processor;
      });
   }, Either::left);
   Codec<Supplier<StructureProcessorList>> PROCESSOR_LIST_CODEC = RegistryKeyCodec.create(Registry.STRUCTURE_PROCESSOR_LIST_KEY, PROCESSOR_LIST_OBJECT_CODEC);

   Codec<P> codec();

   static <P extends StructureProcessor> IStructureProcessorType<P> register(String name, Codec<P> codec) {
      return Registry.register(Registry.STRUCTURE_PROCESSOR, name, () -> {
         return codec;
      });
   }
}