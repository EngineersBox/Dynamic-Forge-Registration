package net.minecraft.network.play.server;

import it.unimi.dsi.fastutil.shorts.ShortSet;
import java.io.IOException;
import java.util.function.BiConsumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SMultiBlockChangePacket implements IPacket<IClientPlayNetHandler> {
   private SectionPos sectionPos;
   private short[] positions;
   private BlockState[] states;
   private boolean field_244308_d;

   public SMultiBlockChangePacket() {
   }

   public SMultiBlockChangePacket(SectionPos sectionPos, ShortSet positions, ChunkSection chunkSection, boolean p_i242085_4_) {
      this.sectionPos = sectionPos;
      this.field_244308_d = p_i242085_4_;
      this.setArraySizes(positions.size());
      int i = 0;

      for(short short1 : positions) {
         this.positions[i] = short1;
         this.states[i] = chunkSection.getBlockState(SectionPos.func_243641_a(short1), SectionPos.func_243642_b(short1), SectionPos.func_243643_c(short1));
         ++i;
      }

   }

   private void setArraySizes(int setLength) {
      this.positions = new short[setLength];
      this.states = new BlockState[setLength];
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.sectionPos = SectionPos.from(buf.readLong());
      this.field_244308_d = buf.readBoolean();
      int i = buf.readVarInt();
      this.setArraySizes(i);

      for(int j = 0; j < this.positions.length; ++j) {
         long k = buf.readVarLong();
         this.positions[j] = (short)((int)(k & 4095L));
         this.states[j] = Block.BLOCK_STATE_IDS.getByValue((int)(k >>> 12));
      }

   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeLong(this.sectionPos.asLong());
      buf.writeBoolean(this.field_244308_d);
      buf.writeVarInt(this.positions.length);

      for(int i = 0; i < this.positions.length; ++i) {
         buf.writeVarLong((long)(Block.getStateId(this.states[i]) << 12 | this.positions[i]));
      }

   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleMultiBlockChange(this);
   }

   public void alterBlock(BiConsumer<BlockPos, BlockState> consumer) {
      BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

      for(int i = 0; i < this.positions.length; ++i) {
         short short1 = this.positions[i];
         blockpos$mutable.setPos(this.sectionPos.func_243644_d(short1), this.sectionPos.func_243645_e(short1), this.sectionPos.func_243646_f(short1));
         consumer.accept(blockpos$mutable, this.states[i]);
      }

   }

   @OnlyIn(Dist.CLIENT)
   public boolean func_244311_b() {
      return this.field_244308_d;
   }
}