package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SCollectItemPacket implements IPacket<IClientPlayNetHandler> {
   private int collectedItemEntityId;
   private int entityId;
   private int collectedQuantity;

   public SCollectItemPacket() {
   }

   public SCollectItemPacket(int collectedItemEntityId, int entityId, int collectedQuantity) {
      this.collectedItemEntityId = collectedItemEntityId;
      this.entityId = entityId;
      this.collectedQuantity = collectedQuantity;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.collectedItemEntityId = buf.readVarInt();
      this.entityId = buf.readVarInt();
      this.collectedQuantity = buf.readVarInt();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarInt(this.collectedItemEntityId);
      buf.writeVarInt(this.entityId);
      buf.writeVarInt(this.collectedQuantity);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleCollectItem(this);
   }

   @OnlyIn(Dist.CLIENT)
   public int getCollectedItemEntityID() {
      return this.collectedItemEntityId;
   }

   @OnlyIn(Dist.CLIENT)
   public int getEntityID() {
      return this.entityId;
   }

   @OnlyIn(Dist.CLIENT)
   public int getAmount() {
      return this.collectedQuantity;
   }
}