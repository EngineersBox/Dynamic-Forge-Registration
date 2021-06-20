package net.minecraft.client.gui.widget.list;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IBidiRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResourcePackList extends ExtendedList<ResourcePackList.ResourcePackEntry> {
   private static final ResourceLocation RESOURCE_PACK_TEXTURES = new ResourceLocation("textures/gui/resource_packs.png");
   private static final ITextComponent field_214368_c = new TranslationTextComponent("pack.incompatible");
   private static final ITextComponent field_214369_d = new TranslationTextComponent("pack.incompatible.confirm.title");
   private final ITextComponent field_214370_e;

   public ResourcePackList(Minecraft mc, int width, int height, ITextComponent p_i241200_4_) {
      super(mc, width, height, 32, height - 55 + 4, 36);
      this.field_214370_e = p_i241200_4_;
      this.centerListVertically = false;
      this.setRenderHeader(true, (int)(9.0F * 1.5F));
   }

   protected void renderHeader(MatrixStack matrixStack, int x, int y, Tessellator tessellator) {
      ITextComponent itextcomponent = (new StringTextComponent("")).appendSibling(this.field_214370_e).mergeStyle(TextFormatting.UNDERLINE, TextFormatting.BOLD);
      this.minecraft.fontRenderer.drawText(matrixStack, itextcomponent, (float)(x + this.width / 2 - this.minecraft.fontRenderer.getStringPropertyWidth(itextcomponent) / 2), (float)Math.min(this.y0 + 3, y), 16777215);
   }

   public int getRowWidth() {
      return this.width;
   }

   protected int getScrollbarPosition() {
      return this.x1 - 6;
   }

   @OnlyIn(Dist.CLIENT)
   public static class ResourcePackEntry extends ExtendedList.AbstractListEntry<ResourcePackList.ResourcePackEntry> {
      private ResourcePackList resourcePackList;
      protected final Minecraft mc;
      protected final Screen field_214429_b;
      private final PackLoadingManager.IPack pack;
      private final IReorderingProcessor field_243407_e;
      private final IBidiRenderer field_243408_f;
      private final IReorderingProcessor field_244422_g;
      private final IBidiRenderer field_244423_h;

      public ResourcePackEntry(Minecraft mc, ResourcePackList resourcePackList, Screen p_i241201_3_, PackLoadingManager.IPack pack) {
         this.mc = mc;
         this.field_214429_b = p_i241201_3_;
         this.pack = pack;
         this.resourcePackList = resourcePackList;
         this.field_243407_e = func_244424_a(mc, pack.func_230462_b_());
         this.field_243408_f = func_244425_b(mc, pack.func_243390_f());
         this.field_244422_g = func_244424_a(mc, ResourcePackList.field_214368_c);
         this.field_244423_h = func_244425_b(mc, pack.func_230460_a_().getDescription());
      }

      private static IReorderingProcessor func_244424_a(Minecraft p_244424_0_, ITextComponent p_244424_1_) {
         int i = p_244424_0_.fontRenderer.getStringPropertyWidth(p_244424_1_);
         if (i > 157) {
            ITextProperties itextproperties = ITextProperties.func_240655_a_(p_244424_0_.fontRenderer.func_238417_a_(p_244424_1_, 157 - p_244424_0_.fontRenderer.getStringWidth("...")), ITextProperties.func_240652_a_("..."));
            return LanguageMap.getInstance().func_241870_a(itextproperties);
         } else {
            return p_244424_1_.func_241878_f();
         }
      }

      private static IBidiRenderer func_244425_b(Minecraft p_244425_0_, ITextComponent p_244425_1_) {
         return IBidiRenderer.func_243259_a(p_244425_0_.fontRenderer, p_244425_1_, 157, 2);
      }

      public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
         PackCompatibility packcompatibility = this.pack.func_230460_a_();
         if (!packcompatibility.isCompatible()) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            AbstractGui.fill(matrixStack, left - 1, top - 1, left + width - 9, top + height + 1, -8978432);
         }

         this.mc.getTextureManager().bindTexture(this.pack.func_241868_a());
         RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
         AbstractGui.blit(matrixStack, left, top, 0.0F, 0.0F, 32, 32, 32, 32);
         IReorderingProcessor ireorderingprocessor = this.field_243407_e;
         IBidiRenderer ibidirenderer = this.field_243408_f;
         if (this.func_238920_a_() && (this.mc.gameSettings.touchscreen || isMouseOver)) {
            this.mc.getTextureManager().bindTexture(ResourcePackList.RESOURCE_PACK_TEXTURES);
            AbstractGui.fill(matrixStack, left, top, left + 32, top + 32, -1601138544);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = mouseX - left;
            int j = mouseY - top;
            if (!this.pack.func_230460_a_().isCompatible()) {
               ireorderingprocessor = this.field_244422_g;
               ibidirenderer = this.field_244423_h;
            }

            if (this.pack.func_238875_m_()) {
               if (i < 32) {
                  AbstractGui.blit(matrixStack, left, top, 0.0F, 32.0F, 32, 32, 256, 256);
               } else {
                  AbstractGui.blit(matrixStack, left, top, 0.0F, 0.0F, 32, 32, 256, 256);
               }
            } else {
               if (this.pack.func_238876_n_()) {
                  if (i < 16) {
                     AbstractGui.blit(matrixStack, left, top, 32.0F, 32.0F, 32, 32, 256, 256);
                  } else {
                     AbstractGui.blit(matrixStack, left, top, 32.0F, 0.0F, 32, 32, 256, 256);
                  }
               }

               if (this.pack.func_230469_o_()) {
                  if (i < 32 && i > 16 && j < 16) {
                     AbstractGui.blit(matrixStack, left, top, 96.0F, 32.0F, 32, 32, 256, 256);
                  } else {
                     AbstractGui.blit(matrixStack, left, top, 96.0F, 0.0F, 32, 32, 256, 256);
                  }
               }

               if (this.pack.func_230470_p_()) {
                  if (i < 32 && i > 16 && j > 16) {
                     AbstractGui.blit(matrixStack, left, top, 64.0F, 32.0F, 32, 32, 256, 256);
                  } else {
                     AbstractGui.blit(matrixStack, left, top, 64.0F, 0.0F, 32, 32, 256, 256);
                  }
               }
            }
         }

         this.mc.fontRenderer.drawTextWithShadow(matrixStack, ireorderingprocessor, (float)(left + 32 + 2), (float)(top + 1), 16777215);
         ibidirenderer.func_241865_b(matrixStack, left + 32 + 2, top + 12, 10, 8421504);
      }

      private boolean func_238920_a_() {
         return !this.pack.func_230465_f_() || !this.pack.func_230466_g_();
      }

      public boolean mouseClicked(double mouseX, double mouseY, int button) {
         double d0 = mouseX - (double)this.resourcePackList.getRowLeft();
         double d1 = mouseY - (double)this.resourcePackList.getRowTop(this.resourcePackList.getEventListeners().indexOf(this));
         if (this.func_238920_a_() && d0 <= 32.0D) {
            if (this.pack.func_238875_m_()) {
               PackCompatibility packcompatibility = this.pack.func_230460_a_();
               if (packcompatibility.isCompatible()) {
                  this.pack.func_230471_h_();
               } else {
                  ITextComponent itextcomponent = packcompatibility.getConfirmMessage();
                  this.mc.displayGuiScreen(new ConfirmScreen((confirm) -> {
                     this.mc.displayGuiScreen(this.field_214429_b);
                     if (confirm) {
                        this.pack.func_230471_h_();
                     }

                  }, ResourcePackList.field_214369_d, itextcomponent));
               }

               return true;
            }

            if (d0 < 16.0D && this.pack.func_238876_n_()) {
               this.pack.func_230472_i_();
               return true;
            }

            if (d0 > 16.0D && d1 < 16.0D && this.pack.func_230469_o_()) {
               this.pack.func_230467_j_();
               return true;
            }

            if (d0 > 16.0D && d1 > 16.0D && this.pack.func_230470_p_()) {
               this.pack.func_230468_k_();
               return true;
            }
         }

         return false;
      }
   }
}