package net.minecraft.client.gui.widget.list;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OptionsRowList extends AbstractOptionList<OptionsRowList.Row> {
   public OptionsRowList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
      super(mc, width, height, top, bottom, itemHeight);
      this.centerListVertically = false;
   }

   public int addOption(AbstractOption option) {
      return this.addEntry(OptionsRowList.Row.create(this.minecraft.gameSettings, this.width, option));
   }

   public void addOption(AbstractOption leftOption, @Nullable AbstractOption rightOption) {
      this.addEntry(OptionsRowList.Row.create(this.minecraft.gameSettings, this.width, leftOption, rightOption));
   }

   public void addOptions(AbstractOption[] options) {
      for(int i = 0; i < options.length; i += 2) {
         this.addOption(options[i], i < options.length - 1 ? options[i + 1] : null);
      }

   }

   public int getRowWidth() {
      return 400;
   }

   protected int getScrollbarPosition() {
      return super.getScrollbarPosition() + 32;
   }

   @Nullable
   public Widget func_243271_b(AbstractOption p_243271_1_) {
      for(OptionsRowList.Row optionsrowlist$row : this.getEventListeners()) {
         for(Widget widget : optionsrowlist$row.widgets) {
            if (widget instanceof OptionButton && ((OptionButton)widget).getOptions() == p_243271_1_) {
               return widget;
            }
         }
      }

      return null;
   }

   public Optional<Widget> getWidget(double mouseX, double mouseY) {
      for(OptionsRowList.Row optionsrowlist$row : this.getEventListeners()) {
         for(Widget widget : optionsrowlist$row.widgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
               return Optional.of(widget);
            }
         }
      }

      return Optional.empty();
   }

   @OnlyIn(Dist.CLIENT)
   public static class Row extends AbstractOptionList.Entry<OptionsRowList.Row> {
      private final List<Widget> widgets;

      private Row(List<Widget> widgetsIn) {
         this.widgets = widgetsIn;
      }

      /**
       * Creates an options row with button for the specified option
       */
      public static OptionsRowList.Row create(GameSettings settings, int guiWidth, AbstractOption option) {
         return new OptionsRowList.Row(ImmutableList.of(option.createWidget(settings, guiWidth / 2 - 155, 0, 310)));
      }

      /**
       * Creates an options row with 1 or 2 buttons for specified options
       */
      public static OptionsRowList.Row create(GameSettings settings, int guiWidth, AbstractOption leftOption, @Nullable AbstractOption rightOption) {
         Widget widget = leftOption.createWidget(settings, guiWidth / 2 - 155, 0, 150);
         return rightOption == null ? new OptionsRowList.Row(ImmutableList.of(widget)) : new OptionsRowList.Row(ImmutableList.of(widget, rightOption.createWidget(settings, guiWidth / 2 - 155 + 160, 0, 150)));
      }

      public void render(MatrixStack matrixStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
         this.widgets.forEach((widget) -> {
            widget.y = top;
            widget.render(matrixStack, mouseX, mouseY, partialTicks);
         });
      }

      public List<? extends IGuiEventListener> getEventListeners() {
         return this.widgets;
      }
   }
}