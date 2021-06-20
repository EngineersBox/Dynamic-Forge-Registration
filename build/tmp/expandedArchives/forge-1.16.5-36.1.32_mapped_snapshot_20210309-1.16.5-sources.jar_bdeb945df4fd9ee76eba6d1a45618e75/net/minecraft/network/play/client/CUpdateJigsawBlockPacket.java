package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.tileentity.JigsawTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CUpdateJigsawBlockPacket implements IPacket<IServerPlayNetHandler> {
   private BlockPos pos;
   private ResourceLocation name;
   private ResourceLocation target;
   private ResourceLocation pool;
   private String blockName;
   private JigsawTileEntity.OrientationType joint;

   public CUpdateJigsawBlockPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CUpdateJigsawBlockPacket(BlockPos pos, ResourceLocation name, ResourceLocation target, ResourceLocation pool, String blockName, JigsawTileEntity.OrientationType joint) {
      this.pos = pos;
      this.name = name;
      this.target = target;
      this.pool = pool;
      this.blockName = blockName;
      this.joint = joint;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.pos = buf.readBlockPos();
      this.name = buf.readResourceLocation();
      this.target = buf.readResourceLocation();
      this.pool = buf.readResourceLocation();
      this.blockName = buf.readString(32767);
      this.joint = JigsawTileEntity.OrientationType.func_235673_a_(buf.readString(32767)).orElse(JigsawTileEntity.OrientationType.ALIGNED);
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeBlockPos(this.pos);
      buf.writeResourceLocation(this.name);
      buf.writeResourceLocation(this.target);
      buf.writeResourceLocation(this.pool);
      buf.writeString(this.blockName);
      buf.writeString(this.joint.getString());
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.func_217262_a(this);
   }

   public BlockPos getPosition() {
      return this.pos;
   }

   public ResourceLocation getName() {
      return this.name;
   }

   public ResourceLocation getTarget() {
      return this.target;
   }

   public ResourceLocation getPool() {
      return this.pool;
   }

   public String getBlockName() {
      return this.blockName;
   }

   public JigsawTileEntity.OrientationType getJointOrientation() {
      return this.joint;
   }
}