package net.minecraft.item;

public class DyeableHorseArmorItem extends HorseArmorItem implements IDyeableArmorItem {
   public DyeableHorseArmorItem(int armorValue, String tierArmor, Item.Properties builder) {
      super(armorValue, tierArmor, builder);
   }
   public DyeableHorseArmorItem(int armorValue, net.minecraft.util.ResourceLocation texture, Item.Properties builder) {
      super(armorValue, texture, builder);
   }
}
