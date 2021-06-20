package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.Validate;

public class SSpawnMovingSoundEffectPacket implements IPacket<IClientPlayNetHandler> {
   private SoundEvent sound;
   private SoundCategory category;
   private int entityId;
   private float volume;
   private float pitch;

   public SSpawnMovingSoundEffectPacket() {
   }

   public SSpawnMovingSoundEffectPacket(SoundEvent sound, SoundCategory category, Entity entity, float volume, float pitch) {
      Validate.notNull(sound, "sound");
      this.sound = sound;
      this.category = category;
      this.entityId = entity.getEntityId();
      this.volume = volume;
      this.pitch = pitch;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.sound = Registry.SOUND_EVENT.getByValue(buf.readVarInt());
      this.category = buf.readEnumValue(SoundCategory.class);
      this.entityId = buf.readVarInt();
      this.volume = buf.readFloat();
      this.pitch = buf.readFloat();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarInt(Registry.SOUND_EVENT.getId(this.sound));
      buf.writeEnumValue(this.category);
      buf.writeVarInt(this.entityId);
      buf.writeFloat(this.volume);
      buf.writeFloat(this.pitch);
   }

   @OnlyIn(Dist.CLIENT)
   public SoundEvent getSound() {
      return this.sound;
   }

   @OnlyIn(Dist.CLIENT)
   public SoundCategory getCategory() {
      return this.category;
   }

   @OnlyIn(Dist.CLIENT)
   public int func_218762_d() {
      return this.entityId;
   }

   @OnlyIn(Dist.CLIENT)
   public float getVolume() {
      return this.volume;
   }

   @OnlyIn(Dist.CLIENT)
   public float getPitch() {
      return this.pitch;
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleSpawnMovingSoundEffect(this);
   }
}