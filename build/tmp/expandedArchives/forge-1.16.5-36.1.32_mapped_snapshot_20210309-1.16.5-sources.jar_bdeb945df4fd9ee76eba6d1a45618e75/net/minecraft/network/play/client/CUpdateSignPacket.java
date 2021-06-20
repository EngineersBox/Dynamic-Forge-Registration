package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CUpdateSignPacket implements IPacket<IServerPlayNetHandler> {
   private BlockPos pos;
   private String[] lines;

   public CUpdateSignPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CUpdateSignPacket(BlockPos pos, String line1, String line2, String line3, String line4) {
      this.pos = pos;
      this.lines = new String[]{line1, line2, line3, line4};
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.pos = buf.readBlockPos();
      this.lines = new String[4];

      for(int i = 0; i < 4; ++i) {
         this.lines[i] = buf.readString(384);
      }

   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeBlockPos(this.pos);

      for(int i = 0; i < 4; ++i) {
         buf.writeString(this.lines[i]);
      }

   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.processUpdateSign(this);
   }

   public BlockPos getPosition() {
      return this.pos;
   }

   public String[] getLines() {
      return this.lines;
   }
}