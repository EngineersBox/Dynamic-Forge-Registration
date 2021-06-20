package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CUpdateStructureBlockPacket implements IPacket<IServerPlayNetHandler> {
   private BlockPos pos;
   private StructureBlockTileEntity.UpdateCommand updateCommand;
   private StructureMode mode;
   private String name;
   private BlockPos position;
   private BlockPos size;
   private Mirror mirror;
   private Rotation rotation;
   private String metaData;
   private boolean ignoreEntities;
   private boolean showAir;
   private boolean showBoundingBox;
   private float integrity;
   private long seed;

   public CUpdateStructureBlockPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CUpdateStructureBlockPacket(BlockPos pos, StructureBlockTileEntity.UpdateCommand updateCommand, StructureMode mode, String name, BlockPos p_i49541_5_, BlockPos size, Mirror mirror, Rotation rotation, String metaData, boolean ignoreEntities, boolean showAir, boolean showBoundingBox, float integrity, long seed) {
      this.pos = pos;
      this.updateCommand = updateCommand;
      this.mode = mode;
      this.name = name;
      this.position = p_i49541_5_;
      this.size = size;
      this.mirror = mirror;
      this.rotation = rotation;
      this.metaData = metaData;
      this.ignoreEntities = ignoreEntities;
      this.showAir = showAir;
      this.showBoundingBox = showBoundingBox;
      this.integrity = integrity;
      this.seed = seed;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.pos = buf.readBlockPos();
      this.updateCommand = buf.readEnumValue(StructureBlockTileEntity.UpdateCommand.class);
      this.mode = buf.readEnumValue(StructureMode.class);
      this.name = buf.readString(32767);
      int i = 48;
      this.position = new BlockPos(MathHelper.clamp(buf.readByte(), -48, 48), MathHelper.clamp(buf.readByte(), -48, 48), MathHelper.clamp(buf.readByte(), -48, 48));
      int j = 48;
      this.size = new BlockPos(MathHelper.clamp(buf.readByte(), 0, 48), MathHelper.clamp(buf.readByte(), 0, 48), MathHelper.clamp(buf.readByte(), 0, 48));
      this.mirror = buf.readEnumValue(Mirror.class);
      this.rotation = buf.readEnumValue(Rotation.class);
      this.metaData = buf.readString(12);
      this.integrity = MathHelper.clamp(buf.readFloat(), 0.0F, 1.0F);
      this.seed = buf.readVarLong();
      int k = buf.readByte();
      this.ignoreEntities = (k & 1) != 0;
      this.showAir = (k & 2) != 0;
      this.showBoundingBox = (k & 4) != 0;
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeBlockPos(this.pos);
      buf.writeEnumValue(this.updateCommand);
      buf.writeEnumValue(this.mode);
      buf.writeString(this.name);
      buf.writeByte(this.position.getX());
      buf.writeByte(this.position.getY());
      buf.writeByte(this.position.getZ());
      buf.writeByte(this.size.getX());
      buf.writeByte(this.size.getY());
      buf.writeByte(this.size.getZ());
      buf.writeEnumValue(this.mirror);
      buf.writeEnumValue(this.rotation);
      buf.writeString(this.metaData);
      buf.writeFloat(this.integrity);
      buf.writeVarLong(this.seed);
      int i = 0;
      if (this.ignoreEntities) {
         i |= 1;
      }

      if (this.showAir) {
         i |= 2;
      }

      if (this.showBoundingBox) {
         i |= 4;
      }

      buf.writeByte(i);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.processUpdateStructureBlock(this);
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public StructureBlockTileEntity.UpdateCommand getUpdateCommand() {
      return this.updateCommand;
   }

   public StructureMode getMode() {
      return this.mode;
   }

   public String getName() {
      return this.name;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public BlockPos getSize() {
      return this.size;
   }

   public Mirror getMirror() {
      return this.mirror;
   }

   public Rotation getRotation() {
      return this.rotation;
   }

   public String getMetadata() {
      return this.metaData;
   }

   public boolean shouldIgnoreEntities() {
      return this.ignoreEntities;
   }

   public boolean shouldShowAir() {
      return this.showAir;
   }

   public boolean shouldShowBoundingBox() {
      return this.showBoundingBox;
   }

   public float getIntegrity() {
      return this.integrity;
   }

   public long getSeed() {
      return this.seed;
   }
}