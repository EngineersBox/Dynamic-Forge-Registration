package net.minecraft.client.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IBidiRenderer {
   IBidiRenderer field_243257_a = new IBidiRenderer() {
      public int func_241863_a(MatrixStack p_241863_1_, int p_241863_2_, int y) {
         return y;
      }

      public int func_241864_a(MatrixStack matrixStack, int x, int y, int p_241864_4_, int color) {
         return y;
      }

      public int func_241865_b(MatrixStack matrixStack, int x, int y, int p_241865_4_, int p_241865_5_) {
         return y;
      }

      public int func_241866_c(MatrixStack p_241866_1_, int x, int y, int p_241866_4_, int backgroundColor) {
         return y;
      }

      public int func_241862_a() {
         return 0;
      }
   };

   static IBidiRenderer func_243258_a(FontRenderer fontRenderer, ITextProperties text, int x) {
      return func_243262_b(fontRenderer, fontRenderer.trimStringToWidth(text, x).stream().map((p_243264_1_) -> {
         return new IBidiRenderer.Entry(p_243264_1_, fontRenderer.func_243245_a(p_243264_1_));
      }).collect(ImmutableList.toImmutableList()));
   }

   static IBidiRenderer func_243259_a(FontRenderer fontRenderer, ITextProperties text, int p_243259_2_, int maxLength) {
      return func_243262_b(fontRenderer, fontRenderer.trimStringToWidth(text, p_243259_2_).stream().limit((long)maxLength).map((p_243263_1_) -> {
         return new IBidiRenderer.Entry(p_243263_1_, fontRenderer.func_243245_a(p_243263_1_));
      }).collect(ImmutableList.toImmutableList()));
   }

   static IBidiRenderer func_243260_a(FontRenderer fontRenderer, ITextComponent... texts) {
      return func_243262_b(fontRenderer, Arrays.stream(texts).map(ITextComponent::func_241878_f).map((p_243261_1_) -> {
         return new IBidiRenderer.Entry(p_243261_1_, fontRenderer.func_243245_a(p_243261_1_));
      }).collect(ImmutableList.toImmutableList()));
   }

   static IBidiRenderer func_243262_b(final FontRenderer fontRenderer, final List<IBidiRenderer.Entry> bidiRendererEntries) {
      return bidiRendererEntries.isEmpty() ? field_243257_a : new IBidiRenderer() {
         public int func_241863_a(MatrixStack p_241863_1_, int p_241863_2_, int y) {
            return this.func_241864_a(p_241863_1_, p_241863_2_, y, 9, 16777215);
         }

         public int func_241864_a(MatrixStack matrixStack, int x, int y, int p_241864_4_, int color) {
            int i = y;

            for(IBidiRenderer.Entry ibidirenderer$entry : bidiRendererEntries) {
               fontRenderer.drawTextWithShadow(matrixStack, ibidirenderer$entry.processor, (float)(x - ibidirenderer$entry.field_243268_b / 2), (float)i, color);
               i += p_241864_4_;
            }

            return i;
         }

         public int func_241865_b(MatrixStack matrixStack, int x, int y, int p_241865_4_, int p_241865_5_) {
            int i = y;

            for(IBidiRenderer.Entry ibidirenderer$entry : bidiRendererEntries) {
               fontRenderer.drawTextWithShadow(matrixStack, ibidirenderer$entry.processor, (float)x, (float)i, p_241865_5_);
               i += p_241865_4_;
            }

            return i;
         }

         public int func_241866_c(MatrixStack p_241866_1_, int x, int y, int p_241866_4_, int backgroundColor) {
            int i = y;

            for(IBidiRenderer.Entry ibidirenderer$entry : bidiRendererEntries) {
               fontRenderer.func_238422_b_(p_241866_1_, ibidirenderer$entry.processor, (float)x, (float)i, backgroundColor);
               i += p_241866_4_;
            }

            return i;
         }

         public int func_241862_a() {
            return bidiRendererEntries.size();
         }
      };
   }

   int func_241863_a(MatrixStack p_241863_1_, int p_241863_2_, int y);

   int func_241864_a(MatrixStack matrixStack, int x, int y, int p_241864_4_, int color);

   int func_241865_b(MatrixStack matrixStack, int x, int y, int p_241865_4_, int p_241865_5_);

   int func_241866_c(MatrixStack p_241866_1_, int x, int y, int p_241866_4_, int backgroundColor);

   int func_241862_a();

   @OnlyIn(Dist.CLIENT)
   public static class Entry {
      private final IReorderingProcessor processor;
      private final int field_243268_b;

      private Entry(IReorderingProcessor processor, int maxLength) {
         this.processor = processor;
         this.field_243268_b = maxLength;
      }
   }
}