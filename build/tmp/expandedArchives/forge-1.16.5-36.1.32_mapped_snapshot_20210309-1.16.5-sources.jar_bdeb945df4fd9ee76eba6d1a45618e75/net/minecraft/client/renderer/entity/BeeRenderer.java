package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BeeRenderer extends MobRenderer<BeeEntity, BeeModel<BeeEntity>> {
   private static final ResourceLocation ANGRY_BEE_TEXTURES = new ResourceLocation("textures/entity/bee/bee_angry.png");
   private static final ResourceLocation ANGRY_NECTAR_BEE_TEXTURES = new ResourceLocation("textures/entity/bee/bee_angry_nectar.png");
   private static final ResourceLocation BEE_TEXTURES = new ResourceLocation("textures/entity/bee/bee.png");
   private static final ResourceLocation NECTAR_BEE_TEXTURES = new ResourceLocation("textures/entity/bee/bee_nectar.png");

   public BeeRenderer(EntityRendererManager p_i226033_1_) {
      super(p_i226033_1_, new BeeModel<>(), 0.4F);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(BeeEntity entity) {
      if (entity.isAngry()) {
         return entity.hasNectar() ? ANGRY_NECTAR_BEE_TEXTURES : ANGRY_BEE_TEXTURES;
      } else {
         return entity.hasNectar() ? NECTAR_BEE_TEXTURES : BEE_TEXTURES;
      }
   }
}