package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SRespawnPacket implements IPacket<IClientPlayNetHandler> {
   private DimensionType field_240822_a_;
   private RegistryKey<World> dimension;
   /** First 8 bytes of the SHA-256 hash of the world's seed */
   private long hashedSeed;
   private GameType gameType;
   private GameType previousGameType;
   private boolean field_240823_e_;
   private boolean isFlatWorld;
   private boolean field_240825_g_;

   public SRespawnPacket() {
   }

   public SRespawnPacket(DimensionType p_i242084_1_, RegistryKey<World> p_i242084_2_, long hashedSeed, GameType gameType, GameType previousGameType, boolean p_i242084_7_, boolean isFlatWorld, boolean p_i242084_9_) {
      this.field_240822_a_ = p_i242084_1_;
      this.dimension = p_i242084_2_;
      this.hashedSeed = hashedSeed;
      this.gameType = gameType;
      this.previousGameType = previousGameType;
      this.field_240823_e_ = p_i242084_7_;
      this.isFlatWorld = isFlatWorld;
      this.field_240825_g_ = p_i242084_9_;
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleRespawn(this);
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.field_240822_a_ = buf.func_240628_a_(DimensionType.DIMENSION_TYPE_CODEC).get();
      this.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buf.readResourceLocation());
      this.hashedSeed = buf.readLong();
      this.gameType = GameType.getByID(buf.readUnsignedByte());
      this.previousGameType = GameType.getByID(buf.readUnsignedByte());
      this.field_240823_e_ = buf.readBoolean();
      this.isFlatWorld = buf.readBoolean();
      this.field_240825_g_ = buf.readBoolean();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.func_240629_a_(DimensionType.DIMENSION_TYPE_CODEC, () -> {
         return this.field_240822_a_;
      });
      buf.writeResourceLocation(this.dimension.getLocation());
      buf.writeLong(this.hashedSeed);
      buf.writeByte(this.gameType.getID());
      buf.writeByte(this.previousGameType.getID());
      buf.writeBoolean(this.field_240823_e_);
      buf.writeBoolean(this.isFlatWorld);
      buf.writeBoolean(this.field_240825_g_);
   }

   @OnlyIn(Dist.CLIENT)
   public DimensionType func_244303_b() {
      return this.field_240822_a_;
   }

   @OnlyIn(Dist.CLIENT)
   public RegistryKey<World> func_240827_c_() {
      return this.dimension;
   }

   /**
    * get value
    */
   @OnlyIn(Dist.CLIENT)
   public long getHashedSeed() {
      return this.hashedSeed;
   }

   @OnlyIn(Dist.CLIENT)
   public GameType getGameType() {
      return this.gameType;
   }

   @OnlyIn(Dist.CLIENT)
   public GameType getPreviousGameType() {
      return this.previousGameType;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean func_240828_f_() {
      return this.field_240823_e_;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFlatWorld() {
      return this.isFlatWorld;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean func_240830_h_() {
      return this.field_240825_g_;
   }
}