package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SEntityEquipmentPacket implements IPacket<IClientPlayNetHandler> {
   private int entityID;
   private final List<Pair<EquipmentSlotType, ItemStack>> equippedMap;

   public SEntityEquipmentPacket() {
      this.equippedMap = Lists.newArrayList();
   }

   public SEntityEquipmentPacket(int entityID, List<Pair<EquipmentSlotType, ItemStack>> equippedMap) {
      this.entityID = entityID;
      this.equippedMap = equippedMap;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.entityID = buf.readVarInt();
      EquipmentSlotType[] aequipmentslottype = EquipmentSlotType.values();

      int i;
      do {
         i = buf.readByte();
         EquipmentSlotType equipmentslottype = aequipmentslottype[i & 127];
         ItemStack itemstack = buf.readItemStack();
         this.equippedMap.add(Pair.of(equipmentslottype, itemstack));
      } while((i & -128) != 0);

   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarInt(this.entityID);
      int i = this.equippedMap.size();

      for(int j = 0; j < i; ++j) {
         Pair<EquipmentSlotType, ItemStack> pair = this.equippedMap.get(j);
         EquipmentSlotType equipmentslottype = pair.getFirst();
         boolean flag = j != i - 1;
         int k = equipmentslottype.ordinal();
         buf.writeByte(flag ? k | -128 : k);
         buf.writeItemStack(pair.getSecond());
      }

   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleEntityEquipment(this);
   }

   @OnlyIn(Dist.CLIENT)
   public int getEntityID() {
      return this.entityID;
   }

   @OnlyIn(Dist.CLIENT)
   public List<Pair<EquipmentSlotType, ItemStack>> getEquipped() {
      return this.equippedMap;
   }
}