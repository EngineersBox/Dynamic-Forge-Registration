package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.entity.layers.HorseMarkingsLayer;
import net.minecraft.client.renderer.entity.layers.LeatherHorseArmorLayer;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.entity.passive.horse.CoatColors;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class HorseRenderer extends AbstractHorseRenderer<HorseEntity, HorseModel<HorseEntity>> {
   private static final Map<CoatColors, ResourceLocation> field_239383_a_ = Util.make(Maps.newEnumMap(CoatColors.class), (coatColorMap) -> {
      coatColorMap.put(CoatColors.WHITE, new ResourceLocation("textures/entity/horse/horse_white.png"));
      coatColorMap.put(CoatColors.CREAMY, new ResourceLocation("textures/entity/horse/horse_creamy.png"));
      coatColorMap.put(CoatColors.CHESTNUT, new ResourceLocation("textures/entity/horse/horse_chestnut.png"));
      coatColorMap.put(CoatColors.BROWN, new ResourceLocation("textures/entity/horse/horse_brown.png"));
      coatColorMap.put(CoatColors.BLACK, new ResourceLocation("textures/entity/horse/horse_black.png"));
      coatColorMap.put(CoatColors.GRAY, new ResourceLocation("textures/entity/horse/horse_gray.png"));
      coatColorMap.put(CoatColors.DARKBROWN, new ResourceLocation("textures/entity/horse/horse_darkbrown.png"));
   });

   public HorseRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new HorseModel<>(0.0F), 1.1F);
      this.addLayer(new HorseMarkingsLayer(this));
      this.addLayer(new LeatherHorseArmorLayer(this));
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(HorseEntity entity) {
      return field_239383_a_.get(entity.func_234239_eK_());
   }
}