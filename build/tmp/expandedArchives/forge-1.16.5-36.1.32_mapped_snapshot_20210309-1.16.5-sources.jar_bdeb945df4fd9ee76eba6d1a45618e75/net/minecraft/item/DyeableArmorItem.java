package net.minecraft.item;

import net.minecraft.inventory.EquipmentSlotType;

public class DyeableArmorItem extends ArmorItem implements IDyeableArmorItem {
   public DyeableArmorItem(IArmorMaterial material, EquipmentSlotType slot, Item.Properties properties) {
      super(material, slot, properties);
   }
}