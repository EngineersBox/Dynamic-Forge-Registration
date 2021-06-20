package net.minecraft.loot;

public class LootPoolEntryType extends LootType<LootEntry> {
   public LootPoolEntryType(ILootSerializer<? extends LootEntry> serializer) {
      super(serializer);
   }
}