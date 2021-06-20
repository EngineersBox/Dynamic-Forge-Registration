package net.minecraft.client.gui.widget.list;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractOptionList<E extends AbstractOptionList.Entry<E>> extends AbstractList<E> {
   public AbstractOptionList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
      super(mc, width, height, top, bottom, itemHeight);
   }

   public boolean changeFocus(boolean focus) {
      boolean flag = super.changeFocus(focus);
      if (flag) {
         this.ensureVisible(this.getListener());
      }

      return flag;
   }

   protected boolean isSelectedItem(int index) {
      return false;
   }

   @OnlyIn(Dist.CLIENT)
   public abstract static class Entry<E extends AbstractOptionList.Entry<E>> extends AbstractList.AbstractListEntry<E> implements INestedGuiEventHandler {
      @Nullable
      private IGuiEventListener listener;
      private boolean dragging;

      public boolean isDragging() {
         return this.dragging;
      }

      public void setDragging(boolean dragging) {
         this.dragging = dragging;
      }

      public void setListener(@Nullable IGuiEventListener listener) {
         this.listener = listener;
      }

      @Nullable
      public IGuiEventListener getListener() {
         return this.listener;
      }
   }
}