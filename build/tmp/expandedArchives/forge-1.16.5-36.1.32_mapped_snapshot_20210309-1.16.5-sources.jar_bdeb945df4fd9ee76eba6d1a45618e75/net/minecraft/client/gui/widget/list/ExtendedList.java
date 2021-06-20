package net.minecraft.client.gui.widget.list;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ExtendedList<E extends AbstractList.AbstractListEntry<E>> extends AbstractList<E> {
   private boolean focused;

   public ExtendedList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
      super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
   }

   public boolean changeFocus(boolean focus) {
      if (!this.focused && this.getItemCount() == 0) {
         return false;
      } else {
         this.focused = !this.focused;
         if (this.focused && this.getSelected() == null && this.getItemCount() > 0) {
            this.moveSelection(AbstractList.Ordering.DOWN);
         } else if (this.focused && this.getSelected() != null) {
            this.func_241574_n_();
         }

         return this.focused;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public abstract static class AbstractListEntry<E extends ExtendedList.AbstractListEntry<E>> extends AbstractList.AbstractListEntry<E> {
      public boolean changeFocus(boolean focus) {
         return false;
      }
   }
}