package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MapItemRenderer implements AutoCloseable {
   private static final ResourceLocation TEXTURE_MAP_ICONS = new ResourceLocation("textures/map/map_icons.png");
   private static final RenderType MAP_ICONS_TYPE = RenderType.getText(TEXTURE_MAP_ICONS);
   private final TextureManager textureManager;
   private final Map<String, MapItemRenderer.Instance> loadedMaps = Maps.newHashMap();

   public MapItemRenderer(TextureManager textureManagerIn) {
      this.textureManager = textureManagerIn;
   }

   /**
    * Updates a map texture
    */
   public void updateMapTexture(MapData mapdataIn) {
      this.getMapRendererInstance(mapdataIn).updateMapTexture();
   }

   public void renderMap(MatrixStack matrixStack, IRenderTypeBuffer buffer, MapData mapData, boolean active, int packedLight) {
      this.getMapRendererInstance(mapData).renderMap(matrixStack, buffer, active, packedLight);
   }

   /**
    * Returns {@link net.minecraft.client.gui.MapItemRenderer.Instance MapItemRenderer.Instance} with given map data
    */
   private MapItemRenderer.Instance getMapRendererInstance(MapData mapdataIn) {
      MapItemRenderer.Instance mapitemrenderer$instance = this.loadedMaps.get(mapdataIn.getName());
      if (mapitemrenderer$instance == null) {
         mapitemrenderer$instance = new MapItemRenderer.Instance(mapdataIn);
         this.loadedMaps.put(mapdataIn.getName(), mapitemrenderer$instance);
      }

      return mapitemrenderer$instance;
   }

   @Nullable
   public MapItemRenderer.Instance getMapInstanceIfExists(String instanceName) {
      return this.loadedMaps.get(instanceName);
   }

   /**
    * Clears the currently loaded maps and removes their corresponding textures
    */
   public void clearLoadedMaps() {
      for(MapItemRenderer.Instance mapitemrenderer$instance : this.loadedMaps.values()) {
         mapitemrenderer$instance.close();
      }

      this.loadedMaps.clear();
   }

   @Nullable
   public MapData getData(@Nullable MapItemRenderer.Instance instance) {
      return instance != null ? instance.mapData : null;
   }

   public void close() {
      this.clearLoadedMaps();
   }

   @OnlyIn(Dist.CLIENT)
   class Instance implements AutoCloseable {
      private final MapData mapData;
      private final DynamicTexture mapTexture;
      private final RenderType renderType;

      private Instance(MapData mapdataIn) {
         this.mapData = mapdataIn;
         this.mapTexture = new DynamicTexture(128, 128, true);
         ResourceLocation resourcelocation = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.getName(), this.mapTexture);
         this.renderType = RenderType.getText(resourcelocation);
      }

      /**
       * Updates a map {@link net.minecraft.client.gui.MapItemRenderer.Instance#mapTexture texture}
       */
      private void updateMapTexture() {
         for(int i = 0; i < 128; ++i) {
            for(int j = 0; j < 128; ++j) {
               int k = j + i * 128;
               int l = this.mapData.colors[k] & 255;
               if (l / 4 == 0) {
                  this.mapTexture.getTextureData().setPixelRGBA(j, i, 0);
               } else {
                  this.mapTexture.getTextureData().setPixelRGBA(j, i, MaterialColor.COLORS[l / 4].getMapColor(l & 3));
               }
            }
         }

         this.mapTexture.updateDynamicTexture();
      }

      private void renderMap(MatrixStack matrixStack, IRenderTypeBuffer buffer, boolean active, int packedLight) {
         int i = 0;
         int j = 0;
         float f = 0.0F;
         Matrix4f matrix4f = matrixStack.getLast().getMatrix();
         IVertexBuilder ivertexbuilder = buffer.getBuffer(this.renderType);
         ivertexbuilder.pos(matrix4f, 0.0F, 128.0F, -0.01F).color(255, 255, 255, 255).tex(0.0F, 1.0F).lightmap(packedLight).endVertex();
         ivertexbuilder.pos(matrix4f, 128.0F, 128.0F, -0.01F).color(255, 255, 255, 255).tex(1.0F, 1.0F).lightmap(packedLight).endVertex();
         ivertexbuilder.pos(matrix4f, 128.0F, 0.0F, -0.01F).color(255, 255, 255, 255).tex(1.0F, 0.0F).lightmap(packedLight).endVertex();
         ivertexbuilder.pos(matrix4f, 0.0F, 0.0F, -0.01F).color(255, 255, 255, 255).tex(0.0F, 0.0F).lightmap(packedLight).endVertex();
         int k = 0;

         for(MapDecoration mapdecoration : this.mapData.mapDecorations.values()) {
            if (!active || mapdecoration.renderOnFrame()) {
               if (mapdecoration.render(k)) { k++; continue; }
               matrixStack.push();
               matrixStack.translate((double)(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F), (double)(0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F), (double)-0.02F);
               matrixStack.rotate(Vector3f.ZP.rotationDegrees((float)(mapdecoration.getRotation() * 360) / 16.0F));
               matrixStack.scale(4.0F, 4.0F, 3.0F);
               matrixStack.translate(-0.125D, 0.125D, 0.0D);
               byte b0 = mapdecoration.getImage();
               float f1 = (float)(b0 % 16 + 0) / 16.0F;
               float f2 = (float)(b0 / 16 + 0) / 16.0F;
               float f3 = (float)(b0 % 16 + 1) / 16.0F;
               float f4 = (float)(b0 / 16 + 1) / 16.0F;
               Matrix4f matrix4f1 = matrixStack.getLast().getMatrix();
               float f5 = -0.001F;
               IVertexBuilder ivertexbuilder1 = buffer.getBuffer(MapItemRenderer.MAP_ICONS_TYPE);
               ivertexbuilder1.pos(matrix4f1, -1.0F, 1.0F, (float)k * -0.001F).color(255, 255, 255, 255).tex(f1, f2).lightmap(packedLight).endVertex();
               ivertexbuilder1.pos(matrix4f1, 1.0F, 1.0F, (float)k * -0.001F).color(255, 255, 255, 255).tex(f3, f2).lightmap(packedLight).endVertex();
               ivertexbuilder1.pos(matrix4f1, 1.0F, -1.0F, (float)k * -0.001F).color(255, 255, 255, 255).tex(f3, f4).lightmap(packedLight).endVertex();
               ivertexbuilder1.pos(matrix4f1, -1.0F, -1.0F, (float)k * -0.001F).color(255, 255, 255, 255).tex(f1, f4).lightmap(packedLight).endVertex();
               matrixStack.pop();
               if (mapdecoration.getCustomName() != null) {
                  FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
                  ITextComponent itextcomponent = mapdecoration.getCustomName();
                  float f6 = (float)fontrenderer.getStringPropertyWidth(itextcomponent);
                  float f7 = MathHelper.clamp(25.0F / f6, 0.0F, 6.0F / 9.0F);
                  matrixStack.push();
                  matrixStack.translate((double)(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F - f6 * f7 / 2.0F), (double)(0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F + 4.0F), (double)-0.025F);
                  matrixStack.scale(f7, f7, 1.0F);
                  matrixStack.translate(0.0D, 0.0D, (double)-0.1F);
                  fontrenderer.func_243247_a(itextcomponent, 0.0F, 0.0F, -1, false, matrixStack.getLast().getMatrix(), buffer, false, Integer.MIN_VALUE, packedLight);
                  matrixStack.pop();
               }

               ++k;
            }
         }

      }

      public void close() {
         this.mapTexture.close();
      }
   }
}
