package net.minecraft.util.text;

import java.util.function.UnaryOperator;

public interface IFormattableTextComponent extends ITextComponent {
   IFormattableTextComponent setStyle(Style style);

   default IFormattableTextComponent appendString(String string) {
      return this.appendSibling(new StringTextComponent(string));
   }

   IFormattableTextComponent appendSibling(ITextComponent sibling);

   default IFormattableTextComponent modifyStyle(UnaryOperator<Style> modifyFunc) {
      this.setStyle(modifyFunc.apply(this.getStyle()));
      return this;
   }

   default IFormattableTextComponent mergeStyle(Style style) {
      this.setStyle(style.mergeStyle(this.getStyle()));
      return this;
   }

   default IFormattableTextComponent mergeStyle(TextFormatting... formats) {
      this.setStyle(this.getStyle().mergeWithFormatting(formats));
      return this;
   }

   default IFormattableTextComponent mergeStyle(TextFormatting format) {
      this.setStyle(this.getStyle().applyFormatting(format));
      return this;
   }
}