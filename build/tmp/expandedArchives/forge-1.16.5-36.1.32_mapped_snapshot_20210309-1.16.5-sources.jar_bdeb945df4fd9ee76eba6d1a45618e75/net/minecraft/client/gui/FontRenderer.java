package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.gui.fonts.EmptyGlyph;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.IGlyph;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ICharacterConsumer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextProcessing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FontRenderer {
   private static final Vector3f FONT_OFFSET = new Vector3f(0.0F, 0.0F, 0.03F);
   public final int FONT_HEIGHT = 9;
   public final Random random = new Random();
   private final Function<ResourceLocation, Font> font;
   private final CharacterManager characterManager;

   public FontRenderer(Function<ResourceLocation, Font> font) {
      this.font = font;
      this.characterManager = new CharacterManager((charID, style) -> {
         return this.getFont(style.getFontId()).func_238557_a_(charID).getAdvance(style.getBold());
      });
   }

   private Font getFont(ResourceLocation fontLocation) {
      return this.font.apply(fontLocation);
   }

   public int drawStringWithShadow(MatrixStack matrixStack, String text, float x, float y, int color) {
      return this.renderString(text, x, y, color, matrixStack.getLast().getMatrix(), true, this.getBidiFlag());
   }

   public int drawStringWithTransparency(MatrixStack matrixStack, String text, float x, float y, int color, boolean transparency) {
      RenderSystem.enableAlphaTest();
      return this.renderString(text, x, y, color, matrixStack.getLast().getMatrix(), true, transparency);
   }

   public int drawString(MatrixStack matrixStack, String text, float x, float y, int color) {
      RenderSystem.enableAlphaTest();
      return this.renderString(text, x, y, color, matrixStack.getLast().getMatrix(), false, this.getBidiFlag());
   }

   public int drawTextWithShadow(MatrixStack matrixStack, IReorderingProcessor text, float x, float y, int color) {
      RenderSystem.enableAlphaTest();
      return this.drawText(text, x, y, color, matrixStack.getLast().getMatrix(), true);
   }

   public int drawTextWithShadow(MatrixStack matrixStack, ITextComponent text, float x, float y, int color) {
      RenderSystem.enableAlphaTest();
      return this.drawText(text.func_241878_f(), x, y, color, matrixStack.getLast().getMatrix(), true);
   }

   public int func_238422_b_(MatrixStack matrixStack, IReorderingProcessor properties, float x, float y, int color) {
      RenderSystem.enableAlphaTest();
      return this.drawText(properties, x, y, color, matrixStack.getLast().getMatrix(), false);
   }

   public int drawText(MatrixStack matrixStack, ITextComponent text, float x, float y, int color) {
      RenderSystem.enableAlphaTest();
      return this.drawText(text.func_241878_f(), x, y, color, matrixStack.getLast().getMatrix(), false);
   }

   /**
    * Apply Unicode Bidirectional Algorithm to string and return a new possibly reordered string for visual rendering.
    */
   public String bidiReorder(String text) {
      try {
         Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
         bidi.setReorderingMode(0);
         return bidi.writeReordered(2);
      } catch (ArabicShapingException arabicshapingexception) {
         return text;
      }
   }

   private int renderString(String text, float x, float y, int color, Matrix4f matrix, boolean dropShadow, boolean transparency) {
      if (text == null) {
         return 0;
      } else {
         IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
         int i = this.drawBidiString(text, x, y, color, dropShadow, matrix, irendertypebuffer$impl, false, 0, 15728880, transparency);
         irendertypebuffer$impl.finish();
         return i;
      }
   }

   private int drawText(IReorderingProcessor reorderingProcessor, float x, float y, int color, Matrix4f matrix, boolean drawShadow) {
      IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
      int i = this.drawEntityText(reorderingProcessor, x, y, color, drawShadow, matrix, irendertypebuffer$impl, false, 0, 15728880);
      irendertypebuffer$impl.finish();
      return i;
   }

   public int renderString(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, IRenderTypeBuffer buffer, boolean transparentIn, int colorBackgroundIn, int packedLight) {
      return this.drawBidiString(text, x, y, color, dropShadow, matrix, buffer, transparentIn, colorBackgroundIn, packedLight, this.getBidiFlag());
   }

   public int drawBidiString(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, IRenderTypeBuffer buffer, boolean transparency, int colorBackground, int packedLight, boolean bidiFlag) {
      return this.drawBidiStringInternal(text, x, y, color, dropShadow, matrix, buffer, transparency, colorBackground, packedLight, bidiFlag);
   }

   public int func_243247_a(ITextComponent text, float x, float y, int color, boolean p_243247_5_, Matrix4f matrix, IRenderTypeBuffer renderBuffer, boolean transparent, int colorBackground, int packedLight) {
      return this.drawEntityText(text.func_241878_f(), x, y, color, p_243247_5_, matrix, renderBuffer, transparent, colorBackground, packedLight);
   }

   public int drawEntityText(IReorderingProcessor processor, float x, float y, int color, boolean dropShadow, Matrix4f matrix, IRenderTypeBuffer buffer, boolean transparent, int colorBackground, int packedLight) {
      return this.drawText(processor, x, y, color, dropShadow, matrix, buffer, transparent, colorBackground, packedLight);
   }

   private static int fixAlpha(int color) {
      return (color & -67108864) == 0 ? color | -16777216 : color;
   }

   private int drawBidiStringInternal(String text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, IRenderTypeBuffer buffer, boolean transparent, int colorBackground, int packedLight, boolean bidiFlag) {
      if (bidiFlag) {
         text = this.bidiReorder(text);
      }

      color = fixAlpha(color);
      Matrix4f matrix4f = matrix.copy();
      if (dropShadow) {
         this.renderStringAtPos(text, x, y, color, true, matrix, buffer, transparent, colorBackground, packedLight);
         matrix4f.translate(FONT_OFFSET);
      }

      x = this.renderStringAtPos(text, x, y, color, false, matrix4f, buffer, transparent, colorBackground, packedLight);
      return (int)x + (dropShadow ? 1 : 0);
   }

   private int drawText(IReorderingProcessor processor, float x, float y, int color, boolean drawShadow, Matrix4f matrix, IRenderTypeBuffer buffer, boolean transparent, int colorBackground, int packedLight) {
      color = fixAlpha(color);
      Matrix4f matrix4f = matrix.copy();
      if (drawShadow) {
         this.func_238426_c_(processor, x, y, color, true, matrix, buffer, transparent, colorBackground, packedLight);
         matrix4f.translate(FONT_OFFSET);
      }

      x = this.func_238426_c_(processor, x, y, color, false, matrix4f, buffer, transparent, colorBackground, packedLight);
      return (int)x + (drawShadow ? 1 : 0);
   }

   private float renderStringAtPos(String text, float x, float y, int color, boolean isShadow, Matrix4f matrix, IRenderTypeBuffer buffer, boolean isTransparent, int colorBackgroundIn, int packedLight) {
      FontRenderer.CharacterRenderer fontrenderer$characterrenderer = new FontRenderer.CharacterRenderer(buffer, x, y, color, isShadow, matrix, isTransparent, packedLight);
      TextProcessing.func_238346_c_(text, Style.EMPTY, fontrenderer$characterrenderer);
      return fontrenderer$characterrenderer.func_238441_a_(colorBackgroundIn, x);
   }

   private float func_238426_c_(IReorderingProcessor p_238426_1_, float x, float y, int color, boolean p_238426_5_, Matrix4f matrix, IRenderTypeBuffer buffer, boolean transparent, int colorBackground, int packedLight) {
      FontRenderer.CharacterRenderer fontrenderer$characterrenderer = new FontRenderer.CharacterRenderer(buffer, x, y, color, p_238426_5_, matrix, transparent, packedLight);
      p_238426_1_.accept(fontrenderer$characterrenderer);
      return fontrenderer$characterrenderer.func_238441_a_(colorBackground, x);
   }

   private void drawGlyph(TexturedGlyph glyphIn, boolean boldIn, boolean italicIn, float boldOffsetIn, float xIn, float yIn, Matrix4f matrix, IVertexBuilder bufferIn, float redIn, float greenIn, float blueIn, float alphaIn, int packedLight) {
      glyphIn.render(italicIn, xIn, yIn, matrix, bufferIn, redIn, greenIn, blueIn, alphaIn, packedLight);
      if (boldIn) {
         glyphIn.render(italicIn, xIn + boldOffsetIn, yIn, matrix, bufferIn, redIn, greenIn, blueIn, alphaIn, packedLight);
      }

   }

   /**
    * Returns the width of this string. Equivalent of FontMetrics.stringWidth(String s).
    */
   public int getStringWidth(String text) {
      return MathHelper.ceil(this.characterManager.func_238350_a_(text));
   }

   public int getStringPropertyWidth(ITextProperties properties) {
      return MathHelper.ceil(this.characterManager.func_238356_a_(properties));
   }

   public int func_243245_a(IReorderingProcessor processor) {
      return MathHelper.ceil(this.characterManager.func_243238_a(processor));
   }

   public String getLineScrollOffset(String text, int maxLength, boolean p_238413_3_) {
      return p_238413_3_ ? this.characterManager.func_238364_c_(text, maxLength, Style.EMPTY) : this.characterManager.func_238361_b_(text, maxLength, Style.EMPTY);
   }

   public String trimStringToWidth(String text, int maxLength) {
      return this.characterManager.func_238361_b_(text, maxLength, Style.EMPTY);
   }

   public ITextProperties func_238417_a_(ITextProperties properties, int maxLength) {
      return this.characterManager.func_238358_a_(properties, maxLength, Style.EMPTY);
   }

   public void func_238418_a_(ITextProperties text, int x, int y, int maxLength, int color) {
      Matrix4f matrix4f = TransformationMatrix.identity().getMatrix();

      for(IReorderingProcessor ireorderingprocessor : this.trimStringToWidth(text, maxLength)) {
         this.drawText(ireorderingprocessor, (float)x, (float)y, color, matrix4f, false);
         y += 9;
      }

   }

   /**
    * Returns the height (in pixels) of the given string if it is wordwrapped to the given max width.
    */
   public int getWordWrappedHeight(String str, int maxLength) {
      return 9 * this.characterManager.func_238365_g_(str, maxLength, Style.EMPTY).size();
   }

   public List<IReorderingProcessor> trimStringToWidth(ITextProperties p_238425_1_, int p_238425_2_) {
      return LanguageMap.getInstance().func_244260_a(this.characterManager.func_238362_b_(p_238425_1_, p_238425_2_, Style.EMPTY));
   }

   /**
    * Get bidiFlag that controls if the Unicode Bidirectional Algorithm should be run before rendering any string
    */
   public boolean getBidiFlag() {
      return LanguageMap.getInstance().func_230505_b_();
   }

   public CharacterManager getCharacterManager() {
      return this.characterManager;
   }

   @OnlyIn(Dist.CLIENT)
   class CharacterRenderer implements ICharacterConsumer {
      final IRenderTypeBuffer buffer;
      private final boolean isShadow;
      private final float colorScale;
      private final float field_238431_e_;
      private final float field_238432_f_;
      private final float field_238433_g_;
      private final float field_238434_h_;
      private final Matrix4f matrix;
      private final boolean transparent;
      private final int packedLight;
      private float x;
      private float y;
      @Nullable
      private List<TexturedGlyph.Effect> texturedGlyphEffects;

      private void addTexturedGlyphEffect(TexturedGlyph.Effect effect) {
         if (this.texturedGlyphEffects == null) {
            this.texturedGlyphEffects = Lists.newArrayList();
         }

         this.texturedGlyphEffects.add(effect);
      }

      public CharacterRenderer(IRenderTypeBuffer buffer, float x, float y, int color, boolean isShadow, Matrix4f matrix, boolean transparent, int packedLight) {
         this.buffer = buffer;
         this.x = x;
         this.y = y;
         this.isShadow = isShadow;
         this.colorScale = isShadow ? 0.25F : 1.0F;
         this.field_238431_e_ = (float)(color >> 16 & 255) / 255.0F * this.colorScale;
         this.field_238432_f_ = (float)(color >> 8 & 255) / 255.0F * this.colorScale;
         this.field_238433_g_ = (float)(color & 255) / 255.0F * this.colorScale;
         this.field_238434_h_ = (float)(color >> 24 & 255) / 255.0F;
         this.matrix = matrix;
         this.transparent = transparent;
         this.packedLight = packedLight;
      }

      public boolean accept(int p_accept_1_, Style p_accept_2_, int p_accept_3_) {
         Font font = FontRenderer.this.getFont(p_accept_2_.getFontId());
         IGlyph iglyph = font.func_238557_a_(p_accept_3_);
         TexturedGlyph texturedglyph = p_accept_2_.getObfuscated() && p_accept_3_ != 32 ? font.obfuscate(iglyph) : font.func_238559_b_(p_accept_3_);
         boolean flag = p_accept_2_.getBold();
         float f3 = this.field_238434_h_;
         Color color = p_accept_2_.getColor();
         float f;
         float f1;
         float f2;
         if (color != null) {
            int i = color.getColor();
            f = (float)(i >> 16 & 255) / 255.0F * this.colorScale;
            f1 = (float)(i >> 8 & 255) / 255.0F * this.colorScale;
            f2 = (float)(i & 255) / 255.0F * this.colorScale;
         } else {
            f = this.field_238431_e_;
            f1 = this.field_238432_f_;
            f2 = this.field_238433_g_;
         }

         if (!(texturedglyph instanceof EmptyGlyph)) {
            float f5 = flag ? iglyph.getBoldOffset() : 0.0F;
            float f4 = this.isShadow ? iglyph.getShadowOffset() : 0.0F;
            IVertexBuilder ivertexbuilder = this.buffer.getBuffer(texturedglyph.getRenderType(this.transparent));
            FontRenderer.this.drawGlyph(texturedglyph, flag, p_accept_2_.getItalic(), f5, this.x + f4, this.y + f4, this.matrix, ivertexbuilder, f, f1, f2, f3, this.packedLight);
         }

         float f6 = iglyph.getAdvance(flag);
         float f7 = this.isShadow ? 1.0F : 0.0F;
         if (p_accept_2_.getStrikethrough()) {
            this.addTexturedGlyphEffect(new TexturedGlyph.Effect(this.x + f7 - 1.0F, this.y + f7 + 4.5F, this.x + f7 + f6, this.y + f7 + 4.5F - 1.0F, 0.01F, f, f1, f2, f3));
         }

         if (p_accept_2_.getUnderlined()) {
            this.addTexturedGlyphEffect(new TexturedGlyph.Effect(this.x + f7 - 1.0F, this.y + f7 + 9.0F, this.x + f7 + f6, this.y + f7 + 9.0F - 1.0F, 0.01F, f, f1, f2, f3));
         }

         this.x += f6;
         return true;
      }

      public float func_238441_a_(int color, float x) {
         if (color != 0) {
            float f = (float)(color >> 24 & 255) / 255.0F;
            float f1 = (float)(color >> 16 & 255) / 255.0F;
            float f2 = (float)(color >> 8 & 255) / 255.0F;
            float f3 = (float)(color & 255) / 255.0F;
            this.addTexturedGlyphEffect(new TexturedGlyph.Effect(x - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, f1, f2, f3, f));
         }

         if (this.texturedGlyphEffects != null) {
            TexturedGlyph texturedglyph = FontRenderer.this.getFont(Style.DEFAULT_FONT).getWhiteGlyph();
            IVertexBuilder ivertexbuilder = this.buffer.getBuffer(texturedglyph.getRenderType(this.transparent));

            for(TexturedGlyph.Effect texturedglyph$effect : this.texturedGlyphEffects) {
               texturedglyph.renderEffect(texturedglyph$effect, this.matrix, ivertexbuilder, this.packedLight);
            }
         }

         return this.x;
      }
   }
}