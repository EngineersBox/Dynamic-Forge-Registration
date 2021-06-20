package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CQueryTileEntityNBTPacket implements IPacket<IServerPlayNetHandler> {
   private int transactionId;
   private BlockPos pos;

   public CQueryTileEntityNBTPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CQueryTileEntityNBTPacket(int transactionId, BlockPos pos) {
      this.transactionId = transactionId;
      this.pos = pos;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.transactionId = buf.readVarInt();
      this.pos = buf.readBlockPos();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarInt(this.transactionId);
      buf.writeBlockPos(this.pos);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.processNBTQueryBlockEntity(this);
   }

   public int getTransactionId() {
      return this.transactionId;
   }

   public BlockPos getPosition() {
      return this.pos;
   }
}