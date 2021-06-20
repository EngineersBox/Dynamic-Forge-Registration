package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SOpenHorseWindowPacket implements IPacket<IClientPlayNetHandler> {
   private int windowId;
   private int field_218706_b;
   private int entityId;

   public SOpenHorseWindowPacket() {
   }

   public SOpenHorseWindowPacket(int windowId, int p_i50776_2_, int entityId) {
      this.windowId = windowId;
      this.field_218706_b = p_i50776_2_;
      this.entityId = entityId;
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleOpenHorseWindow(this);
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.windowId = buf.readUnsignedByte();
      this.field_218706_b = buf.readVarInt();
      this.entityId = buf.readInt();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeByte(this.windowId);
      buf.writeVarInt(this.field_218706_b);
      buf.writeInt(this.entityId);
   }

   @OnlyIn(Dist.CLIENT)
   public int getWindowId() {
      return this.windowId;
   }

   @OnlyIn(Dist.CLIENT)
   public int func_218702_c() {
      return this.field_218706_b;
   }

   @OnlyIn(Dist.CLIENT)
   public int getEntityId() {
      return this.entityId;
   }
}