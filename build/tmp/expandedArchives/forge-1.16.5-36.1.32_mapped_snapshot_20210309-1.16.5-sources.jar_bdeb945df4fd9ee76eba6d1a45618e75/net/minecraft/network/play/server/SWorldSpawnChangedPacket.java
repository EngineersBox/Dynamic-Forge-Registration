package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SWorldSpawnChangedPacket implements IPacket<IClientPlayNetHandler> {
   private BlockPos spawnPos;
   private float spawnAngle;

   public SWorldSpawnChangedPacket() {
   }

   public SWorldSpawnChangedPacket(BlockPos spawnPos, float spawnAngle) {
      this.spawnPos = spawnPos;
      this.spawnAngle = spawnAngle;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.spawnPos = buf.readBlockPos();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeBlockPos(this.spawnPos);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.func_230488_a_(this);
   }

   @OnlyIn(Dist.CLIENT)
   public BlockPos getSpawnPos() {
      return this.spawnPos;
   }

   @OnlyIn(Dist.CLIENT)
   public float getSpawnAngle() {
      return this.spawnAngle;
   }
}