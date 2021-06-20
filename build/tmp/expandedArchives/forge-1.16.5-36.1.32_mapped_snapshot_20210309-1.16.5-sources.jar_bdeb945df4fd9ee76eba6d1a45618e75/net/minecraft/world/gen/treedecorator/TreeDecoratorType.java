package net.minecraft.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;

public class TreeDecoratorType<P extends TreeDecorator> extends net.minecraftforge.registries.ForgeRegistryEntry<TreeDecoratorType<?>> {
   public static final TreeDecoratorType<TrunkVineTreeDecorator> TRUNK_VINE = register("trunk_vine", TrunkVineTreeDecorator.CODEC);
   public static final TreeDecoratorType<LeaveVineTreeDecorator> LEAVE_VINE = register("leave_vine", LeaveVineTreeDecorator.field_236870_a_);
   public static final TreeDecoratorType<CocoaTreeDecorator> COCOA = register("cocoa", CocoaTreeDecorator.field_236866_a_);
   public static final TreeDecoratorType<BeehiveTreeDecorator> BEEHIVE = register("beehive", BeehiveTreeDecorator.field_236863_a_);
   public static final TreeDecoratorType<AlterGroundTreeDecorator> ALTER_GROUND = register("alter_ground", AlterGroundTreeDecorator.CODEC);
   private final Codec<P> codec;

   private static <P extends TreeDecorator> TreeDecoratorType<P> register(String p_236877_0_, Codec<P> codec) {
      return Registry.register(Registry.TREE_DECORATOR_TYPE, p_236877_0_, new TreeDecoratorType<>(codec));
   }

   public TreeDecoratorType(Codec<P> codec) {
      this.codec = codec;
   }

   public Codec<P> getCodec() {
      return this.codec;
   }
}
