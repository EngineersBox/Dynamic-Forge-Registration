package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.model.BoarModel;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HoglinRenderer extends MobRenderer<HoglinEntity, BoarModel<HoglinEntity>> {
   private static final ResourceLocation HOGLIN_TEXTURES = new ResourceLocation("textures/entity/hoglin/hoglin.png");

   public HoglinRenderer(EntityRendererManager p_i232470_1_) {
      super(p_i232470_1_, new BoarModel<>(), 0.7F);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(HoglinEntity entity) {
      return HOGLIN_TEXTURES;
   }

   protected boolean func_230495_a_(HoglinEntity entity) {
      return entity.func_234364_eK_();
   }
}