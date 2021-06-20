package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SPlaySoundPacket implements IPacket<IClientPlayNetHandler> {
   private ResourceLocation soundName;
   private SoundCategory category;
   private int x;
   private int y = Integer.MAX_VALUE;
   private int z;
   private float volume;
   private float pitch;

   public SPlaySoundPacket() {
   }

   public SPlaySoundPacket(ResourceLocation soundName, SoundCategory category, Vector3d positionVector, float volume, float pitch) {
      this.soundName = soundName;
      this.category = category;
      this.x = (int)(positionVector.x * 8.0D);
      this.y = (int)(positionVector.y * 8.0D);
      this.z = (int)(positionVector.z * 8.0D);
      this.volume = volume;
      this.pitch = pitch;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.soundName = buf.readResourceLocation();
      this.category = buf.readEnumValue(SoundCategory.class);
      this.x = buf.readInt();
      this.y = buf.readInt();
      this.z = buf.readInt();
      this.volume = buf.readFloat();
      this.pitch = buf.readFloat();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeResourceLocation(this.soundName);
      buf.writeEnumValue(this.category);
      buf.writeInt(this.x);
      buf.writeInt(this.y);
      buf.writeInt(this.z);
      buf.writeFloat(this.volume);
      buf.writeFloat(this.pitch);
   }

   @OnlyIn(Dist.CLIENT)
   public ResourceLocation getSoundName() {
      return this.soundName;
   }

   @OnlyIn(Dist.CLIENT)
   public SoundCategory getCategory() {
      return this.category;
   }

   @OnlyIn(Dist.CLIENT)
   public double getX() {
      return (double)((float)this.x / 8.0F);
   }

   @OnlyIn(Dist.CLIENT)
   public double getY() {
      return (double)((float)this.y / 8.0F);
   }

   @OnlyIn(Dist.CLIENT)
   public double getZ() {
      return (double)((float)this.z / 8.0F);
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
      handler.handleCustomSound(this);
   }
}