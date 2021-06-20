package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CJigsawBlockGeneratePacket implements IPacket<IServerPlayNetHandler> {
   private BlockPos pos;
   private int field_240842_b_;
   private boolean keepJigsaws;

   public CJigsawBlockGeneratePacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CJigsawBlockGeneratePacket(BlockPos pos, int p_i232583_2_, boolean keepJigsaws) {
      this.pos = pos;
      this.field_240842_b_ = p_i232583_2_;
      this.keepJigsaws = keepJigsaws;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.pos = buf.readBlockPos();
      this.field_240842_b_ = buf.readVarInt();
      this.keepJigsaws = buf.readBoolean();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeBlockPos(this.pos);
      buf.writeVarInt(this.field_240842_b_);
      buf.writeBoolean(this.keepJigsaws);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.func_230549_a_(this);
   }

   public BlockPos getPosition() {
      return this.pos;
   }

   public int func_240845_c_() {
      return this.field_240842_b_;
   }

   public boolean shouldKeepJigsaws() {
      return this.keepJigsaws;
   }
}