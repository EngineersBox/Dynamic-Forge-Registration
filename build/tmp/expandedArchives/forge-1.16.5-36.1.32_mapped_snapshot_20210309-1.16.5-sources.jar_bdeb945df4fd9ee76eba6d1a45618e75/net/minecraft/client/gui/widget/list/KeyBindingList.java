package net.minecraft.client.gui.widget.list;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.ArrayUtils;

@OnlyIn(Dist.CLIENT)
public class KeyBindingList extends AbstractOptionList<KeyBindingList.Entry> {
   private final ControlsScreen controlsScreen;
   private int maxListLabelWidth;

   public KeyBindingList(ControlsScreen controls, Minecraft mcIn) {
      super(mcIn, controls.width + 45, controls.height, 43, controls.height - 32, 20);
      this.controlsScreen = controls;
      KeyBinding[] akeybinding = ArrayUtils.clone(mcIn.gameSettings.keyBindings);
      Arrays.sort((Object[])akeybinding);
      String s = null;

      for(KeyBinding keybinding : akeybinding) {
         String s1 = keybinding.getKeyCategory();
         if (!s1.equals(s)) {
            s = s1;
            this.addEntry(new KeyBindingList.CategoryEntry(new TranslationTextComponent(s1)));
         }

         ITextComponent itextcomponent = new TranslationTextComponent(keybinding.getKeyDescription());
         int i = mcIn.fontRenderer.getStringPropertyWidth(itextcomponent);
         if (i > this.maxListLabelWidth) {
            this.maxListLabelWidth = i;
         }

         this.addEntry(new KeyBindingList.KeyEntry(keybinding, itextcomponent));
      }

   }

   protected int getScrollbarPosition() {
      return super.getScrollbarPosition() + 15 + 20;
   }

   public int getRowWidth() {
      return super.getRowWidth() + 32;
   }

   @OnlyIn(Dist.CLIENT)
   public class CategoryEntry extends KeyBindingList.Entry {
      private final ITextComponent labelText;
      private final int labelWidth;

      public CategoryEntry(ITextComponent labelText) {
         this.labelText = labelText;
         this.labelWidth = KeyBindingList.this.minecraft.fontRenderer.getStringPropertyWidth(this.labelText);
      }

      public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
         KeyBindingList.this.minecraft.fontRenderer.drawText(matrixStack, this.labelText, (float)(KeyBindingList.this.minecraft.currentScreen.width / 2 - this.labelWidth / 2), (float)(top + height - 9 - 1), 16777215);
      }

      public boolean changeFocus(boolean focus) {
         return false;
      }

      public List<? extends IGuiEventListener> getEventListeners() {
         return Collections.emptyList();
      }
   }

   @OnlyIn(Dist.CLIENT)
   public abstract static class Entry extends AbstractOptionList.Entry<KeyBindingList.Entry> {
   }

   @OnlyIn(Dist.CLIENT)
   public class KeyEntry extends KeyBindingList.Entry {
      /** The keybinding specified for this KeyEntry */
      private final KeyBinding keybinding;
      /** The localized key description for this KeyEntry */
      private final ITextComponent keyDesc;
      private final Button btnChangeKeyBinding;
      private final Button btnReset;

      private KeyEntry(final KeyBinding keyBinding, final ITextComponent keyDesc) {
         this.keybinding = keyBinding;
         this.keyDesc = keyDesc;
         this.btnChangeKeyBinding = new Button(0, 0, 75 + 20 /*Forge: add space*/, 20, keyDesc, (button) -> {
            KeyBindingList.this.controlsScreen.buttonId = keyBinding;
         }) {
            protected IFormattableTextComponent getNarrationMessage() {
               return keyBinding.isInvalid() ? new TranslationTextComponent("narrator.controls.unbound", keyDesc) : new TranslationTextComponent("narrator.controls.bound", keyDesc, super.getNarrationMessage());
            }
         };
         this.btnReset = new Button(0, 0, 50, 20, new TranslationTextComponent("controls.reset"), (button) -> {
            keybinding.setToDefault();
            KeyBindingList.this.minecraft.gameSettings.setKeyBindingCode(keyBinding, keyBinding.getDefault());
            KeyBinding.resetKeyBindingArrayAndHash();
         }) {
            protected IFormattableTextComponent getNarrationMessage() {
               return new TranslationTextComponent("narrator.controls.reset", keyDesc);
            }
         };
      }

      public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
         boolean flag = KeyBindingList.this.controlsScreen.buttonId == this.keybinding;
         KeyBindingList.this.minecraft.fontRenderer.drawText(matrixStack, this.keyDesc, (float)(left + 90 - KeyBindingList.this.maxListLabelWidth), (float)(top + height / 2 - 9 / 2), 16777215);
         this.btnReset.x = left + 190 + 20;
         this.btnReset.y = top;
         this.btnReset.active = !this.keybinding.isDefault();
         this.btnReset.render(matrixStack, mouseX, mouseY, partialTicks);
         this.btnChangeKeyBinding.x = left + 105;
         this.btnChangeKeyBinding.y = top;
         this.btnChangeKeyBinding.setMessage(this.keybinding.func_238171_j_());
         boolean flag1 = false;
         boolean keyCodeModifierConflict = true; // less severe form of conflict, like SHIFT conflicting with SHIFT+G
         if (!this.keybinding.isInvalid()) {
            for(KeyBinding keybinding : KeyBindingList.this.minecraft.gameSettings.keyBindings) {
               if (keybinding != this.keybinding && this.keybinding.conflicts(keybinding)) {
                  flag1 = true;
                  keyCodeModifierConflict &= keybinding.hasKeyCodeModifierConflict(keybinding);
               }
            }
         }

         if (flag) {
            this.btnChangeKeyBinding.setMessage((new StringTextComponent("> ")).appendSibling(this.btnChangeKeyBinding.getMessage().deepCopy().mergeStyle(TextFormatting.YELLOW)).appendString(" <").mergeStyle(TextFormatting.YELLOW));
         } else if (flag1) {
            this.btnChangeKeyBinding.setMessage(this.btnChangeKeyBinding.getMessage().deepCopy().mergeStyle(keyCodeModifierConflict ? TextFormatting.GOLD : TextFormatting.RED));
         }

         this.btnChangeKeyBinding.render(matrixStack, mouseX, mouseY, partialTicks);
      }

      public List<? extends IGuiEventListener> getEventListeners() {
         return ImmutableList.of(this.btnChangeKeyBinding, this.btnReset);
      }

      public boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (this.btnChangeKeyBinding.mouseClicked(mouseX, mouseY, button)) {
            return true;
         } else {
            return this.btnReset.mouseClicked(mouseX, mouseY, button);
         }
      }

      public boolean mouseReleased(double mouseX, double mouseY, int button) {
         return this.btnChangeKeyBinding.mouseReleased(mouseX, mouseY, button) || this.btnReset.mouseReleased(mouseX, mouseY, button);
      }
   }
}
