package net.minecraft.client.renderer;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SortedMap;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTypeBuffers {
   private final RegionRenderCacheBuilder fixedBuilder = new RegionRenderCacheBuilder();
   private final SortedMap<RenderType, BufferBuilder> fixedBuffers = Util.make(new Object2ObjectLinkedOpenHashMap<>(), (map) -> {
      map.put(Atlases.getSolidBlockType(), this.fixedBuilder.getBuilder(RenderType.getSolid()));
      map.put(Atlases.getCutoutBlockType(), this.fixedBuilder.getBuilder(RenderType.getCutout()));
      map.put(Atlases.getBannerType(), this.fixedBuilder.getBuilder(RenderType.getCutoutMipped()));
      map.put(Atlases.getTranslucentCullBlockType(), this.fixedBuilder.getBuilder(RenderType.getTranslucent()));
      put(map, Atlases.getShieldType());
      put(map, Atlases.getBedType());
      put(map, Atlases.getShulkerBoxType());
      put(map, Atlases.getSignType());
      put(map, Atlases.getChestType());
      put(map, RenderType.getTranslucentNoCrumbling());
      put(map, RenderType.getArmorGlint());
      put(map, RenderType.getArmorEntityGlint());
      put(map, RenderType.getGlint());
      put(map, RenderType.getGlintDirect());
      put(map, RenderType.getGlintTranslucent());
      put(map, RenderType.getEntityGlint());
      put(map, RenderType.getEntityGlintDirect());
      put(map, RenderType.getWaterMask());
      ModelBakery.DESTROY_RENDER_TYPES.forEach((renderType) -> {
         put(map, renderType);
      });
   });
   private final IRenderTypeBuffer.Impl bufferSource = IRenderTypeBuffer.getImpl(this.fixedBuffers, new BufferBuilder(256));
   private final IRenderTypeBuffer.Impl crumblingBufferSource = IRenderTypeBuffer.getImpl(new BufferBuilder(256));
   private final OutlineLayerBuffer outlineBufferSource = new OutlineLayerBuffer(this.bufferSource);

   private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> mapBuildersIn, RenderType renderTypeIn) {
      mapBuildersIn.put(renderTypeIn, new BufferBuilder(renderTypeIn.getBufferSize()));
   }

   public RegionRenderCacheBuilder getFixedBuilder() {
      return this.fixedBuilder;
   }

   public IRenderTypeBuffer.Impl getBufferSource() {
      return this.bufferSource;
   }

   public IRenderTypeBuffer.Impl getCrumblingBufferSource() {
      return this.crumblingBufferSource;
   }

   public OutlineLayerBuffer getOutlineBufferSource() {
      return this.outlineBufferSource;
   }
}