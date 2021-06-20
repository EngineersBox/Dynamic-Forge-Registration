package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CLockDifficultyPacket implements IPacket<IServerPlayNetHandler> {
   private boolean locked;

   public CLockDifficultyPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CLockDifficultyPacket(boolean locked) {
      this.locked = locked;
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.func_217261_a(this);
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.locked = buf.readBoolean();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeBoolean(this.locked);
   }

   public boolean isLocked() {
      return this.locked;
   }
}