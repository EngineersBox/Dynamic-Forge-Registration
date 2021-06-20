package net.minecraft.network.play.server;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Set;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SJoinGamePacket implements IPacket<IClientPlayNetHandler> {
   private int playerId;
   /** First 8 bytes of the SHA-256 hash of the world's seed */
   private long hashedSeed;
   private boolean hardcoreMode;
   private GameType gameType;
   private GameType previousGameType;
   private Set<RegistryKey<World>> dimensionKeys;
   private DynamicRegistries.Impl dynamicRegistries;
   private DimensionType spawnDimension;
   private RegistryKey<World> dimension;
   private int maxPlayers;
   private int viewDistance;
   private boolean reducedDebugInfo;
   /** Set to false when the doImmediateRespawn gamerule is true */
   private boolean enableRespawnScreen;
   private boolean field_240814_m_;
   private boolean flatWorld;

   public SJoinGamePacket() {
   }

   public SJoinGamePacket(int playerId, GameType gameType, GameType previousGameType, long hashedSeed, boolean hardcoreMode, Set<RegistryKey<World>> dimensionKeys, DynamicRegistries.Impl dynamicRegistries, DimensionType spawnDimension, RegistryKey<World> dimension, int maxPlayers, int viewDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean p_i242082_15_, boolean flatWorld) {
      this.playerId = playerId;
      this.dimensionKeys = dimensionKeys;
      this.dynamicRegistries = dynamicRegistries;
      this.spawnDimension = spawnDimension;
      this.dimension = dimension;
      this.hashedSeed = hashedSeed;
      this.gameType = gameType;
      this.previousGameType = previousGameType;
      this.maxPlayers = maxPlayers;
      this.hardcoreMode = hardcoreMode;
      this.viewDistance = viewDistance;
      this.reducedDebugInfo = reducedDebugInfo;
      this.enableRespawnScreen = enableRespawnScreen;
      this.field_240814_m_ = p_i242082_15_;
      this.flatWorld = flatWorld;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.playerId = buf.readInt();
      this.hardcoreMode = buf.readBoolean();
      this.gameType = GameType.getByID(buf.readByte());
      this.previousGameType = GameType.getByID(buf.readByte());
      int i = buf.readVarInt();
      this.dimensionKeys = Sets.newHashSet();

      for(int j = 0; j < i; ++j) {
         this.dimensionKeys.add(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buf.readResourceLocation()));
      }

      this.dynamicRegistries = buf.func_240628_a_(DynamicRegistries.Impl.registryCodec);
      this.spawnDimension = buf.func_240628_a_(DimensionType.DIMENSION_TYPE_CODEC).get();
      this.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buf.readResourceLocation());
      this.hashedSeed = buf.readLong();
      this.maxPlayers = buf.readVarInt();
      this.viewDistance = buf.readVarInt();
      this.reducedDebugInfo = buf.readBoolean();
      this.enableRespawnScreen = buf.readBoolean();
      this.field_240814_m_ = buf.readBoolean();
      this.flatWorld = buf.readBoolean();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeInt(this.playerId);
      buf.writeBoolean(this.hardcoreMode);
      buf.writeByte(this.gameType.getID());
      buf.writeByte(this.previousGameType.getID());
      buf.writeVarInt(this.dimensionKeys.size());

      for(RegistryKey<World> registrykey : this.dimensionKeys) {
         buf.writeResourceLocation(registrykey.getLocation());
      }

      buf.func_240629_a_(DynamicRegistries.Impl.registryCodec, this.dynamicRegistries);
      buf.func_240629_a_(DimensionType.DIMENSION_TYPE_CODEC, () -> {
         return this.spawnDimension;
      });
      buf.writeResourceLocation(this.dimension.getLocation());
      buf.writeLong(this.hashedSeed);
      buf.writeVarInt(this.maxPlayers);
      buf.writeVarInt(this.viewDistance);
      buf.writeBoolean(this.reducedDebugInfo);
      buf.writeBoolean(this.enableRespawnScreen);
      buf.writeBoolean(this.field_240814_m_);
      buf.writeBoolean(this.flatWorld);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleJoinGame(this);
   }

   @OnlyIn(Dist.CLIENT)
   public int getPlayerId() {
      return this.playerId;
   }

   /**
    * get value
    */
   @OnlyIn(Dist.CLIENT)
   public long getHashedSeed() {
      return this.hashedSeed;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isHardcoreMode() {
      return this.hardcoreMode;
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
   public Set<RegistryKey<World>> getDimensionKeys() {
      return this.dimensionKeys;
   }

   @OnlyIn(Dist.CLIENT)
   public DynamicRegistries getDynamicRegistries() {
      return this.dynamicRegistries;
   }

   @OnlyIn(Dist.CLIENT)
   public DimensionType getSpawnDimension() {
      return this.spawnDimension;
   }

   @OnlyIn(Dist.CLIENT)
   public RegistryKey<World> getDimension() {
      return this.dimension;
   }

   @OnlyIn(Dist.CLIENT)
   public int getViewDistance() {
      return this.viewDistance;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isReducedDebugInfo() {
      return this.reducedDebugInfo;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean shouldEnableRespawnScreen() {
      return this.enableRespawnScreen;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean func_240820_n_() {
      return this.field_240814_m_;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFlatWorld() {
      return this.flatWorld;
   }
}