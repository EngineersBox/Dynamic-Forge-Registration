package net.minecraft.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class RenderState {
   protected final String name;
   protected Runnable setupTask;
   private final Runnable clearTask;
   protected static final RenderState.TransparencyState NO_TRANSPARENCY = new RenderState.TransparencyState("no_transparency", () -> {
      RenderSystem.disableBlend();
   }, () -> {
   });
   protected static final RenderState.TransparencyState ADDITIVE_TRANSPARENCY = new RenderState.TransparencyState("additive_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final RenderState.TransparencyState LIGHTNING_TRANSPARENCY = new RenderState.TransparencyState("lightning_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final RenderState.TransparencyState GLINT_TRANSPARENCY = new RenderState.TransparencyState("glint_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final RenderState.TransparencyState CRUMBLING_TRANSPARENCY = new RenderState.TransparencyState("crumbling_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = new RenderState.TransparencyState("translucent_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final RenderState.AlphaState ZERO_ALPHA = new RenderState.AlphaState(0.0F);
   protected static final RenderState.AlphaState DEFAULT_ALPHA = new RenderState.AlphaState(0.003921569F);
   protected static final RenderState.AlphaState HALF_ALPHA = new RenderState.AlphaState(0.5F);
   protected static final RenderState.ShadeModelState SHADE_DISABLED = new RenderState.ShadeModelState(false);
   protected static final RenderState.ShadeModelState SHADE_ENABLED = new RenderState.ShadeModelState(true);
   protected static final RenderState.TextureState BLOCK_SHEET_MIPPED = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, true);
   protected static final RenderState.TextureState BLOCK_SHEET = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false);
   protected static final RenderState.TextureState NO_TEXTURE = new RenderState.TextureState();
   protected static final RenderState.TexturingState DEFAULT_TEXTURING = new RenderState.TexturingState("default_texturing", () -> {
   }, () -> {
   });
   protected static final RenderState.TexturingState OUTLINE_TEXTURING = new RenderState.TexturingState("outline_texturing", () -> {
      RenderSystem.setupOutline();
   }, () -> {
      RenderSystem.teardownOutline();
   });
   protected static final RenderState.TexturingState GLINT_TEXTURING = new RenderState.TexturingState("glint_texturing", () -> {
      setupGlintTexturing(8.0F);
   }, () -> {
      RenderSystem.matrixMode(5890);
      RenderSystem.popMatrix();
      RenderSystem.matrixMode(5888);
   });
   protected static final RenderState.TexturingState ENTITY_GLINT_TEXTURING = new RenderState.TexturingState("entity_glint_texturing", () -> {
      setupGlintTexturing(0.16F);
   }, () -> {
      RenderSystem.matrixMode(5890);
      RenderSystem.popMatrix();
      RenderSystem.matrixMode(5888);
   });
   protected static final RenderState.LightmapState LIGHTMAP_ENABLED = new RenderState.LightmapState(true);
   protected static final RenderState.LightmapState LIGHTMAP_DISABLED = new RenderState.LightmapState(false);
   protected static final RenderState.OverlayState OVERLAY_ENABLED = new RenderState.OverlayState(true);
   protected static final RenderState.OverlayState OVERLAY_DISABLED = new RenderState.OverlayState(false);
   protected static final RenderState.DiffuseLightingState DIFFUSE_LIGHTING_ENABLED = new RenderState.DiffuseLightingState(true);
   protected static final RenderState.DiffuseLightingState DIFFUSE_LIGHTING_DISABLED = new RenderState.DiffuseLightingState(false);
   protected static final RenderState.CullState CULL_ENABLED = new RenderState.CullState(true);
   protected static final RenderState.CullState CULL_DISABLED = new RenderState.CullState(false);
   protected static final RenderState.DepthTestState DEPTH_ALWAYS = new RenderState.DepthTestState("always", 519);
   protected static final RenderState.DepthTestState DEPTH_EQUAL = new RenderState.DepthTestState("==", 514);
   protected static final RenderState.DepthTestState DEPTH_LEQUAL = new RenderState.DepthTestState("<=", 515);
   protected static final RenderState.WriteMaskState COLOR_DEPTH_WRITE = new RenderState.WriteMaskState(true, true);
   protected static final RenderState.WriteMaskState COLOR_WRITE = new RenderState.WriteMaskState(true, false);
   protected static final RenderState.WriteMaskState DEPTH_WRITE = new RenderState.WriteMaskState(false, true);
   protected static final RenderState.LayerState NO_LAYERING = new RenderState.LayerState("no_layering", () -> {
   }, () -> {
   });
   protected static final RenderState.LayerState POLYGON_OFFSET_LAYERING = new RenderState.LayerState("polygon_offset_layering", () -> {
      RenderSystem.polygonOffset(-1.0F, -10.0F);
      RenderSystem.enablePolygonOffset();
   }, () -> {
      RenderSystem.polygonOffset(0.0F, 0.0F);
      RenderSystem.disablePolygonOffset();
   });
   protected static final RenderState.LayerState VIEW_OFFSET_Z_LAYERING = new RenderState.LayerState("view_offset_z_layering", () -> {
      RenderSystem.pushMatrix();
      RenderSystem.scalef(0.99975586F, 0.99975586F, 0.99975586F);
   }, RenderSystem::popMatrix);
   protected static final RenderState.FogState NO_FOG = new RenderState.FogState("no_fog", () -> {
   }, () -> {
   });
   protected static final RenderState.FogState FOG = new RenderState.FogState("fog", () -> {
      FogRenderer.applyFog();
      RenderSystem.enableFog();
   }, () -> {
      RenderSystem.disableFog();
   });
   protected static final RenderState.FogState BLACK_FOG = new RenderState.FogState("black_fog", () -> {
      RenderSystem.fog(2918, 0.0F, 0.0F, 0.0F, 1.0F);
      RenderSystem.enableFog();
   }, () -> {
      FogRenderer.applyFog();
      RenderSystem.disableFog();
   });
   protected static final RenderState.TargetState MAIN_TARGET = new RenderState.TargetState("main_target", () -> {
   }, () -> {
   });
   protected static final RenderState.TargetState OUTLINE_TARGET = new RenderState.TargetState("outline_target", () -> {
      Minecraft.getInstance().worldRenderer.getEntityOutlineFramebuffer().bindFramebuffer(false);
   }, () -> {
      Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
   });
   protected static final RenderState.TargetState TRANSLUCENT_TARGET = new RenderState.TargetState("translucent_target", () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().worldRenderer.getTranslucentFrameBuffer().bindFramebuffer(false);
      }

   }, () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
      }

   });
   protected static final RenderState.TargetState PARTICLES_TARGET = new RenderState.TargetState("particles_target", () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().worldRenderer.getParticleFrameBuffer().bindFramebuffer(false);
      }

   }, () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
      }

   });
   protected static final RenderState.TargetState WEATHER_TARGET = new RenderState.TargetState("weather_target", () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().worldRenderer.getWeatherFrameBuffer().bindFramebuffer(false);
      }

   }, () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
      }

   });
   protected static final RenderState.TargetState CLOUDS_TARGET = new RenderState.TargetState("clouds_target", () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().worldRenderer.getCloudFrameBuffer().bindFramebuffer(false);
      }

   }, () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
      }

   });
   protected static final RenderState.TargetState ITEM_ENTITY_TARGET = new RenderState.TargetState("item_entity_target", () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().worldRenderer.getItemEntityFrameBuffer().bindFramebuffer(false);
      }

   }, () -> {
      if (Minecraft.isFabulousGraphicsEnabled()) {
         Minecraft.getInstance().getFramebuffer().bindFramebuffer(false);
      }

   });
   protected static final RenderState.LineState DEFAULT_LINE = new RenderState.LineState(OptionalDouble.of(1.0D));

   public RenderState(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
      this.name = nameIn;
      this.setupTask = setupTaskIn;
      this.clearTask = clearTaskIn;
   }

   public void setupRenderState() {
      this.setupTask.run();
   }

   public void clearRenderState() {
      this.clearTask.run();
   }

   public boolean equals(@Nullable Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
         RenderState renderstate = (RenderState)p_equals_1_;
         return this.name.equals(renderstate.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return this.name;
   }

   private static void setupGlintTexturing(float scaleIn) {
      RenderSystem.matrixMode(5890);
      RenderSystem.pushMatrix();
      RenderSystem.loadIdentity();
      long i = Util.milliTime() * 8L;
      float f = (float)(i % 110000L) / 110000.0F;
      float f1 = (float)(i % 30000L) / 30000.0F;
      RenderSystem.translatef(-f, f1, 0.0F);
      RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
      RenderSystem.scalef(scaleIn, scaleIn, scaleIn);
      RenderSystem.matrixMode(5888);
   }

   @OnlyIn(Dist.CLIENT)
   public static class AlphaState extends RenderState {
      private final float ref;

      public AlphaState(float refIn) {
         super("alpha", () -> {
            if (refIn > 0.0F) {
               RenderSystem.enableAlphaTest();
               RenderSystem.alphaFunc(516, refIn);
            } else {
               RenderSystem.disableAlphaTest();
            }

         }, () -> {
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultAlphaFunc();
         });
         this.ref = refIn;
      }

      public boolean equals(@Nullable Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            if (!super.equals(p_equals_1_)) {
               return false;
            } else {
               return this.ref == ((RenderState.AlphaState)p_equals_1_).ref;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(super.hashCode(), this.ref);
      }

      public String toString() {
         return this.name + '[' + this.ref + ']';
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class BooleanState extends RenderState {
      private final boolean enabled;

      public BooleanState(String name, Runnable setupTask, Runnable clearTask, boolean enabled) {
         super(name, setupTask, clearTask);
         this.enabled = enabled;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.BooleanState renderstate$booleanstate = (RenderState.BooleanState)p_equals_1_;
            return this.enabled == renderstate$booleanstate.enabled;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Boolean.hashCode(this.enabled);
      }

      public String toString() {
         return this.name + '[' + this.enabled + ']';
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class CullState extends RenderState.BooleanState {
      public CullState(boolean enabled) {
         super("cull", () -> {
            if (!enabled) {
               RenderSystem.disableCull();
            }

         }, () -> {
            if (!enabled) {
               RenderSystem.enableCull();
            }

         }, enabled);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class DepthTestState extends RenderState {
      private final String depthName;
      private final int func;

      public DepthTestState(String depthName, int func) {
         super("depth_test", () -> {
            if (func != 519) {
               RenderSystem.enableDepthTest();
               RenderSystem.depthFunc(func);
            }

         }, () -> {
            if (func != 519) {
               RenderSystem.disableDepthTest();
               RenderSystem.depthFunc(515);
            }

         });
         this.depthName = depthName;
         this.func = func;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.DepthTestState renderstate$depthteststate = (RenderState.DepthTestState)p_equals_1_;
            return this.func == renderstate$depthteststate.func;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Integer.hashCode(this.func);
      }

      public String toString() {
         return this.name + '[' + this.depthName + ']';
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class DiffuseLightingState extends RenderState.BooleanState {
      public DiffuseLightingState(boolean enabled) {
         super("diffuse_lighting", () -> {
            if (enabled) {
               RenderHelper.enableStandardItemLighting();
            }

         }, () -> {
            if (enabled) {
               RenderHelper.disableStandardItemLighting();
            }

         }, enabled);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class FogState extends RenderState {
      public FogState(String name, Runnable setupTask, Runnable clearTask) {
         super(name, setupTask, clearTask);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class LayerState extends RenderState {
      public LayerState(String name, Runnable setupTask, Runnable clearTask) {
         super(name, setupTask, clearTask);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class LightmapState extends RenderState.BooleanState {
      public LightmapState(boolean enabled) {
         super("lightmap", () -> {
            if (enabled) {
               Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
            }

         }, () -> {
            if (enabled) {
               Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
            }

         }, enabled);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class LineState extends RenderState {
      private final OptionalDouble width;

      public LineState(OptionalDouble width) {
         super("line_width", () -> {
            if (!Objects.equals(width, OptionalDouble.of(1.0D))) {
               if (width.isPresent()) {
                  RenderSystem.lineWidth((float)width.getAsDouble());
               } else {
                  RenderSystem.lineWidth(Math.max(2.5F, (float)Minecraft.getInstance().getMainWindow().getFramebufferWidth() / 1920.0F * 2.5F));
               }
            }

         }, () -> {
            if (!Objects.equals(width, OptionalDouble.of(1.0D))) {
               RenderSystem.lineWidth(1.0F);
            }

         });
         this.width = width;
      }

      public boolean equals(@Nullable Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            return !super.equals(p_equals_1_) ? false : Objects.equals(this.width, ((RenderState.LineState)p_equals_1_).width);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(super.hashCode(), this.width);
      }

      public String toString() {
         return this.name + '[' + (this.width.isPresent() ? this.width.getAsDouble() : "window_scale") + ']';
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static final class OffsetTexturingState extends RenderState.TexturingState {
      private final float offsetU;
      private final float offsetV;

      public OffsetTexturingState(float offsetU, float offsetV) {
         super("offset_texturing", () -> {
            RenderSystem.matrixMode(5890);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.translatef(offsetU, offsetV, 0.0F);
            RenderSystem.matrixMode(5888);
         }, () -> {
            RenderSystem.matrixMode(5890);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
         });
         this.offsetU = offsetU;
         this.offsetV = offsetV;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.OffsetTexturingState renderstate$offsettexturingstate = (RenderState.OffsetTexturingState)p_equals_1_;
            return Float.compare(renderstate$offsettexturingstate.offsetU, this.offsetU) == 0 && Float.compare(renderstate$offsettexturingstate.offsetV, this.offsetV) == 0;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(this.offsetU, this.offsetV);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class OverlayState extends RenderState.BooleanState {
      public OverlayState(boolean enabled) {
         super("overlay", () -> {
            if (enabled) {
               Minecraft.getInstance().gameRenderer.getOverlayTexture().setupOverlayColor();
            }

         }, () -> {
            if (enabled) {
               Minecraft.getInstance().gameRenderer.getOverlayTexture().teardownOverlayColor();
            }

         }, enabled);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static final class PortalTexturingState extends RenderState.TexturingState {
      private final int iteration;

      public PortalTexturingState(int iteration) {
         super("portal_texturing", () -> {
            RenderSystem.matrixMode(5890);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.5F, 0.5F, 0.0F);
            RenderSystem.scalef(0.5F, 0.5F, 1.0F);
            RenderSystem.translatef(17.0F / (float)iteration, (2.0F + (float)iteration / 1.5F) * ((float)(Util.milliTime() % 800000L) / 800000.0F), 0.0F);
            RenderSystem.rotatef(((float)(iteration * iteration) * 4321.0F + (float)iteration * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            RenderSystem.scalef(4.5F - (float)iteration / 4.0F, 4.5F - (float)iteration / 4.0F, 1.0F);
            RenderSystem.mulTextureByProjModelView();
            RenderSystem.matrixMode(5888);
            RenderSystem.setupEndPortalTexGen();
         }, () -> {
            RenderSystem.matrixMode(5890);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
            RenderSystem.clearTexGen();
         });
         this.iteration = iteration;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.PortalTexturingState renderstate$portaltexturingstate = (RenderState.PortalTexturingState)p_equals_1_;
            return this.iteration == renderstate$portaltexturingstate.iteration;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Integer.hashCode(this.iteration);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class ShadeModelState extends RenderState {
      private final boolean smooth;

      public ShadeModelState(boolean smooth) {
         super("shade_model", () -> {
            RenderSystem.shadeModel(smooth ? 7425 : 7424);
         }, () -> {
            RenderSystem.shadeModel(7424);
         });
         this.smooth = smooth;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.ShadeModelState renderstate$shademodelstate = (RenderState.ShadeModelState)p_equals_1_;
            return this.smooth == renderstate$shademodelstate.smooth;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Boolean.hashCode(this.smooth);
      }

      public String toString() {
         return this.name + '[' + (this.smooth ? "smooth" : "flat") + ']';
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class TargetState extends RenderState {
      public TargetState(String name, Runnable setupTask, Runnable clearTask) {
         super(name, setupTask, clearTask);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class TextureState extends RenderState {
      private final Optional<ResourceLocation> texture;
      protected boolean blur;
      protected boolean mipmap;

      public TextureState(ResourceLocation texture, boolean blur, boolean mipmap) {
         super("texture", () -> {
            RenderSystem.enableTexture();
            TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
            texturemanager.bindTexture(texture);
            texturemanager.getTexture(texture).setBlurMipmapDirect(blur, mipmap);
         }, () -> {
         });
         this.texture = Optional.of(texture);
         this.blur = blur;
         this.mipmap = mipmap;
      }

      public TextureState() {
         super("texture", () -> {
            RenderSystem.disableTexture();
         }, () -> {
            RenderSystem.enableTexture();
         });
         this.texture = Optional.empty();
         this.blur = false;
         this.mipmap = false;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.TextureState renderstate$texturestate = (RenderState.TextureState)p_equals_1_;
            return this.texture.equals(renderstate$texturestate.texture) && this.blur == renderstate$texturestate.blur && this.mipmap == renderstate$texturestate.mipmap;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.texture.hashCode();
      }

      public String toString() {
         return this.name + '[' + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
      }

      protected Optional<ResourceLocation> texture() {
         return this.texture;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class TexturingState extends RenderState {
      public TexturingState(String name, Runnable setupTask, Runnable clearTask) {
         super(name, setupTask, clearTask);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class TransparencyState extends RenderState {
      public TransparencyState(String name, Runnable setupTask, Runnable clearTask) {
         super(name, setupTask, clearTask);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class WriteMaskState extends RenderState {
      private final boolean colorMask;
      private final boolean depthMask;

      public WriteMaskState(boolean colorMask, boolean depthMask) {
         super("write_mask_state", () -> {
            if (!depthMask) {
               RenderSystem.depthMask(depthMask);
            }

            if (!colorMask) {
               RenderSystem.colorMask(colorMask, colorMask, colorMask, colorMask);
            }

         }, () -> {
            if (!depthMask) {
               RenderSystem.depthMask(true);
            }

            if (!colorMask) {
               RenderSystem.colorMask(true, true, true, true);
            }

         });
         this.colorMask = colorMask;
         this.depthMask = depthMask;
      }

      public boolean equals(Object p_equals_1_) {
         if (this == p_equals_1_) {
            return true;
         } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            RenderState.WriteMaskState renderstate$writemaskstate = (RenderState.WriteMaskState)p_equals_1_;
            return this.colorMask == renderstate$writemaskstate.colorMask && this.depthMask == renderstate$writemaskstate.depthMask;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(this.colorMask, this.depthMask);
      }

      public String toString() {
         return this.name + "[writeColor=" + this.colorMask + ", writeDepth=" + this.depthMask + ']';
      }
   }
}