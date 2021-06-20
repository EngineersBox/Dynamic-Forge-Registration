package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CPlayerTryUseItemOnBlockPacket implements IPacket<IServerPlayNetHandler> {
   private BlockRayTraceResult rayTraceResult;
   private Hand hand;

   public CPlayerTryUseItemOnBlockPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CPlayerTryUseItemOnBlockPacket(Hand hand, BlockRayTraceResult rayTraceResult) {
      this.hand = hand;
      this.rayTraceResult = rayTraceResult;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.hand = buf.readEnumValue(Hand.class);
      this.rayTraceResult = buf.readBlockRay();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeEnumValue(this.hand);
      buf.writeBlockRay(this.rayTraceResult);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.processTryUseItemOnBlock(this);
   }

   public Hand getHand() {
      return this.hand;
   }

   public BlockRayTraceResult getRayTraceResult() {
      return this.rayTraceResult;
   }
}